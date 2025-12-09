package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service class dedicated to generating questions for the "Multi-Fact Mix" quiz.
 * <p>
 * This quiz type fetches comprehensive game data (release date, rating, genres, platforms)
 * and generates diverse questions based on multiple attributes simultaneously.
 *
 * @author machm
 */
@Service
public class MultiFactMixQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    /**
     * Retrieves one random game that meets the criteria (has release date, rating, and platforms defined)
     * and generates a question based on a random template (0-6).
     *
     * @return A fully populated {@link Question} object, or null if no suitable game is found.
     */
    public Question getNextQuestion() {
        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date IS NOT NULL
              AND rating IS NOT NULL
              AND array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                float rating = rs.getFloat("rating");
                Array genresArray = rs.getArray("genres");
                Array platformsArray = rs.getArray("platforms");

                List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : new ArrayList<>();
                List<String> platforms = platformsArray != null ? Arrays.asList((String[]) platformsArray.getArray()) : new ArrayList<>();

                int template = new Random().nextInt(18); // 0-17
                Question q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setGameId(id);
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> {
                        q.setQuestionText("Czy gra '" + title + "' została wydana przed 2010 rokiem?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2010, 1, 1)) ? "tak" : "nie");
                    }
                    case 1 -> {
                        q.setQuestionText("Czy gra '" + title + "' ma ocenę powyżej 4.0?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(rating > 4.0 ? "tak" : "nie");
                    }
                    case 2 -> {
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku Action?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean isAction = genres.stream().anyMatch(g -> g.equalsIgnoreCase("Action"));
                        q.setCorrectAnswer(isAction ? "tak" : "nie");
                    }
                    case 3 -> {
                        q.setQuestionText("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                    case 4 -> {
                        int year = releaseDate.getYear();
                        q.setQuestionText("W której dekadzie wydano grę '" + title + "'?");
                        q.setOptions(List.of("2000-2009", "2010-2019", "2020-2029"));
                        String decade = (year < 2010) ? "2000-2009" : (year < 2020 ? "2010-2019" : "2020-2029");
                        q.setCorrectAnswer(decade);
                    }
                    case 5 -> {
                        List<String> possibleGenres = List.of("Action", "RPG", "Shooter", "Puzzle", "Strategy", "Adventure");
                        String chosenGenre = possibleGenres.get(new Random().nextInt(possibleGenres.size()));
                        q.setQuestionText("Czy gra '" + title + "' należy do gatunku " + chosenGenre + "?");
                        q.setOptions(List.of("tak", "nie"));
                        boolean hasGenre = genres.stream().anyMatch(g -> g.equalsIgnoreCase(chosenGenre));
                        q.setCorrectAnswer(hasGenre ? "tak" : "nie");
                    }
                    case 6 -> { // Platforma losowa z dostępnych
                        if (!platforms.isEmpty()) {
                            Collections.shuffle(platforms);
                            q.setOptions(platforms); // frontend użyje input jeśli lista będzie pusta
                            q.setCorrectAnswer(platforms.get(0));
                            q.setQuestionText("Na której z poniższych platform wydano grę '" + title + "'?");
                        } else {
                            q.setQuestionText("Brak dostępnych platform dla gry '" + title + "'");
                            q.setOptions(List.of());
                            q.setCorrectAnswer("");
                        }
                    }
                    case 7 -> {
                        int count = new Random().nextInt(1, 4); // 1–3
                        q.setQuestionText("Czy gra '" + title + "' ma więcej niż " + count + " gatunków?");
                        q.setOptions(List.of("tak", "nie"));
                        q.setCorrectAnswer(genres.size() > count ? "tak" : "nie");
                    }
                    case 8 -> {
                        int age = LocalDate.now().getYear() - releaseDate.getYear();
                        q.setQuestionText("Ile lat ma gra '" + title + "'?");
                        q.setOptions(List.of());
                        q.setCorrectAnswer(String.valueOf(age));
                    }
                    case 9 -> {
                        List<String> ranges = List.of("0–2", "2–3", "3–4", "4–5");
                        String correctRange =
                                (rating < 2) ? "0–2" :
                                        (rating < 3) ? "2–3" :
                                                (rating < 4) ? "3–4" : "4–5";

                        q.setQuestionText("W jakim zakresie znajduje się ocena gry '" + title + "'?");
                        q.setOptions(ranges);
                        q.setCorrectAnswer(correctRange);
                    }
                    case 10 -> {
                        String otherQuery = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            if (!r2.next()) return null;

                            String otherTitle = r2.getString("title");
                            LocalDate otherDate = r2.getDate("release_date").toLocalDate();

                            q.setQuestionText("Czy gra '" + title + "' wyszła wcześniej niż '" + otherTitle + "'?");
                            q.setOptions(List.of("tak", "nie"));
                            q.setCorrectAnswer(releaseDate.isBefore(otherDate) ? "tak" : "nie");
                        }
                    }
                    case 11 -> {
                        String otherQuery = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            if (!r2.next()) return null; // <-- zamiast break

                            String otherTitle = r2.getString("title");
                            LocalDate otherDate = r2.getDate("release_date").toLocalDate();

                            int diff = Math.abs(releaseDate.getYear() - otherDate.getYear());

                            q.setQuestionText("Ile lat różnicy dzieli premiery gier '" + title + "' i '" + otherTitle + "'?");
                            q.setOptions(List.of());
                            q.setCorrectAnswer(String.valueOf(diff));
                        }
                    }

                    case 12 -> {
                        String otherQuery = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            if (!r2.next()) break;

                            String otherTitle = r2.getString("title");
                            LocalDate otherDate = r2.getDate("release_date").toLocalDate();

                            q.setQuestionText("Która gra ukazała się wcześniej?");
                            q.setOptions(List.of(title, otherTitle));

                            String correct =
                                    releaseDate.isBefore(otherDate) ? title : otherTitle;

                            q.setCorrectAnswer(correct);
                        }
                    }
                    case 13 -> {
                        String otherQuery = """
                            SELECT title, rating
                            FROM games
                            WHERE rating IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            if (!r2.next()) break;

                            String otherTitle = r2.getString("title");
                            float otherRating = r2.getFloat("rating");

                            q.setQuestionText("Która z tych gier ma wyższą ocenę?");
                            q.setOptions(List.of(title, otherTitle));

                            String correct = (rating > otherRating) ? title : otherTitle;

                            q.setCorrectAnswer(correct);
                        }
                    }
                    case 14 -> {
                        String otherQuery = """
                        SELECT title, release_date
                        FROM games
                        WHERE release_date IS NOT NULL
                        ORDER BY RANDOM()
                        LIMIT 1
                    """;

                        try (PreparedStatement s2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            if (!r2.next()) break;

                            String otherTitle = r2.getString("title");
                            LocalDate otherDate = r2.getDate("release_date").toLocalDate();

                            int diff = Math.abs(releaseDate.getYear() - otherDate.getYear());

                            q.setQuestionText("Ile lat różnicy jest między premierami '" + title + "' i '" + otherTitle + "'?");
                            q.setOptions(List.of());
                            q.setCorrectAnswer(String.valueOf(diff));
                        }
                    }
                    case 15 -> {
                        String tripleQuery = """
                            SELECT title, rating
                            FROM games
                            WHERE rating IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 2
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(tripleQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            List<String> titles = new ArrayList<>();
                            List<Float> ratings = new ArrayList<>();

                            titles.add(title);
                            ratings.add(rating);

                            while (r2.next()) {
                                titles.add(r2.getString("title"));
                                ratings.add(r2.getFloat("rating"));
                            }

                            if (titles.size() < 3) return null;

                            q.setQuestionText("Która z tych gier ma najwyższą ocenę?");
                            q.setOptions(titles);

                            int maxIndex = 0;
                            for (int i = 1; i < ratings.size(); i++)
                                if (ratings.get(i) > ratings.get(maxIndex)) maxIndex = i;

                            q.setCorrectAnswer(titles.get(maxIndex));
                        }
                    }
                    case 16 -> {
                        String tripleQuery = """
                            SELECT title, rating
                            FROM games
                            WHERE rating IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 2
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(tripleQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            List<String> titles = new ArrayList<>();
                            List<Float> ratings = new ArrayList<>();

                            titles.add(title);
                            ratings.add(rating);

                            while (r2.next()) {
                                titles.add(r2.getString("title"));
                                ratings.add(r2.getFloat("rating"));
                            }

                            if (titles.size() < 3) return null; // <-- zamiast break

                            q.setQuestionText("Która z tych gier najbardziej odstaje oceną od pozostałych?");
                            q.setOptions(titles);

                            float avg = (ratings.get(0) + ratings.get(1) + ratings.get(2)) / 3f;

                            float maxDiff = -1;
                            int idx = 0;

                            for (int i = 0; i < 3; i++) {
                                float diff = Math.abs(ratings.get(i) - avg);
                                if (diff > maxDiff) {
                                    maxDiff = diff;
                                    idx = i;
                                }
                            }

                            q.setCorrectAnswer(titles.get(idx));
                        }
                    }
                    case 17 -> {
                        String tripleQuery = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date IS NOT NULL
                            ORDER BY RANDOM()
                            LIMIT 2
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(tripleQuery);
                             ResultSet r2 = s2.executeQuery()) {

                            record G(String title, LocalDate date) {}

                            List<G> list = new ArrayList<>();
                            list.add(new G(title, releaseDate));

                            while (r2.next()) {
                                list.add(new G(
                                        r2.getString("title"),
                                        r2.getDate("release_date").toLocalDate()
                                ));
                            }

                            if (list.size() < 3) return null;

                            List<String> options = List.of(
                                    list.get(0).title() + " → " + list.get(1).title() + " → " + list.get(2).title(),
                                    list.get(0).title() + " → " + list.get(2).title() + " → " + list.get(1).title(),
                                    list.get(1).title() + " → " + list.get(0).title() + " → " + list.get(2).title(),
                                    list.get(1).title() + " → " + list.get(2).title() + " → " + list.get(0).title(),
                                    list.get(2).title() + " → " + list.get(0).title() + " → " + list.get(1).title(),
                                    list.get(2).title() + " → " + list.get(1).title() + " → " + list.get(0).title()
                            );

                            list.sort(Comparator.comparing(G::date));
                            String correct = list.get(0).title() + " → " + list.get(1).title() + " → " + list.get(2).title();

                            q.setQuestionText("W jakiej kolejności ukazały się te gry?");
                            q.setOptions(options);
                            q.setCorrectAnswer(correct);
                        }
                    }
                }

                if (q.getOptions() == null || q.getOptions().size() <= 1) {
                    return null;
                }

                return q;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Saves the final result of the MultiFactMix quiz to the database.
     * <p>
     * Note: This method currently uses direct JDBC connection for saving results.
     *
     * @param userId The ID of the user who completed the quiz.
     * @param lastGameId The ID of the last game questioned in the quiz (or -1 if not applicable).
     * @param score The final score achieved.
     * @param timeTakenSeconds The total time taken to complete the quiz.
     */
    public void saveResult(int userId, int lastGameId, int score, int timeTakenSeconds) {
        String insertQuery = """
            INSERT INTO results (user_id, game_id, score, time_taken_seconds)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            insertStmt.setInt(1, userId);
            if (lastGameId != -1) insertStmt.setInt(2, lastGameId);
            else insertStmt.setNull(2, Types.INTEGER);
            insertStmt.setInt(3, score);
            insertStmt.setInt(4, timeTakenSeconds);

            insertStmt.executeUpdate();
            System.out.println("Wynik MultiFactMix zapisany w bazie.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
