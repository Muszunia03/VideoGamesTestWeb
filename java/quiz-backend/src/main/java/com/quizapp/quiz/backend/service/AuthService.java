package com.quizapp.quiz.backend.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.sql.*;

/**
 * Service class responsible for handling user authentication and registration logic.
 * <p>
 * Manages database interactions related to user credential verification and new user creation,
 * including password hashing using BCrypt.
 *
 * @author machm
 */
@Service
public class AuthService {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";


    /**
     * Attempts to log in a user by checking the provided credentials against the stored hash.
     *
     * @param login The user's login identifier (username or email).
     * @param plainPassword The raw password provided by the user.
     * @return true if the login is successful and the password matches the stored hash, false otherwise.
     */
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

    /**
     * Registers a new user account after hashing the password.
     * Handles potential SQL exceptions related to unique constraints (username and email).
     *
     * @param username The desired username.
     * @param email The user's email address.
     * @param plainPassword The raw password to be hashed and stored.
     * @return A status string: "success", "username_taken", "email_taken", or "error".
     */

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
