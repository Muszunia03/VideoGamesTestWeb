package com.quizapp.quiz.backend;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;

public class SpeedrunQuiz {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres"; // zmień jeśli inna nazwa bazy
    private static final String DB_USER = "postgres"; // użytkownik
    private static final String DB_PASS = "admin";    // hasło

    public static List<Map<String, Object>> generateSpeedrunQuiz(int count) {

        List<Map<String, Object>> questions = new ArrayList<>();
        Random rand = new Random();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            for (int i = 0; i < count; i++) {
                int type = i % 9;
                Map<String, Object> question;

                switch (type) {
                    case 0:
                        question = buildRatingCompareQuestion(conn);
                        break;
                    case 1:
                        question = buildReleaseYearYesNo(conn);
                        break;
                    case 2:
                        question = buildPlatformChoice(conn);
                        break;
                    case 3:
                        question = buildImageGuess(conn);
                        break;
                    case 4:
                        question = buildGenreSingle(conn);
                        break;
                    case 5:
                        question = buildOlderNewerCompare(conn);
                        break;
                    case 6:
                        question = buildMultiGenre(conn);
                        break;
                    case 7:
                        question = buildOrderingByDate(conn);
                        break;
                    case 8:
                        question = buildFakeOneOut(conn);
                        break;
                    default:
                        continue;
                }

                if (question != null) questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    private static Map<String, Object> buildRatingCompareQuestion(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, rating FROM games WHERE rating IS NOT NULL ORDER BY RANDOM() LIMIT 2");
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> options = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("title", rs.getString("title"));
            g.put("rating", rs.getFloat("rating"));
            options.add(g);
        }

        if (options.size() < 2) return null;

        Map<String, Object> q = new HashMap<>();
        q.put("type", "rating_compare");
        q.put("question", "Która z tych gier ma wyższą ocenę?");
        q.put("options", Arrays.asList(options.get(0).get("title"), options.get(1).get("title")));
        q.put("correct", ((float) options.get(0).get("rating") > (float) options.get(1).get("rating")) ?
                options.get(0).get("title") : options.get(1).get("title"));
        return q;
    }

    private static Map<String, Object> buildReleaseYearYesNo(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        String title = rs.getString("title");
        int year = rs.getDate("release_date").toLocalDate().getYear();
        boolean after2015 = year > 2015;

        Map<String, Object> q = new HashMap<>();
        q.put("type", "yes_no");
        q.put("question", "Czy gra \"" + title + "\" została wydana po 2015 roku?");
        q.put("correct", after2015 ? "Tak" : "Nie");
        return q;
    }

    private static Map<String, Object> buildPlatformChoice(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, platforms FROM games WHERE platforms IS NOT NULL ORDER BY RANDOM() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        String[] platforms = (String[]) rs.getArray("platforms").getArray();
        String correct = platforms[new Random().nextInt(platforms.length)];

        Map<String, Object> q = new HashMap<>();
        q.put("type", "platform_choice");
        q.put("question", "Na której platformie dostępna jest gra \"" + rs.getString("title") + "\"?");
        q.put("options", Arrays.asList(platforms));
        q.put("correct", correct);
        return q;
    }

    private static Map<String, Object> buildImageGuess(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, background_image FROM games WHERE background_image IS NOT NULL ORDER BY RANDOM() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        Map<String, Object> q = new HashMap<>();
        q.put("type", "image_guess");
        q.put("question", "Jak nazywa się gra przedstawiona na obrazku?");
        q.put("image", rs.getString("background_image")); // poprawione
        q.put("correct", rs.getString("title"));
        return q;
    }

    private static Map<String, Object> buildGenreSingle(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, genres FROM games WHERE genres IS NOT NULL ORDER BY RANDOM() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        String[] genres = (String[]) rs.getArray("genres").getArray();

        Map<String, Object> q = new HashMap<>();
        q.put("type", "genre_single");
        q.put("question", "Jaki gatunek ma gra \"" + rs.getString("title") + "\"?");
        q.put("correct", genres[0]); // pierwszy z listy
        return q;
    }

    private static Map<String, Object> buildOlderNewerCompare(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 2");
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> options = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("title", rs.getString("title"));
            g.put("date", rs.getDate("release_date").toLocalDate());
            options.add(g);
        }

        if (options.size() < 2) return null;

        Map<String, Object> q = new HashMap<>();
        q.put("type", "date_compare");
        q.put("question", "Która gra została wydana wcześniej?");
        q.put("options", Arrays.asList(options.get(0).get("title"), options.get(1).get("title")));
        q.put("correct", ((LocalDate) options.get(0).get("date")).isBefore((LocalDate) options.get(1).get("date")) ?
                options.get(0).get("title") : options.get(1).get("title"));
        return q;
    }

    private static Map<String, Object> buildMultiGenre(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, genres FROM games WHERE genres IS NOT NULL ORDER BY RANDOM() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        String[] genres = (String[]) rs.getArray("genres").getArray();

        Map<String, Object> q = new HashMap<>();
        q.put("type", "multi_genre");
        q.put("question", "Jakie gatunki ma gra \"" + rs.getString("title") + "\"?");
        q.put("correct", Arrays.asList(genres));
        return q;
    }

    private static Map<String, Object> buildOrderingByDate(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 3");
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> titles = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("title", rs.getString("title"));
            g.put("release_date", rs.getDate("release_date").toLocalDate());
            titles.add(g);
        }

        titles.sort(Comparator.comparing(a -> (LocalDate) a.get("release_date")));

        Map<String, Object> q = new HashMap<>();
        q.put("type", "order_date");
        q.put("question", "Uporządkuj gry od najstarszej do najnowszej.");
        q.put("options", titles.stream().map(t -> t.get("title")).toList());
        q.put("correct_order", titles.stream().map(t -> t.get("title")).toList());
        return q;
    }

    private static Map<String, Object> buildFakeOneOut(Connection conn) throws SQLException {
        List<String> real = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT title FROM games ORDER BY RANDOM() LIMIT 3");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            real.add(rs.getString("title"));
        }

        String fake = "Final Fantasy 21: Online Cats"; // hardkodowany fake
        List<String> options = new ArrayList<>(real);
        options.add(fake);
        Collections.shuffle(options);

        Map<String, Object> q = new HashMap<>();
        q.put("type", "fake_one");
        q.put("question", "Która z tych gier NIE istnieje?");
        q.put("options", options);
        q.put("correct", fake);
        return q;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> quiz = generateSpeedrunQuiz(10);
        for (Map<String, Object> q : quiz) {
            System.out.println(q);
        }
    }
}
