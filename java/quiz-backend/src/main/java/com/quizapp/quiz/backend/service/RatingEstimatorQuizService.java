package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service class dedicated to generating questions for the "Rating Estimator" quiz.
 * <p>
 * This quiz type focuses on challenging users to guess or estimate the critic rating (e.g., Metacritic score) of a game.
 * Uses various question templates (0-4) for variety.
 *
 * @author machm
 */
@Service
public class RatingEstimatorQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    /**
     * Retrieves one random game that has a rating defined and generates a question based on a random template (0-4).
     *
     * @return A fully populated {@link Question} object, or null if no suitable game is found.
     */
    public Question getSingleRatingQuestion() {

        String query = """
            SELECT id, title, rating
            FROM games
            WHERE rating IS NOT NULL
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) return null;

            int id = rs.getInt("id");
            String title = rs.getString("title");
            float rating = rs.getFloat("rating");

            Question q = new Question();
            q.setId(id);
            q.setGameId(id);
            q.setTitle(title);

            int template = ThreadLocalRandom.current().nextInt(9); // 0–8
            q.setTemplateType(template);

            switch (template) {

                // TEMPLATE 0 — Czy ocena powyżej progu?
                case 0 -> {
                    float threshold = randomFloat(2.0f, 4.5f);

                    q.setQuestionText(
                            String.format("Is the rating of the game ‘%s’ higher than %.1f", title, threshold)
                    );
                    q.setOptions(List.of("yes", "no"));
                    q.setCorrectAnswer(rating > threshold ? "yes" : "no");
                }

                // TEMPLATE 1 — Czy ocena bliżej A czy B?
                case 1 -> {
                    float a = randomFloat(1.0f, 4.0f);
                    float b = randomFloat(1.0f, 4.0f);

                    while (Math.abs(a - b) < 0.5f) {
                        b = randomFloat(1.0f, 4.0f);
                    }

                    float distA = Math.abs(rating - a);
                    float distB = Math.abs(rating - b);

                    q.setQuestionText(
                            String.format("Is the rating ‘%s’ closer to %.1f or %.1f?", title, a, b)
                    );
                    q.setOptions(List.of(String.format("%.1f", a), String.format("%.1f", b)));
                    q.setCorrectAnswer(distA < distB ? String.format("%.1f", a) : String.format("%.1f", b));
                }

                // TEMPLATE 2 — Która opcja jest najbliżej prawdziwej oceny?
                case 2 -> {
                    List<String> options = new ArrayList<>();

                    float correct = round1(rating);
                    options.add(String.format("%.1f", correct));

                    for (int i = 0; i < 3; i++) {
                        float wrong = round1(correct + randomFloat(-1.0f, 1.0f));
                        options.add(String.format("%.1f", wrong));
                    }

                    Collections.shuffle(options);

                    q.setQuestionText(
                            String.format("Which value is closest to the true rating of the game ‘%s’?", title)
                    );
                    q.setOptions(options);
                    q.setCorrectAnswer(String.format("%.1f", correct));
                }

                // TEMPLATE 3 — Wybierz poprawny zakres ocen
                case 3 -> {
                    List<String> ranges = List.of(
                            "0–2", "2–3", "3–4", "4–5"
                    );

                    String correctRange;
                    if (rating < 2) correctRange = "0–2";
                    else if (rating < 3) correctRange = "2–3";
                    else if (rating < 4) correctRange = "3–4";
                    else correctRange = "4–5";

                    q.setQuestionText(
                            String.format("What is the rating of the game ‘%s’?", title)
                    );
                    q.setOptions(ranges);
                    q.setCorrectAnswer(correctRange);
                }

                // TEMPLATE 4 — (INPUT) Podaj ocenę zaokrągloną do 1 miejsca
                case 4 -> {
                    float correct = round1(rating);

                    q.setQuestionText(
                            String.format("Give an exact rating for the game ‘%s’ (rounded to 1 decimal place):", title)
                    );

                    q.setOptions(Collections.emptyList()); // input mode
                    q.setCorrectAnswer(String.format("%.1f", correct));
                }

                // TEMPLATE 5 — Czy gra X ma wyższą ocenę niż Y?
                case 5 -> {
                    String dualQuery = """
                        SELECT id, title, rating
                        FROM games
                        WHERE rating IS NOT NULL
                        ORDER BY RANDOM()
                        LIMIT 2
                    """;

                    try (PreparedStatement stmt2 = conn.prepareStatement(dualQuery);
                         ResultSet rs2 = stmt2.executeQuery()) {

                        List<Map<String, Object>> games = new ArrayList<>();

                        while (rs2.next()) {
                            Map<String, Object> g = new HashMap<>();
                            g.put("id", rs2.getInt("id"));
                            g.put("title", rs2.getString("title"));
                            g.put("rating", rs2.getFloat("rating"));
                            games.add(g);
                        }

                        if (games.size() < 2) {
                            return null;
                        }

                        Map<String, Object> g1 = games.get(0);
                        Map<String, Object> g2 = games.get(1);

                        String title1 = (String) g1.get("title");
                        String title2 = (String) g2.get("title");

                        float r1 = (float) g1.get("rating");
                        float r2 = (float) g2.get("rating");

                        String correct = (r1 > r2) ? "yes" : "no";

                        q.setQuestionText(String.format(
                                "Does the game ‘%s’ have a higher rating than ‘%s’?",
                                title1, title2
                        ));

                        q.setOptions(List.of("yes", "no"));
                        q.setCorrectAnswer(correct);
                    }
                }

                // TEMPLATE 6 — Która gra ma niższą ocenę?
                case 6 -> {
                    String dualQuery = """
                        SELECT id, title, rating
                        FROM games
                        WHERE rating IS NOT NULL
                        ORDER BY RANDOM()
                        LIMIT 2
                    """;

                    try (PreparedStatement stmt2 = conn.prepareStatement(dualQuery);
                         ResultSet rs2 = stmt2.executeQuery()) {

                        List<Map<String, Object>> games = new ArrayList<>();

                        while (rs2.next()) {
                            Map<String, Object> g = new HashMap<>();
                            g.put("title", rs2.getString("title"));
                            g.put("rating", rs2.getFloat("rating"));
                            games.add(g);
                        }

                        if (games.size() < 2) return null;

                        var g1 = games.get(0);
                        var g2 = games.get(1);

                        String title1 = (String) g1.get("title");
                        String title2 = (String) g2.get("title");
                        float r1 = (float) g1.get("rating");
                        float r2 = (float) g2.get("rating");

                        String correct = r1 < r2 ? title1 : title2;

                        q.setQuestionText(String.format(
                                "Which game has a lower rating?\n" +
                                        "A: %s\n" +
                                        "B: %s",
                                title1, title2
                        ));

                        q.setOptions(List.of(title1, title2));
                        q.setCorrectAnswer(correct);
                    }
                }

                // TEMPLATE 7 — Która z trzech gier ma najwyższą ocenę?
                case 7 -> {
                    String tripleQuery = """
                        SELECT title, rating
                        FROM games
                        WHERE rating IS NOT NULL
                        ORDER BY RANDOM()
                        LIMIT 3
                    """;

                    try (PreparedStatement stmt3 = conn.prepareStatement(tripleQuery);
                         ResultSet rs3 = stmt3.executeQuery()) {

                        List<Map<String, Object>> games = new ArrayList<>();

                        while (rs3.next()) {
                            Map<String, Object> g = new HashMap<>();
                            g.put("title", rs3.getString("title"));
                            g.put("rating", rs3.getFloat("rating"));
                            games.add(g);
                        }

                        if (games.size() < 3) return null;

                        Map<String, Object> best = Collections.max(
                                games,
                                Comparator.comparing(g -> (Float) g.get("rating"))
                        );

                        String correct = (String) best.get("title");

                        List<String> options = games.stream()
                                .map(g -> (String) g.get("title"))
                                .toList();

                        q.setQuestionText(
                                "Which of these three games has the highest rating?"
                        );

                        q.setOptions(options);
                        q.setCorrectAnswer(correct);
                    }
                }

                // TEMPLATE 8 — Ile punktów różnicy ma gra A i B? (INPUT)
                case 8 -> {
                    String dualQuery = """
                        SELECT title, rating
                        FROM games
                        WHERE rating IS NOT NULL
                        ORDER BY RANDOM()
                        LIMIT 2
                    """;

                    try (PreparedStatement stmt2 = conn.prepareStatement(dualQuery);
                         ResultSet rs2 = stmt2.executeQuery()) {

                        List<Map<String, Object>> games = new ArrayList<>();

                        while (rs2.next()) {
                            Map<String, Object> g = new HashMap<>();
                            g.put("title", rs2.getString("title"));
                            g.put("rating", rs2.getFloat("rating"));
                            games.add(g);
                        }

                        if (games.size() < 2) return null;

                        var g1 = games.get(0);
                        var g2 = games.get(1);

                        String title1 = (String) g1.get("title");
                        String title2 = (String) g2.get("title");
                        float r1 = (float) g1.get("rating");
                        float r2 = (float) g2.get("rating");

                        float diff = round1(Math.abs(r1 - r2));

                        q.setQuestionText(String.format(
                                "What is the difference in points between the ratings of ‘%s’ and ‘%s’? (round to 1 decimal place)",
                                title1, title2
                        ));

                        q.setOptions(Collections.emptyList());
                        q.setCorrectAnswer(String.format("%.1f", diff));
                    }
                }


            }

            if (q.getOptions() == null || q.getOptions().size() <= 1) {
                return null;
            }

            return q;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Helper method to generate a random float within a specified range [min, max).
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (exclusive).
     * @return A random float.
     */
    private float randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    /**
     * Helper method to round a float value to one decimal place.
     *
     * @param x The float value to round.
     * @return The float rounded to one decimal place.
     */
    private float round1(float x) {
        return Math.round(x * 10f) / 10f;
    }
}
