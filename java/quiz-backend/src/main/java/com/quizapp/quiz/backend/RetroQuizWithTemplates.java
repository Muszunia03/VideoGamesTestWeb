package com.quizapp.quiz.backend;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RetroQuizWithTemplates {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "admin";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String query = """
                SELECT id, title, release_date, rating, genres, platforms 
                FROM games 
                WHERE release_date < '2010-01-01' 
                AND array_length(platforms, 1) > 0 
                ORDER BY RANDOM() 
                LIMIT 5
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                Scanner scanner = new Scanner(System.in);
                int score = 0;
                long startTime = System.currentTimeMillis();

                int lastGameId = -1; // zapamiętamy ID ostatniej gry

                while (rs.next()) {
                    int gameId = rs.getInt("id");
                    String title = rs.getString("title");
                    LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                    float rating = rs.getFloat("rating");
                    Array genresArray = rs.getArray("genres");
                    Array platformsArray = rs.getArray("platforms");

                    List<String> genres = genresArray != null ? Arrays.asList((String[]) genresArray.getArray()) : new ArrayList<>();
                    List<String> platforms = platformsArray != null ? Arrays.asList((String[]) platformsArray.getArray()) : new ArrayList<>();

                    int template = new Random().nextInt(4);
                    boolean correct = false;
                    String answer;

                    switch (template) {
                        case 0:
                            System.out.println("Na jakiej platformie zadebiutowała gra '" + title + "'?");
                            System.out.print("Twoja odpowiedź: ");
                            answer = scanner.nextLine();
                            if (platforms.stream().anyMatch(p -> p.equalsIgnoreCase(answer.trim()))) {
                                correct = true;
                            }
                            break;
                        case 1:
                            System.out.println("Czy gra '" + title + "' została wydana przed 2005 rokiem? (tak/nie)");
                            System.out.print("Twoja odpowiedź: ");
                            answer = scanner.nextLine();
                            correct = (releaseDate.isBefore(LocalDate.of(2005, 1, 1)) && answer.equalsIgnoreCase("tak")) ||
                                      (!releaseDate.isBefore(LocalDate.of(2005, 1, 1)) && answer.equalsIgnoreCase("nie"));
                            break;
                        case 2:
                            System.out.println("Czy gra '" + title + "' miała ocenę powyżej 3.3? (tak/nie)");
                            System.out.print("Twoja odpowiedź: ");
                            answer = scanner.nextLine();
                            correct = (rating > 3.3 && answer.equalsIgnoreCase("tak")) ||
                                      (rating <= 3.3 && answer.equalsIgnoreCase("nie"));
                            break;
                        case 3:
                            System.out.println("Czy gra '" + title + "' należy do gatunku RPG? (tak/nie)");
                            System.out.print("Twoja odpowiedź: ");
                            answer = scanner.nextLine();
                            correct = (genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG")) && answer.equalsIgnoreCase("tak")) ||
                                      (!genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG")) && answer.equalsIgnoreCase("nie"));
                            break;
                    }

                    if (correct) {
                        System.out.println("Dobrze!\n");
                        score++;
                        lastGameId = gameId; // zapamiętaj ostatnią poprawną grę
                    } else {
                        System.out.println("Niepoprawnie. Poprawna odpowiedź: " +
                                generateCorrectAnswer(template, releaseDate, rating, genres, platforms) + "\n");
                        break;
                    }
                }

                long endTime = System.currentTimeMillis();
                int timeTakenSeconds = (int) ((endTime - startTime) / 1000);

                System.out.println("Twój końcowy wynik: " + score);

                // === ZAPIS WYNIKU DO TABELI results ===
                int fakeUserId = 1;
                String insertQuery = """
                    INSERT INTO results (user_id, game_id, score, time_taken_seconds)
                    VALUES (?, ?, ?, ?)
                """;

                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, fakeUserId);

                    if (lastGameId != -1) {
                        insertStmt.setInt(2, lastGameId);
                    } else {
                        insertStmt.setNull(2, Types.INTEGER);
                    }

                    insertStmt.setInt(3, score);
                    insertStmt.setInt(4, timeTakenSeconds);

                    insertStmt.executeUpdate();
                    System.out.println("Wynik został zapisany do bazy danych.");
                }

            }

        } catch (SQLException e) {
            System.out.println("Błąd SQL: " + e.getMessage());
        }
    }

    private static String generateCorrectAnswer(int template, LocalDate releaseDate, float rating, List<String> genres, List<String> platforms) {
        return switch (template) {
            case 0 -> String.join(", ", platforms);
            case 1 -> releaseDate.isBefore(LocalDate.of(2005, 1, 1)) ? "tak" : "nie";
            case 2 -> rating > 3.3 ? "tak" : "nie";
            case 3 -> genres.stream().anyMatch(g -> g.equalsIgnoreCase("RPG")) ? "tak" : "nie";
            default -> "?";
        };
    }
}
