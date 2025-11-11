package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Game;
import java.sql.*;
import java.util.*;

public class ImageQuizService {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    public Game getRandomGame() {
        String query = """
            SELECT id, title, background_image 
            FROM games 
            WHERE background_image IS NOT NULL AND background_image != ''
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new Game(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("background_image")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Game> getAllGamesList() {
        List<Game> games = new ArrayList<>();
        String query = """
            SELECT id, title, background_image 
            FROM games 
            WHERE background_image IS NOT NULL AND background_image != ''
            ORDER BY title ASC
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("background_image")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public List<String> searchGamesByTitle(String query) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT title FROM games WHERE LOWER(title) LIKE LOWER(?) ORDER BY title LIMIT 20";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}