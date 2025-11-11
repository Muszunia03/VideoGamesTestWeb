package com.quizapp.quiz.backend.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class AuthService {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    public boolean login(String login, String plainPassword) {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "SELECT password_hash FROM users WHERE email = ? OR username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, login);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password_hash");
                        return BCrypt.checkpw(plainPassword, storedHash);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Błąd SQL przy logowaniu: " + e.getMessage());
        }
        return false;
    }

    public String register(String username, String email, String plainPassword) {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();
                return "success";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("users_username_key")) {
                return "username_taken";
            } else if (e.getMessage().contains("users_email_key")) {
                return "email_taken";
            } else {
                System.out.println("Błąd SQL przy rejestracji: " + e.getMessage());
                return "error";
            }
        }
    }
}
