package com.quizapp.quiz.backend.model;

/**
 * Represents the response sent to the client after an authentication attempt.
 * <p>
 * Contains the status of the operation (e.g., "success" or "error") and a descriptive message.
 *
 * @author machm
 */
public class AuthResponse {

    /**
     * The outcome of the authentication request (e.g., "success", "error").
     */
    private String status;

    /**
     * A readable message providing details about the result.
     */
    private String message;

    /**
     * Constructs a new AuthResponse with the specified status and message.
     *
     * @param status  The status code or string indicating success/failure.
     * @param message The descriptive message for the user.
     */
    public AuthResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the status of the response.
     *
     * @return The status string.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the descriptive message.
     *
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }
}