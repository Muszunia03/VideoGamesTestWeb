package com.quizapp.quiz.backend.model;

/**
 * Data Transfer Object (DTO) for user registration.
 * <p>
 * Captures the necessary information (username, email, password) required to create a new account.
 *
 * @author machm
 */
public class RegisterRequest {

    /** The desired username. */
    private String username;

    /** The user's email address. */
    private String email;

    /** The raw password provided by the user (to be hashed). */
    private String password;

    /**
     * Gets the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address.
     *
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}