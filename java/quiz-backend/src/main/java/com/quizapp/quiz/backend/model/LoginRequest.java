package com.quizapp.quiz.backend.model;

/**
 * Data Transfer Object (DTO) for capturing user login credentials.
 * <p>
 * Used in authentication endpoints to receive the login (username/email) and password via JSON.
 *
 * @author machm
 */
public class LoginRequest {

    /** The username or email provided by the user. */
    private String login;

    /** The password provided by the user. */
    private String password;

    /**
     * Gets the login identifier.
     *
     * @return The username or email.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login identifier.
     *
     * @param login The username or email to set.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the password.
     *
     * @return The password string.
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