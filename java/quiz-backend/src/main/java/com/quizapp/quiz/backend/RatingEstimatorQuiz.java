package com.quizapp.quiz.backend;

import java.sql.*;
import java.util.*;

public class RatingEstimatorQuiz {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "admin";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String query = """
                SELECT id, title, rating 
                FROM games 
                WHERE rating IS NOT NULL 
                ORDER BY RANDOM() 
                LIMIT 5
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                Scanner scanner = new Scanner(System.in);
                int score = 0;
                int lastGameId = -1;
                long startTime = System.currentTimeMillis();

                while (rs.next()) {
                    int gameId = rs.getInt("id");
                    String title = rs.getString("title");
                    float rating = rs.getFloat("rating");

                    System.out.println("Czy gra '" + title + "' ma ocenę powyżej 3.3? (tak/nie)");
                    System.out.print("Twoja odpowiedź: ");
                    String answer = scanner.nextLine();

                    boolean correct = (rating > 3.3 && answer.equalsIgnoreCase("tak")) ||
                                      (rating <= 3.3 && answer.equalsIgnoreCase("nie"));

                    if (correct) {
                        System.out.println("Dobrze!\n");
                        score++;
                        lastGameId = gameId;
                    } else {
                        System.out.println("Niepoprawnie. Ocena: " + rating + "\n");
                        break;
                    }
                }

                long duration = (System.currentTimeMillis() - startTime) / 1000;
                saveResult(conn, 1, lastGameId, score, (int) duration);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveResult(Connection conn, int userId, int gameId, int score, int seconds) throws SQLException {
        String sql = "INSERT INTO results (user_id, game_id, score, time_taken_seconds) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            if (gameId != -1) stmt.setInt(2, gameId); else stmt.setNull(2, Types.INTEGER);
            stmt.setInt(3, score);
            stmt.setInt(4, seconds);
            stmt.executeUpdate();
            System.out.println("Wynik zapisany.");
        }
    }
}
