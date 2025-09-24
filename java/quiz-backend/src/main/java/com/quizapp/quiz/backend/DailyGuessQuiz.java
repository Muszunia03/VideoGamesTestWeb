package com.quizapp.quiz.backend;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DailyGuessQuiz {

    static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASSWORD = "admin";
    static final List<String> FAKE_GENRES = List.of("Puzzle", "Board", "Card", "Educational");
    static final List<String> FAKE_PLATFORMS = List.of("DreamStation 9", "VirtuaBox", "RetroDeck", "GameSphere");

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            int score = 0;
            Random random = new Random();

            // Pobierz dzienną grę na dziś
            String query = """
                    SELECT g.id, g.title, g.release_date, g.rating, g.genres, g.platforms 
                    FROM daily_games d
                    JOIN games g ON d.game_id = g.id
                    WHERE d.date = ?
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Brak gry dziennej na dziś.");
                        return;
                    }

                    String title = rs.getString("title");
                    java.sql.Date releaseDate = rs.getDate("release_date");
                    float rating = rs.getFloat("rating");
                    List<String> genres = toList(rs.getArray("genres"));
                    List<String> platforms = toList(rs.getArray("platforms"));

                    System.out.println("🎮 Daily Guess - Gra dnia: " + title);
                    boolean keepPlaying = true;

                    while (keepPlaying) {
                        int questionType = random.nextInt(5); // 5 typów pytań
                        boolean correct = false;

                        switch (questionType) {
                            case 0 -> correct = questionPlatformNotReleased(scanner, title, platforms);
                            case 1 -> correct = questionReleaseYear(scanner, title, releaseDate);
                            case 2 -> correct = questionGenreNotBelong(scanner, title, genres);
                            case 3 -> correct = questionRatingABCD(scanner, title, rating);
                            case 4 -> correct = questionFirstLetter(scanner, title);
                        }

                        if (correct) {
                            System.out.println("✅ Dobrze!\n");
                            score++;
                        } else {
                            System.out.println("❌ Błąd! Koniec gry.");
                            keepPlaying = false;
                        }
                    }

                    System.out.println("Twój wynik końcowy: " + score);
                }
            }

        } catch (SQLException e) {
            System.err.println("Błąd SQL: " + e.getMessage());
        }
    }

    private static List<String> toList(Array array) throws SQLException {
        if (array == null) return List.of();
        return Arrays.asList((String[]) array.getArray());
    }

    private static boolean questionPlatformNotReleased(Scanner sc, String title, List<String> platforms) {
        List<String> options = new ArrayList<>(platforms);
        Collections.shuffle(options);
        while (options.size() < 3 && !FAKE_PLATFORMS.isEmpty()) {
            options.add(FAKE_PLATFORMS.get(new Random().nextInt(FAKE_PLATFORMS.size())));
        }
        options = options.subList(0, Math.min(3, options.size()));
        String fake = FAKE_PLATFORMS.get(new Random().nextInt(FAKE_PLATFORMS.size()));
        while (options.contains(fake)) {
            fake = FAKE_PLATFORMS.get(new Random().nextInt(FAKE_PLATFORMS.size()));
        }
        options.add(fake);
        Collections.shuffle(options);

        System.out.println("Na której platformie NIE została wydana gra?");
        char label = 'A';
        for (String opt : options) {
            System.out.println((label++) + ". " + opt);
        }

        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < options.size() && !platforms.contains(options.get(idx));
    }

    private static boolean questionReleaseYear(Scanner sc, String title, java.sql.Date date) {
        int correctYear = date.toLocalDate().getYear();
        List<Integer> years = new ArrayList<>(List.of(correctYear - 1, correctYear, correctYear + 1, correctYear + 2));
        Collections.shuffle(years);
        System.out.println("W którym roku wydano grę?");
        char label = 'A';
        for (int year : years) {
            System.out.println((label++) + ". " + year);
        }
        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && years.get(idx) == correctYear;
    }

    private static boolean questionGenreNotBelong(Scanner sc, String title, List<String> genres) {
        List<String> options = new ArrayList<>(genres);
        Collections.shuffle(options);
        while (options.size() < 3 && !FAKE_GENRES.isEmpty()) {
            options.add(FAKE_GENRES.get(new Random().nextInt(FAKE_GENRES.size())));
        }
        options = options.subList(0, Math.min(3, options.size()));
        String fake = FAKE_GENRES.get(new Random().nextInt(FAKE_GENRES.size()));
        while (genres.contains(fake)) {
            fake = FAKE_GENRES.get(new Random().nextInt(FAKE_GENRES.size()));
        }
        options.add(fake);
        Collections.shuffle(options);

        System.out.println("Który gatunek NIE pasuje do gry?");
        char label = 'A';
        for (String opt : options) {
            System.out.println((label++) + ". " + opt);
        }
        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && !genres.contains(options.get(idx));
    }

    private static boolean questionRatingABCD(Scanner sc, String title, float rating) {
        List<Float> options = new ArrayList<>();
        options.add(rating);
        Random rand = new Random();
        while (options.size() < 4) {
            float fakeRating = Math.round((rand.nextFloat() * 10) * 10f) / 10f; // losowa ocena 0.0 - 10.0
            if (!options.contains(fakeRating)) options.add(fakeRating);
        }
        Collections.shuffle(options);

        System.out.println("Jaka jest ocena gry?");
        char label = 'A';
        for (Float opt : options) {
            System.out.println((label++) + ". " + opt);
        }

        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && options.get(idx) == rating;
    }

    private static boolean questionFirstLetter(Scanner sc, String title) {
        char correct = Character.toUpperCase(title.charAt(0));
        System.out.print("Na jaką literę zaczyna się tytuł gry? ");
        String input = sc.nextLine().trim().toUpperCase();
        return input.length() == 1 && input.charAt(0) == correct;
    }
}
