package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RatingEstimatorQuizService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

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

            int template = ThreadLocalRandom.current().nextInt(5); // 0–4
            q.setTemplateType(template);

            switch (template) {

                // TEMPLATE 0 — Czy ocena powyżej progu?
                case 0 -> {
                    float threshold = randomFloat(2.0f, 4.5f);

                    q.setQuestionText(
                            String.format("Czy ocena gry '%s' jest wyższa niż %.1f?", title, threshold)
                    );
                    q.setOptions(List.of("tak", "nie"));
                    q.setCorrectAnswer(rating > threshold ? "tak" : "nie");
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
                            String.format("Czy ocena '%s' jest bliżej %.1f czy %.1f?", title, a, b)
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
                            String.format("Która wartość jest najbliżej prawdziwej oceny gry '%s'?", title)
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
                            String.format("W jakim zakresie znajduje się ocena gry '%s'?", title)
                    );
                    q.setOptions(ranges);
                    q.setCorrectAnswer(correctRange);
                }

                // TEMPLATE 4 — (INPUT) Podaj ocenę zaokrągloną do 1 miejsca
                case 4 -> {
                    float correct = round1(rating);

                    q.setQuestionText(
                            String.format("Podaj dokładną ocenę gry '%s' (zaokrąglona do 1 miejsca):", title)
                    );

                    q.setOptions(Collections.emptyList()); // input mode
                    q.setCorrectAnswer(String.format("%.1f", correct));
                }
            }

            return q;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // helper: losowy float
    private float randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    // helper: zaokrąglenie
    private float round1(float x) {
        return Math.round(x * 10f) / 10f;
    }
}
