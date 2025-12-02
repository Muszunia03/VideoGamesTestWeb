package com.quizapp.quiz.backend.model;

import java.util.List;

/**
 * Represents a single quiz question presented to the user.
 * <p>
 * Contains the text of the question, possible answers, the correct answer,
 * and metadata linking it to specific games or template types.
 *
 * @author machm
 */
public class Question {

    /** Unique identifier for the question. */
    private int id;

    /** A short title or category for the question. */
    private String title;

    /** The ID of the specific game this question relates to (optional). */
    private int gameId;

    /** The actual text of the question. */
    private String questionText;

    /** A list of possible answer options (distractors + correct answer). */
    private List<String> options;

    /** The correct answer string. */
    private String correctAnswer;

    /** * The ID representing the type of template/logic used to generate this question.
     * (e.g., "Guess by Image", "Guess by Description").
     */
    private int templateType;


    /**
     * Returns the unique identifier (ID) of the entity.
     *
     * @return The unique ID as an integer.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier (ID) of the entity.
     *
     * @param id The new unique ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Returns the title or name of the entity.
     *
     * @return The title as a String.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title or name of the entity.
     *
     * @param title The new title or name.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets the ID of the game associated with this question.
     *
     * @return The game ID.
     */
    public int getGameId() { return gameId; }

    /**
     * Sets the ID of the game associated with this question.
     *
     * @param gameId The game ID to set.
     */
    public void setGameId(int gameId) { this.gameId = gameId; }


    /**
     * Returns the text content of the question.
     *
     * @return The question text.
     */
    public String getQuestionText() { return questionText; }

    /**
     * Sets the text content of the question.
     *
     * @param questionText The new question text content.
     */
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    /**
     * Returns the list of available options (possible answers) for the question.
     *
     * @return A List of String objects representing the options.
     */
    public List<String> getOptions() { return options; }

    /**
     * Sets the list of options (possible answers) for the question.
     *
     * @param options The new list of options.
     */
    public void setOptions(List<String> options) { this.options = options; }

    /**
     * Returns the correct answer for the question.
     *
     * @return The correct answer as a String.
     */
    public String getCorrectAnswer() { return correctAnswer; }

    /**
     * Sets the correct answer for the question.
     *
     * @param correctAnswer The new correct answer.
     */
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    /**
     * Returns the template type of the question (e.g., 1 for single-choice, 2 for multiple-choice, etc.).
     *
     * @return An integer representing the question template type.
     */
    public int getTemplateType() { return templateType; }

    /**
     * Sets the template type of the question.
     *
     * @param templateType The new integer representing the question template type.
     */
    public void setTemplateType(int templateType) { this.templateType = templateType; }
}