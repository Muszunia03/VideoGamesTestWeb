package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class SpeedrunQuizService {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "admin";

    public List<Question> generateSpeedrunQuiz(int count) {
        List<Question> questions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            for (int i = 0; i < count; i++) {
                int type = i % 9;
                Question q = null;

                switch (type) {
                    case 0 -> q = buildRatingCompareQuestion(conn);
                    case 1 -> q = buildReleaseYearYesNo(conn);
                    case 2 -> q = buildPlatformChoice(conn);
                    case 3 -> q = buildImageGuess(conn);
                    case 4 -> q = buildGenreSingle(conn);
                    case 5 -> q = buildOlderNewerCompare(conn);
                    case 6 -> q = buildMultiGenre(conn);
                    case 7 -> q = buildOrderingByDate(conn);
                    case 8 -> q = buildFakeOneOut(conn);
                }

                if (q != null) questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    private Question buildRatingCompareQuestion(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, rating FROM games WHERE rating IS NOT NULL ORDER BY RANDOM() LIMIT 2"
        );
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> games = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("id", rs.getInt("id"));
            g.put("title", rs.getString("title"));
            g.put("rating", rs.getFloat("rating"));
            games.add(g);
        }
        if (games.size() < 2) return null;

        Question q = new Question();
        q.setTemplateType(0); // rating compare
        q.setQuestionText("Która z tych gier ma wyższą ocenę?");
        q.setOptions(List.of((String) games.get(0).get("title"), (String) games.get(1).get("title")));
        q.setCorrectAnswer((float) games.get(0).get("rating") > (float) games.get(1).get("rating") ?
                (String) games.get(0).get("title") : (String) games.get(1).get("title"));
        q.setGameId((int) games.get(0).get("id")); // ustawiam pierwszy jako główny gameId
        return q;
    }

    private Question buildReleaseYearYesNo(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        int gameId = rs.getInt("id");
        String title = rs.getString("title");
        int year = rs.getDate("release_date").toLocalDate().getYear();

        Question q = new Question();
        q.setTemplateType(1); // yes/no
        q.setQuestionText("Czy gra \"" + title + "\" została wydana po 2015 roku?");
        q.setCorrectAnswer(year > 2015 ? "Tak" : "Nie");
        q.setGameId(gameId);
        return q;
    }

    private Question buildPlatformChoice(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, platforms FROM games WHERE platforms IS NOT NULL ORDER BY RANDOM() LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        int gameId = rs.getInt("id");
        String title = rs.getString("title");
        String[] platforms = (String[]) rs.getArray("platforms").getArray();
        String correct = platforms[new Random().nextInt(platforms.length)];

        Question q = new Question();
        q.setTemplateType(2); // platform choice
        q.setQuestionText("Na której platformie dostępna jest gra \"" + title + "\"?");
        q.setOptions(Arrays.asList(platforms));
        q.setCorrectAnswer(correct);
        q.setGameId(gameId);
        return q;
    }

    private Question buildImageGuess(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, background_image FROM games WHERE background_image IS NOT NULL ORDER BY RANDOM() LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        int gameId = rs.getInt("id");
        String title = rs.getString("title");
        String image = rs.getString("background_image");

        Question q = new Question();
        q.setTemplateType(3); // image guess
        q.setQuestionText("Jak nazywa się gra przedstawiona na obrazku?");
        q.setCorrectAnswer(title);
        q.setGameId(gameId);
        return q;
    }

    private Question buildGenreSingle(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, genres FROM games WHERE genres IS NOT NULL ORDER BY RANDOM() LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        int gameId = rs.getInt("id");
        String title = rs.getString("title");
        String[] genres = (String[]) rs.getArray("genres").getArray();

        Question q = new Question();
        q.setTemplateType(4); // genre single
        q.setQuestionText("Jaki gatunek ma gra \"" + title + "\"?");
        q.setCorrectAnswer(genres[0]);
        q.setGameId(gameId);
        return q;
    }

    private Question buildOlderNewerCompare(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 2"
        );
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> games = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("id", rs.getInt("id"));
            g.put("title", rs.getString("title"));
            g.put("date", rs.getDate("release_date").toLocalDate());
            games.add(g);
        }
        if (games.size() < 2) return null;

        Question q = new Question();
        q.setTemplateType(5); // older/newer compare
        q.setQuestionText("Która gra została wydana wcześniej?");
        q.setOptions(List.of((String) games.get(0).get("title"), (String) games.get(1).get("title")));
        q.setCorrectAnswer(((LocalDate) games.get(0).get("date")).isBefore((LocalDate) games.get(1).get("date")) ?
                (String) games.get(0).get("title") : (String) games.get(1).get("title"));
        q.setGameId((int) games.get(0).get("id"));
        return q;
    }

    private Question buildMultiGenre(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, genres FROM games WHERE genres IS NOT NULL ORDER BY RANDOM() LIMIT 1"
        );
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) return null;

        int gameId = rs.getInt("id");
        String title = rs.getString("title");
        String[] genres = (String[]) rs.getArray("genres").getArray();

        Question q = new Question();
        q.setTemplateType(6); // multi-genre
        q.setQuestionText("Jakie gatunki ma gra \"" + title + "\"?");
        q.setCorrectAnswer(String.join(", ", genres));
        q.setGameId(gameId);
        return q;
    }

    private Question buildOrderingByDate(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title, release_date FROM games WHERE release_date IS NOT NULL ORDER BY RANDOM() LIMIT 3"
        );
        ResultSet rs = stmt.executeQuery();

        List<Map<String, Object>> games = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> g = new HashMap<>();
            g.put("id", rs.getInt("id"));
            g.put("title", rs.getString("title"));
            g.put("date", rs.getDate("release_date").toLocalDate());
            games.add(g);
        }

        games.sort(Comparator.comparing(a -> (LocalDate) a.get("date")));

        Question q = new Question();
        q.setTemplateType(7); // order by date
        q.setQuestionText("Uporządkuj gry od najstarszej do najnowszej.");
        List<String> orderedTitles = games.stream().map(t -> (String) t.get("title")).toList();
        q.setOptions(orderedTitles);
        q.setCorrectAnswer(String.join(", ", orderedTitles)); // jako ciąg do porównania
        q.setGameId((int) games.get(0).get("id"));
        return q;
    }

    private Question buildFakeOneOut(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title FROM games ORDER BY RANDOM() LIMIT 3"
        );
        ResultSet rs = stmt.executeQuery();

        List<String> real = new ArrayList<>();
        while (rs.next()) {
            real.add(rs.getString("title"));
        }

        String fake = "Final Fantasy 21: Online Cats"; // hardkodowany fake
        List<String> options = new ArrayList<>(real);
        options.add(fake);
        Collections.shuffle(options);

        Question q = new Question();
        q.setTemplateType(8); // fake one out
        q.setQuestionText("Która z tych gier NIE istnieje?");
        q.setOptions(options);
        q.setCorrectAnswer(fake);
        q.setGameId(0); // brak gameId
        return q;
    }
}
