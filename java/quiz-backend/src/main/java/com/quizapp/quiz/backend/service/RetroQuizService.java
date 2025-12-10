package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service class dedicated to generating questions for the "Retro Quiz" game mode.
 * <p>
 * Focuses on games released before 2010-01-01 and generates simple True/False or multiple-choice questions
 * based on their core attributes.
 *
 * @author machm
 */
@Service
public class RetroQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    /**
     * Retrieves one random game released before 2010 and generates a question based on a random template (0-3).
     *
     * @return A {@link Question} object containing a retro game challenge, or null if no suitable game is found.
     */
    public Question getNextRetroQuestion() {
        Question q = null;
        String query = """
            SELECT id, title, release_date, rating, genres, platforms
            FROM games
            WHERE release_date < '2010-01-01'
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

                int template = new Random().nextInt(12);
                q = new Question();
                q.setId(id);
                q.setTitle(title);
                q.setTemplateType(template);

                switch (template) {
                    case 0 -> {
                        q.setQuestionText("Which platform did the game '" + title + "' debut on?");
                        q.setOptions(platforms);
                        q.setCorrectAnswer(platforms.isEmpty() ? "" : platforms.get(0));
                    }
                    case 1 -> {
                        q.setQuestionText("Was the game '" + title + "' released before 2005?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2005, 1, 1)) ? "yes" : "no");
                    }
                    case 2 -> {
                        q.setQuestionText("Did the game '" + title + "' have a rating above 3.3?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(rating > 3.3 ? "yes" : "no");
                    }
                    case 3 -> {
                        q.setQuestionText("Is the game '" + title + "' an RPG?");
                        q.setOptions(List.of("yes", "no"));
                        boolean isRPG = genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG"));
                        q.setCorrectAnswer(isRPG ? "yes" : "no");
                    }
                    case 4 -> {
                        List<String> knownPlatforms = Arrays.asList("PC", "PS1", "PS2", "PS3", "Xbox", "Xbox 360", "Wii", "DS");
                        List<String> notOn = knownPlatforms.stream()
                                .filter(p -> platforms.stream().noneMatch(x -> x.equalsIgnoreCase(p)))
                                .toList();

                        if (notOn.size() < 2) return null;

                        Collections.shuffle(new ArrayList<>(notOn));
                        List<String> options = notOn.subList(0, Math.min(3, notOn.size()));

                        q.setQuestionText("Which of these platforms did NOT get the game '" + title + "'?");
                        q.setOptions(options);
                        q.setCorrectAnswer(options.get(0));
                    }
                    case 5 -> {
                        String otherQuery = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date < '2010-01-01'
                            ORDER BY RANDOM()
                            LIMIT 1
                        """;

                        try (PreparedStatement stmt2 = conn.prepareStatement(otherQuery);
                             ResultSet r2 = stmt2.executeQuery()) {
                            if (!r2.next()) return null;

                            String otherTitle = r2.getString("title");
                            LocalDate otherDate = r2.getDate("release_date").toLocalDate();

                            q.setQuestionText("Which game was released earlier?");
                            q.setOptions(List.of(title, otherTitle));
                            q.setCorrectAnswer(releaseDate.isBefore(otherDate) ? title : otherTitle);
                        }
                    }
                    case 6 -> {
                        List<String> retroGenres = Arrays.asList("Platformer", "Adventure", "RPG", "Puzzle", "Arcade");
                        List<String> options = new ArrayList<>(retroGenres);
                        Collections.shuffle(options);

                        q.setQuestionText("Which of these genres is typical for retro games?");
                        q.setOptions(options);
                        q.setCorrectAnswer(options.get(0));
                    }
                    case 7 -> {
                        q.setQuestionText("Was '" + title + "' released in the 1990s?");
                        q.setOptions(List.of("yes", "no"));

                        boolean is90s = releaseDate.getYear() >= 1990 && releaseDate.getYear() <= 1999;
                        q.setCorrectAnswer(is90s ? "yes" : "no");
                    }
                    case 8 -> {
                        int age = LocalDate.now().getYear() - releaseDate.getYear();

                        q.setQuestionText("Approximately how old is '" + title + "'?");
                        q.setOptions(List.of("10+", "15+", "20+", "30+"));

                        if (age >= 30) q.setCorrectAnswer("30+");
                        else if (age >= 20) q.setCorrectAnswer("20+");
                        else if (age >= 15) q.setCorrectAnswer("15+");
                        else q.setCorrectAnswer("10+");
                    }
                    case 9 -> {
                        q.setQuestionText("Was '" + title + "' released before the PlayStation 3 came out in 2006?");
                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(releaseDate.isBefore(LocalDate.of(2006, 11, 11)) ? "yes" : "no");
                    }
                    case 10 -> {
                        String query3 = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date < '2010-01-01'
                            ORDER BY RANDOM()
                            LIMIT 3
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(query3);
                             ResultSet r2 = s2.executeQuery()) {

                            List<String> titles = new ArrayList<>();
                            List<LocalDate> dates = new ArrayList<>();

                            while (r2.next()) {
                                titles.add(r2.getString("title"));
                                dates.add(r2.getDate("release_date").toLocalDate());
                            }

                            if (titles.size() < 3) return null;

                            int oldestIndex = 0;
                            for (int i = 1; i < 3; i++) {
                                if (dates.get(i).isBefore(dates.get(oldestIndex))) {
                                    oldestIndex = i;
                                }
                            }

                            q.setQuestionText("Which of these retro games is the oldest?");
                            q.setOptions(titles);
                            q.setCorrectAnswer(titles.get(oldestIndex));
                        }
                    }
                    case 11 -> {
                        String query3 = """
                            SELECT title, release_date
                            FROM games
                            WHERE release_date < '2010-01-01'
                            ORDER BY RANDOM()
                            LIMIT 3
                        """;

                        try (PreparedStatement s2 = conn.prepareStatement(query3);
                             ResultSet r2 = s2.executeQuery()) {

                            List<String> titles = new ArrayList<>();
                            List<LocalDate> dates = new ArrayList<>();

                            while (r2.next()) {
                                titles.add(r2.getString("title"));
                                dates.add(r2.getDate("release_date").toLocalDate());
                            }

                            if (titles.size() < 3) return null;

                            int oldestIndex = 0;
                            for (int i = 1; i < titles.size(); i++) {
                                if (dates.get(i).isBefore(dates.get(oldestIndex))) {
                                    oldestIndex = i;
                                }
                            }

                            Collections.shuffle(titles);

                            q.setQuestionText("Which of these games should appear first when sorting from oldest to newest?");
                            q.setOptions(titles);
                            q.setCorrectAnswer(titles.contains(titles.get(oldestIndex))
                                    ? titles.get(oldestIndex)
                                    : null);
                        }
                    }


                }
                if (q.getOptions() == null || q.getOptions().size() <= 1) {
                    return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return q;
    }
}
