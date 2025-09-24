package com.quizapp.quiz.backend;

import java.sql.*;
import java.util.*;

public class GenreChallengeQuiz {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "admin";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String query = """
                SELECT id, title, genres 
                FROM games 
                WHERE array_length(genres, 1) > 0 
                ORDER BY RANDOM() 
                LIMIT 5
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                Scanner scanner = new Scanner(System.in);
                int score = 0;
                int lastGameId = -1;
                long startTime = System.currentTimeMillis();

                List<String> allGenres = Arrays.asList(
                        "RPG", "FPS", "Action", "Adventure", "Strategy",
                        "Simulation", "Puzzle", "Platformer", "Sports", "Racing"
                );

                Random random = new Random();

                while (rs.next()) {
                    int gameId = rs.getInt("id");
                    String title = rs.getString("title");
                    Array genresArray = rs.getArray("genres");
                    List<String> genres = Arrays.asList((String[]) genresArray.getArray());

                    int template = random.nextInt(5);
                    boolean correct = false;

                    switch (template) {
                        case 0 -> {
                            // Czy gra należy do konkretnego gatunku?
                            String genreToAsk = allGenres.get(random.nextInt(allGenres.size()));
                            System.out.println("Czy gra '" + title + "' należy do gatunku " + genreToAsk + "? (tak/nie)");
                            String answer = scanner.nextLine();
                            correct = (genres.contains(genreToAsk) && answer.equalsIgnoreCase("tak")) ||
                                      (!genres.contains(genreToAsk) && answer.equalsIgnoreCase("nie"));
                        }
                        case 1 -> {
                            // Wymień jeden z gatunków tej gry
                            System.out.println("Wymień jeden z gatunków gry '" + title + "':");
                            String answer = scanner.nextLine().trim();
                            correct = genres.stream().anyMatch(g -> g.equalsIgnoreCase(answer));
                        }
                        case 2 -> {
                            // Ile gatunków ma ta gra?
                            int actualCount = genres.size();
                            System.out.println("Ile gatunków posiada gra '" + title + "'?");
                            String answer = scanner.nextLine().trim();
                            try {
                                int guess = Integer.parseInt(answer);
                                correct = (guess == actualCount);
                            } catch (NumberFormatException e) {
                                correct = false;
                            }
                        }
                        case 3 -> {
                            // Czy gra należy do dwóch gatunków A i B?
                            List<String> shuffled = new ArrayList<>(allGenres);
                            Collections.shuffle(shuffled);
                            String g1 = shuffled.get(0);
                            String g2 = shuffled.get(1);
                            System.out.println("Czy gra '" + title + "' należy do gatunków " + g1 + " i " + g2 + "? (tak/nie)");
                            String answer = scanner.nextLine().trim();
                            boolean hasBoth = genres.contains(g1) && genres.contains(g2);
                            correct = (hasBoth && answer.equalsIgnoreCase("tak")) ||
                                      (!hasBoth && answer.equalsIgnoreCase("nie"));
                        }
                        case 4 -> {
                            // Czy pierwszy gatunek to "najważniejszy"?
                            String mainGenre = genres.get(0);
                            System.out.println("Czy głównym gatunkiem gry '" + title + "' jest " + mainGenre + "? (tak/nie)");
                            String answer = scanner.nextLine().trim();
                            correct = answer.equalsIgnoreCase("tak"); // Zawsze tak, bo to pierwszy
                        }
                    }

                    if (correct) {
                        System.out.println("Dobrze!\n");
                        score++;
                        lastGameId = gameId;
                    } else {
                        System.out.println("Niepoprawnie. Gatunki tej gry: " + String.join(", ", genres) + "\n");
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
