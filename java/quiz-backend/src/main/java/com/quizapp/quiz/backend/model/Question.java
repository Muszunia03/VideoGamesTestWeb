package com.quizapp.quiz.backend.model;

import java.util.List;

public class Question {
    private int id;
    private String title;          // tytuł gry
    private String questionText;   // tekst pytania
    private List<String> options;  // opcje odpowiedzi
    private String correctAnswer;  // prawidłowa odpowiedź
    private int templateType;      // typ pytania

    // Gettery i settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public int getTemplateType() { return templateType; }
    public void setTemplateType(int templateType) { this.templateType = templateType; }
}
