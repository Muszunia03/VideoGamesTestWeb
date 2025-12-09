package com.quizapp.quiz.backend.service;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for validating specific data rows in the database.
 * <p>
 * <b>Note:</b> This test requires a running PostgreSQL instance available at
 * {@code localhost:5432} with the database {@code postgres}, user {@code postgres},
 * and password {@code admin}. It connects directly via JDBC to verify data integrity.
 *
 * @author machm
 */
public class GameRowsValidationTest {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String pass = "admin";

    /**
     * Verifies the existence and data accuracy of specific games (GTA V, Witcher 3, Portal 2).
     * <p>
     * Connects to the database and checks if the title, release date, rating,
     * and genres match the expected hardcoded values.
     *
     * @throws Exception If a database access error occurs.
     */
    @Test
    void testSpecificGameRows() throws Exception {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            checkGame(
                    conn,
                    1052,
                    "Grand Theft Auto V",
                    LocalDate.of(2013, 9, 17),
                    4.5f,
                    new String[]{"Action"}
            );

            checkGame(
                    conn,
                    1053,
                    "The Witcher 3: Wild Hunt",
                    LocalDate.of(2015, 5, 18),
                    4.6f,
                    new String[]{"Action", "RPG"}
            );

            checkGame(
                    conn,
                    1054,
                    "Portal 2",
                    LocalDate.of(2011, 4, 18),
                    4.6f,
                    new String[]{"Shooter", "Puzzle"}
            );
        }
    }

    /**
     * Helper method to query the database for a specific game ID and assert the results.
     *
     * @param conn           Active JDBC connection.
     * @param id             The ID of the game to check.
     * @param expectedTitle  The expected title of the game.
     * @param expectedDate   The expected release date.
     * @param expectedRating The expected rating.
     * @param expectedGenres The expected array of genres.
     * @throws Exception If SQL execution fails or assertions are not met.
     */
    private void checkGame(
            Connection conn,
            int id,
            String expectedTitle,
            LocalDate expectedDate,
            float expectedRating,
            String[] expectedGenres
    ) throws Exception {

        String sql = "SELECT title, release_date, rating, genres FROM games WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Game with id " + id + " should exist");

                assertEquals(expectedTitle, rs.getString("title"));
                assertEquals(expectedDate, rs.getDate("release_date").toLocalDate());
                assertEquals(expectedRating, rs.getFloat("rating"));

                Array genresArr = rs.getArray("genres");
                String[] genres = genresArr != null ? (String[]) genresArr.getArray() : new String[0];
                assertArrayEquals(expectedGenres, genres, "Genres mismatch for id " + id);
            }
        }
    }
}