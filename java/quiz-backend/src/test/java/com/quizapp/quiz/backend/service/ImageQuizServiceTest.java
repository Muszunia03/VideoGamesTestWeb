package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Game;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ImageQuizService} class.
 * <p>
 * This class validates the core comparison logic used in the "Image Quiz" mode.
 * It tests how the service evaluates similarities between game attributes such as
 * genre lists, numeric ratings, release years, and title matching.
 *
 * @author machm
 */
class ImageQuizServiceTest {

    private final ImageQuizService service = new ImageQuizService();

    /**
     * Verifies the logic for comparing two lists of strings (e.g., genres or platforms).
     * <p>
     * Expected behaviors:
     * <ul>
     * <li><b>green:</b> Lists are identical.</li>
     * <li><b>yellow:</b> Lists share at least one common element but are not identical.</li>
     * <li><b>red:</b> Lists have no common elements or one/both are empty.</li>
     * </ul>
     */
    @Test
    void testCompareListsGenresStatus() {
        List<String> a = List.of("RPG", "Action");
        List<String> b = List.of("RPG", "Action");
        assertEquals("green", service.compareListsGenresStatus(a, b));

        List<String> c = List.of("RPG", "Puzzle");
        assertEquals("yellow", service.compareListsGenresStatus(a, c));

        List<String> d = List.of("Puzzle", "Simulation");
        assertEquals("red", service.compareListsGenresStatus(a, d));

        assertEquals("red", service.compareListsGenresStatus(a, List.of()));
        assertEquals("red", service.compareListsGenresStatus(List.of(), a));
    }

    /**
     * Verifies the logic for comparing numeric values (e.g., ratings).
     * <p>
     * Expected behaviors:
     * <ul>
     * <li><b>green:</b> Values are within a close range (e.g., < 0.5 difference).</li>
     * <li><b>yellow:</b> Values are somewhat close (e.g., < 1.0 difference).</li>
     * <li><b>red:</b> Values are far apart or one is null.</li>
     * </ul>
     */
    @Test
    void testCompareNumericRatingStatus() {
        assertEquals("green", service.compareNumericRatingStatus(5.0, 5.4));
        assertEquals("yellow", service.compareNumericRatingStatus(5.0, 5.9));
        assertEquals("red", service.compareNumericRatingStatus(5.0, 7.0));
        assertEquals("red", service.compareNumericRatingStatus(5.0, null));
    }

    /**
     * Verifies the logic for comparing release years.
     * <p>
     * Expected behaviors:
     * <ul>
     * <li><b>green:</b> Years are identical.</li>
     * <li><b>yellow:</b> Years are very close (e.g., +/- 1 year).</li>
     * <li><b>higher/lower:</b> Hint indicating the direction of the target year.</li>
     * <li><b>red:</b> Input is invalid (null).</li>
     * </ul>
     */
    @Test
    void testCompareYearStatus() {
        assertEquals("green", service.compareYearStatus(2020, 2020));
        assertEquals("yellow", service.compareYearStatus(2020, 2021));
        assertEquals("yellow", service.compareYearStatus(2020, 2019));
        assertEquals("higher", service.compareYearStatus(2020, 2022));
        assertEquals("red", service.compareYearStatus(null, 2020));
    }

    /**
     * Verifies the text matching logic to check if two strings share common words.
     * <p>
     * Useful for checking if a user's guess is partially correct (e.g., "Witcher" in "The Witcher 3").
     */
    @Test
    void testHasCommonWords() {
        assertTrue(service.hasCommonWords("The Witcher", "Witcher 3"));
        assertFalse(service.hasCommonWords("FIFA 23", "NBA 2K"));
    }

    /**
     * Verifies the comparison logic using actual {@link Game} objects.
     * <p>
     * This test ensures that the service can extract and compare attributes directly from
     * game instances.
     */
    @Test
    void testCompareGames_withMockedGames() {
        Game g1 = new Game(1, "ext1", "Game A", null, LocalDate.of(2020,1,1), null, 5.0, List.of("RPG"), List.of("PC"), null);
        Game g2 = new Game(2, "ext2", "Game A", null, LocalDate.of(2020,1,1), null, 5.0, List.of("RPG"), List.of("PC"), null);

        assertEquals("green", service.compareListsGenresStatus(g1.getGenres(), g2.getGenres()));
    }
}