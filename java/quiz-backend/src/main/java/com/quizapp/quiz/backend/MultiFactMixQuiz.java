package com.quizapp.quiz.backend;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class MultiFactMixQuiz {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "admin";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String query = """
                SELECT id, title, release_date, rating, genres, platforms 
                FROM games 
                WHERE release_date IS NOT NULL AND rating IS NOT NULL 
                      AND array_length(platforms, 1) > 0 
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
                    LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                    float rating = rs.getFloat("rating");
                    Array genresArray = rs.getArray("genres");
                    Array platformsArray = rs.getArray("platforms");

                    List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : new ArrayList<>();
                    List<String> platforms = platformsArray != null ? Arrays.asList((String[]) platformsArray.getArray()) : new ArrayList<>();

                    int template = new Random().nextInt(3);
                    boolean correct = false;
                    String answer;

                    switch (template) {
                        case 0:
                            System.out.println("Czy gra '" + title + "' została wydana przed 2005 rokiem? (tak/nie)");
                            answer = scanner.nextLine();
                            correct = (releaseDate.isBefore(LocalDate.of(2005, 1, 1)) && answer.equalsIgnoreCase("tak")) ||
                                      (!releaseDate.isBefore(LocalDate.of(2005, 1, 1)) && answer.equalsIgnoreCase("nie"));
                            break;
                        case 1:
                            System.out.println("Czy gra '" + title + "' ma ocenę powyżej 3.3? (tak/nie)");
                            answer = scanner.nextLine();
                            correct = (rating > 3.3 && answer.equalsIgnoreCase("tak")) ||
                                      (rating <= 3.3 && answer.equalsIgnoreCase("nie"));
                            break;
                        case 2:
                            System.out.println("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                            System.out.print("Twoja odpowiedź: ");
                            answer = scanner.nextLine();
                            correct = platforms.stream().anyMatch(p -> p.equalsIgnoreCase(answer.trim()));
                            break;
                    }

                    if (correct) {
                        System.out.println("Dobrze!\n");
                        score++;
                        lastGameId = gameId;
                    } else {
                        System.out.println("Niepoprawnie. Spróbuj następnym razem.\n");
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
