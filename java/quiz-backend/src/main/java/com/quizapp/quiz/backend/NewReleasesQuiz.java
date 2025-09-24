package com.quizapp.quiz.backend;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class NewReleasesQuiz {

    static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASSWORD = "admin";
    static final List<String> FAKE_GENRES = List.of("Puzzle", "Board", "Card", "Educational");
    static final List<String> FAKE_PLATFORMS = List.of("DreamStation 9", "VirtuaBox", "RetroDeck", "GameSphere");

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            int score = 0;
            boolean keepPlaying = true;
            Random random = new Random();

            while (keepPlaying) {
                String query = """
                    SELECT id, title, release_date, rating, genres, platforms 
                    FROM games 
                    WHERE release_date >= '2020-01-01' 
                    AND array_length(platforms, 1) > 0 
                    ORDER BY RANDOM() LIMIT 1
                """;

                try (PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Brak odpowiednich gier w bazie.");
                        break;
                    }

                    String title = rs.getString("title");
                    java.sql.Date releaseDate = rs.getDate("release_date");
                    float rating = rs.getFloat("rating");
                    List<String> genres = toList(rs.getArray("genres"));
                    List<String> platforms = toList(rs.getArray("platforms"));

                    int questionType = random.nextInt(10);
                    boolean correct = false;

                    System.out.println("Gra: " + title);
                    System.out.print("➡ ");

                    switch (questionType) {
                        case 0 -> correct = questionPlatformNotReleased(scanner, title, platforms);
                        case 1 -> correct = questionReleaseYear(scanner, title, releaseDate);
                        case 2 -> correct = questionGenreNotBelong(scanner, title, genres);
                        case 3 -> correct = questionRatingABCD(scanner, title, rating);
                        case 4 -> correct = questionIsGenre(scanner, title, genres);
                        case 5 -> correct = questionAfter2023(scanner, title, releaseDate);
                        case 6 -> correct = questionInputPlatform(scanner, platforms);
                        case 7 -> correct = questionGameAge(scanner, title, releaseDate);
                        case 8 -> correct = questionGameNotReleased2020(scanner, conn, title);
                        case 9 -> correct = questionFirstLetter(scanner, title);
                    }

                    if (correct) {
                        System.out.println(" Dobrze!\n");
                        score++;
                    } else {
                        System.out.println(" Błąd! Koniec gry.");
                        keepPlaying = false;
                    }
                }
            }

            System.out.println("Twój wynik końcowy: " + score);
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
        Collections.shuffle(options);
        String fake = FAKE_PLATFORMS.get(new Random().nextInt(FAKE_PLATFORMS.size()));
        while (options.contains(fake)) {
            fake = FAKE_PLATFORMS.get(new Random().nextInt(FAKE_PLATFORMS.size()));
        }
        options = options.subList(0, Math.min(3, options.size()));
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
            options.add("Action"); // fallback
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
            float f = Math.round((rating + (rand.nextFloat() * 2 - 1)) * 10) / 10f;
            if (!options.contains(f) && f >= 0 && f <= 10) options.add(f);
        }
        Collections.shuffle(options);
        System.out.println("Jaka była ocena gry?");
        char label = 'A';
        for (float r : options) {
            System.out.println((label++) + ". " + r);
        }
        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && Math.abs(options.get(idx) - rating) < 0.01;
    }

    private static boolean questionIsGenre(Scanner sc, String title, List<String> genres) {
        String g = "Action";
        System.out.print("Czy gra należy do gatunku " + g + "? (tak/nie): ");
        String input = sc.nextLine().trim().toLowerCase();
        return (genres.contains(g) && input.equals("tak")) || (!genres.contains(g) && input.equals("nie"));
    }

    private static boolean questionAfter2023(Scanner sc, String title, java.sql.Date date) {
        System.out.print("Czy gra wyszła po 2022 roku? (tak/nie): ");
        String input = sc.nextLine().trim().toLowerCase();
        boolean isAfter = date.toLocalDate().isAfter(LocalDate.of(2022, 1, 1));
        return (isAfter && input.equals("tak")) || (!isAfter && input.equals("nie"));
    }

    private static boolean questionInputPlatform(Scanner sc, List<String> platforms) {
        System.out.print("Podaj jedną z platform gry: ");
        String input = sc.nextLine().trim();
        return platforms.stream().anyMatch(p -> p.equalsIgnoreCase(input));
    }

    private static boolean questionGameAge(Scanner sc, String title, java.sql.Date date) {
        int years = LocalDate.now().getYear() - date.toLocalDate().getYear();
        List<Integer> opts = new ArrayList<>(List.of(years, years + 1, years - 1, years + 2));
        Collections.shuffle(opts);
        System.out.println("Ile lat ma gra?");
        char label = 'A';
        for (int y : opts) {
            System.out.println((label++) + ". " + y);
        }
        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && opts.get(idx) == years;
    }

    private static boolean questionGameNotReleased2020(Scanner sc, Connection conn, String currentGameTitle) throws SQLException {
        List<String> titles = new ArrayList<>();
        String q = "SELECT title, release_date FROM games WHERE release_date >= '2018-01-01' ORDER BY RANDOM() LIMIT 4";
        try (PreparedStatement stmt = conn.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                titles.add(rs.getString("title") + "|" + rs.getDate("release_date"));
            }
        }

        Collections.shuffle(titles);
        System.out.println("Której z poniższych gier NIE wydano po 2020 roku?");
        char label = 'A';
        for (String entry : titles) {
            String name = entry.split("\\|")[0];
            System.out.println((label++) + ". " + name);
        }

        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        if (idx >= 0 && idx < titles.size()) {
            java.sql.Date d = java.sql.Date.valueOf(titles.get(idx).split("\\|")[1]);
            return d.toLocalDate().getYear() < 2020;
        }
        return false;
    }

    private static boolean questionFirstLetter(Scanner sc, String title) {
        char correct = Character.toUpperCase(title.charAt(0));
        List<Character> options = new ArrayList<>(List.of(correct, 'A', 'B', 'C', 'D', 'E', 'F'));
        options.removeIf(c -> c == correct);
        Collections.shuffle(options);
        options = options.subList(0, 3);
        options.add(correct);
        Collections.shuffle(options);

        System.out.println("Jaka jest pierwsza litera tytułu gry?");
        char label = 'A';
        for (char c : options) {
            System.out.println((label++) + ". " + c);
        }

        System.out.print("Twoja odpowiedź (A-D): ");
        String input = sc.nextLine().trim().toUpperCase();
        int idx = input.charAt(0) - 'A';
        return idx >= 0 && idx < 4 && options.get(idx) == correct;
    }
}
