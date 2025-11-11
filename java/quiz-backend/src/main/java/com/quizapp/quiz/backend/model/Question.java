package com.quizapp.quiz.backend.model;

import java.util.List;

public class Question {
    private int id;
    private String title;
    private int gameId;            // <--- NEW FIELD: The ID of the game this question is about
    private String questionText;
    private List<String> options;  // answer options
    private String correctAnswer;
    private int templateType;      // question type

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // NEW GETTER AND SETTER FOR gameId
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public int getTemplateType() { return templateType; }
    public void setTemplateType(int templateType) { this.templateType = templateType; }
}