package com.quizapp.quiz.backend.model;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Represents a video game entity within the application.
 * <p>
 * Maps to the database structure and contains detailed information such as
 * title, description, release date, genres, and platforms.
 *
 * @author machm
 */
public class Game {

    /** Unique internal identifier for the game. */
    private int id;

    /** Identifier from an external API (e.g., RAWG). */
    private String externalId;

    /** The official title of the game. */
    private String title;

    /** A brief description or summary of the game. */
    private String description;

    /** The date when the game was released. */
    private LocalDate releaseDate;

    /** URL to the game's background image (maps to DB 'background_image'). */
    private String backgroundImage;

    /** The game's rating (e.g., out of 5.0 or 100). */
    private Double rating;

    /** List of genres associated with the game (e.g., "Action", "RPG"). */
    private List<String> genres;

    /** List of platforms the game is available on (e.g., "PC", "PlayStation"). */
    private List<String> platforms;

    /** Timestamp indicating when this record was created in the database. */
    private Timestamp createdAt;

    /**
     * Default no-args constructor.
     */
    public Game() {}

    /**
     * Full constructor for initializing all fields of a Game.
     *
     * @param id              Internal ID.
     * @param externalId      External API ID.
     * @param title           Game title.
     * @param description     Game description.
     * @param releaseDate     Release date.
     * @param backgroundImage URL to the background image.
     * @param rating          Game rating.
     * @param genres          List of genres.
     * @param platforms       List of platforms.
     * @param createdAt       Creation timestamp.
     */
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

    /**
     * Lightweight constructor for simple use cases (e.g., lists or simple displays).
     * Keeps backward compatibility.
     *
     * @param id       Internal ID.
     * @param title    Game title.
     * @param imageUrl URL to the background image.
     */
    public Game(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.backgroundImage = imageUrl;
    }


    /**
     * Returns the internal, unique identifier of the entity.
     *
     * @return The unique ID as an integer.
     */
    public int getId() { return id; }

    /**
     * Sets the internal, unique identifier of the entity.
     *
     * @param id The new unique ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Returns the external, unique identifier (often used for integration with other systems).
     *
     * @return The external ID as a String.
     */
    public String getExternalId() { return externalId; }

    /**
     * Sets the external, unique identifier.
     *
     * @param externalId The new external ID.
     */
    public void setExternalId(String externalId) { this.externalId = externalId; }

    /**
     * Returns the title or name of the entity.
     *
     * @return The title as a String.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title or name of the entity.
     *
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns a detailed description of the entity.
     *
     * @return The description as a String.
     */
    public String getDescription() { return description; }

    /**
     * Sets a detailed description of the entity.
     *
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the release date of the entity.
     *
     * @return The release date as a LocalDate object.
     */
    public LocalDate getReleaseDate() { return releaseDate; }

    /**
     * Sets the release date of the entity.
     *
     * @param releaseDate The new release date.
     */
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    /**
     * Gets the background image URL.
     * Alias for {@link #getBackgroundImage()}.
     *
     * @return The image URL.
     */
    public String getImageUrl() { return backgroundImage; }

    /**
     * Returns the URL or path to the background image associated with the entity.
     *
     * @return The background image URL/path as a String.
     */
    public String getBackgroundImage() { return backgroundImage; }

    /**
     * Sets the URL or path to the background image associated with the entity.
     *
     * @param backgroundImage The new background image URL/path.
     */
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    /**
     * Returns the numerical rating (e.g., average user rating).
     *
     * @return The rating as a Double.
     */
    public Double getRating() { return rating; }

    /**
     * Sets the numerical rating.
     *
     * @param rating The new rating.
     */
    public void setRating(Double rating) { this.rating = rating; }


    /**
     * Returns the list of genres associated with the entity (e.g., a movie or game).
     *
     * @return A List of String objects representing the genres.
     */
    public List<String> getGenres() { return genres; }

    /**
     * Sets the list of genres associated with the entity.
     *
     * @param genres The new list of genres.
     */
    public void setGenres(List<String> genres) { this.genres = genres; }

    /**
     * Returns the list of platforms on which the entity is available (e.g., PC, PS5, iOS).
     *
     * @return A List of String objects representing the platforms.
     */
    public List<String> getPlatforms() { return platforms; }

    /**
     * Sets the list of platforms on which the entity is available.
     *
     * @param platforms The new list of platforms.
     */
    public void setPlatforms(List<String> platforms) { this.platforms = platforms; }

    /**
     * Returns the timestamp indicating when the entity record was created in the database.
     *
     * @return The creation time as a java.sql.Timestamp.
     */
    public Timestamp getCreatedAt() { return createdAt; }

    /**
     * Sets the timestamp indicating when the entity record was created.
     *
     * @param createdAt The new creation timestamp.
     */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /**
     * Helper method to extract just the year from the release date.
     *
     * @return The release year as an Integer, or null if the date is null.
     */
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

    /**
     * Compares this Game object with the specified object for equality.
     * <p>
     * Two Game objects are considered equal if and only if they have the same
     * {@code id} and the same {@code title}.
     *
     * @param o The object to compare with this Game.
     * @return {@code true} if the specified object is equal to this Game; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id == game.id && Objects.equals(title, game.title);
    }

    /**
     * Returns a hash code value for this Game object.
     * <p>
     * The hash code is calculated based on the {@code id} and {@code title} fields,
     * consistent with the fields used in the {@code equals} method,
     * satisfying the general contract for {@code Object.hashCode}.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}