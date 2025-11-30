package com.quizapp.quiz.backend.model;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Game {
    private int id;
    private String externalId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String backgroundImage; // maps to DB background_image
    private Double rating;
    private List<String> genres;
    private List<String> platforms;
    private Timestamp createdAt;

    public Game() {}

    // full constructor
    public Game(int id, String externalId, String title, String description,
                LocalDate releaseDate, String backgroundImage, Double rating,
                List<String> genres, List<String> platforms, Timestamp createdAt) {
        this.id = id;
        this.externalId = externalId;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.backgroundImage = backgroundImage;
        this.rating = rating;
        this.genres = genres;
        this.platforms = platforms;
        this.createdAt = createdAt;
    }

    // lightweight constructor (keeps backward compatibility)
    public Game(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.backgroundImage = imageUrl;
    }

    // --- getters & setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    // alias for frontend
    public String getImageUrl() { return backgroundImage; }
    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getPlatforms() { return platforms; }
    public void setPlatforms(List<String> platforms) { this.platforms = platforms; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Integer getReleaseYear() {
        return releaseDate == null ? null : releaseDate.getYear();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                ", genres=" + genres +
                ", platforms=" + platforms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id == game.id && Objects.equals(title, game.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
