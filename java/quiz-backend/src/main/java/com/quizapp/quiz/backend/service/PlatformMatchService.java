package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.Question;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class PlatformMatchService {

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    private static final List<String> ALL_PLATFORMS = Arrays.asList(
            "PC", "PlayStation", "PlayStation 2", "PlayStation 3", "PlayStation 4",
            "Xbox", "Xbox 360", "Xbox One",
            "Nintendo DS", "Nintendo Switch", "Wii", "GameCube",
            "Sega Genesis", "Dreamcast", "Mobile", "PSP", "PS Vita"
    );

    private static final Map<String, Integer> PLATFORM_GENERATION = Map.ofEntries(
            Map.entry("Sega Genesis", 4),
            Map.entry("PlayStation", 5),
            Map.entry("Nintendo 64", 5),
            Map.entry("Dreamcast", 6),
            Map.entry("PlayStation 2", 6),
            Map.entry("Xbox", 6),
            Map.entry("GameCube", 6),
            Map.entry("PlayStation 3", 7),
            Map.entry("Xbox 360", 7),
            Map.entry("Nintendo Wii", 7),
            Map.entry("PlayStation 4", 8),
            Map.entry("Xbox One", 8),
            Map.entry("Nintendo Switch", 8),
            Map.entry("PSP", 6),
            Map.entry("PS Vita", 8),
            Map.entry("Nintendo DS", 7)
    );

    public Question getSinglePlatformQuestion() {

        String query = """
            SELECT id, title, platforms
            FROM games
            WHERE array_length(platforms, 1) > 0
            ORDER BY RANDOM()
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) return null;

            int gameId = rs.getInt("id");
            String title = rs.getString("title");

            Array platformsArray = rs.getArray("platforms");
            List<String> platforms = Arrays.asList((String[]) platformsArray.getArray());

            Random random = new Random();
            int template = random.nextInt(11); // now 0–10

            Question q = new Question();
            q.setGameId(gameId);
            q.setTitle(title);
            q.setTemplateType(template);

            switch (template) {

                // TEMPLATE 0 — Debut platform
                case 0 -> {
                    String correct = platforms.get(0);

                    List<String> options = new ArrayList<>();
                    options.add(correct);

                    Collections.shuffle(ALL_PLATFORMS);
                    for (String p : ALL_PLATFORMS) {
                        if (!platforms.contains(p) && options.size() < 4) {
                            options.add(p);
                        }
                    }

                    Collections.shuffle(options);

                    q.setQuestionText("On which platform did '" + title + "' debut?");
                    q.setOptions(options);
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 1 — Yes/No: Did the game release on X?
                case 1 -> {
                    String randomPlatform = ALL_PLATFORMS.get(random.nextInt(ALL_PLATFORMS.size()));
                    boolean released = platforms.contains(randomPlatform);

                    q.setQuestionText("Did '" + title + "' release on " + randomPlatform + "?");
                    q.setOptions(Arrays.asList("Yes", "No"));
                    q.setCorrectAnswer(released ? "Yes" : "No");
                }

                // TEMPLATE 2 — Platform count
                case 2 -> {
                    q.setQuestionText("How many platforms did '" + title + "' release on?");
                    q.setOptions(Collections.emptyList());
                    q.setCorrectAnswer(String.valueOf(platforms.size()));
                }

                // TEMPLATE 3 — Which platform is NOT supported?
                case 3 -> {
                    String correct = null;

                    for (String p : ALL_PLATFORMS) {
                        if (!platforms.contains(p)) {
                            correct = p;
                            break;
                        }
                    }

                    List<String> options = new ArrayList<>();
                    options.add(correct);

                    Collections.shuffle(ALL_PLATFORMS);
                    for (String p : ALL_PLATFORMS) {
                        if (!p.equals(correct) && options.size() < 4) {
                            options.add(p);
                        }
                    }

                    Collections.shuffle(options);

                    q.setQuestionText("Which of these platforms did '" + title + "' NOT release on?");
                    q.setOptions(options);
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 4 — What was the second platform?
                case 4 -> {
                    if (platforms.size() < 2) return getSinglePlatformQuestion();

                    String correct = platforms.get(1);

                    List<String> options = new ArrayList<>();
                    options.add(correct);

                    for (String p : ALL_PLATFORMS) {
                        if (!platforms.contains(p) && options.size() < 4) {
                            options.add(p);
                        }
                    }

                    Collections.shuffle(options);

                    q.setQuestionText("What was the SECOND platform for '" + title + "'?");
                    q.setOptions(options);
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 5 — Which platform did it release on earlier?
                case 5 -> {
                    if (platforms.size() < 2) return getSinglePlatformQuestion();

                    List<String> twoPlatforms = new ArrayList<>(platforms);
                    Collections.shuffle(twoPlatforms);
                    twoPlatforms = twoPlatforms.subList(0, 2);

                    String correct = twoPlatforms.get(0);

                    q.setQuestionText("Which platform did '" + title + "' release on earlier?");
                    q.setOptions(twoPlatforms);
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 6 — True/False: It did NOT release on X
                case 6 -> {
                    String randomPlatform = ALL_PLATFORMS.get(random.nextInt(ALL_PLATFORMS.size()));
                    boolean notReleased = !platforms.contains(randomPlatform);

                    q.setQuestionText("True or False: '" + title + "' did NOT release on " + randomPlatform + ".");
                    q.setOptions(Arrays.asList("True", "False"));
                    q.setCorrectAnswer(notReleased ? "True" : "False");
                }

                // TEMPLATE 7 — Console family match
                case 7 -> {
                    List<String> sony = List.of("PlayStation", "PlayStation 2", "PlayStation 3", "PlayStation 4", "PSP", "PS Vita");
                    List<String> nintendo = List.of("Nintendo DS", "Nintendo Switch", "Wii", "GameCube");
                    List<String> xbox = List.of("Xbox", "Xbox 360", "Xbox One");

                    List<String> correctGroup = null;

                    if (!Collections.disjoint(platforms, sony)) correctGroup = sony;
                    else if (!Collections.disjoint(platforms, nintendo)) correctGroup = nintendo;
                    else if (!Collections.disjoint(platforms, xbox)) correctGroup = xbox;
                    else correctGroup = sony;

                    List<String> options = new ArrayList<>(correctGroup);
                    Collections.shuffle(options);

                    q.setQuestionText("Which platform belongs to the SAME console family as a platform of '" + title + "'?");
                    q.setOptions(options.subList(0, Math.min(4, options.size())));
                    q.setCorrectAnswer(options.get(0));
                }

                // TEMPLATE 8 — Handheld platforms only
                case 8 -> {
                    List<String> handheld = List.of("PSP", "PS Vita", "Nintendo DS");

                    List<String> valid = new ArrayList<>();
                    for (String p : platforms) {
                        if (handheld.contains(p)) valid.add(p);
                    }

                    if (valid.isEmpty()) return getSinglePlatformQuestion();

                    String correct = valid.get(0);

                    List<String> options = new ArrayList<>();
                    options.add(correct);

                    for (String p : handheld) {
                        if (!valid.contains(p) && options.size() < 4) options.add(p);
                    }

                    Collections.shuffle(options);

                    q.setQuestionText("On which handheld platform did '" + title + "' release?");
                    q.setOptions(options);
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 9 — Order platforms (oldest → newest)
                case 9 -> {
                    List<String> sorted = new ArrayList<>(platforms);
                    sorted.sort(Comparator.comparing(p -> PLATFORM_GENERATION.getOrDefault(p, 99)));

                    String correct = sorted.get(0);

                    List<String> options = new ArrayList<>(platforms);
                    Collections.shuffle(options);

                    q.setQuestionText("Which platform of '" + title + "' is the OLDEST?");
                    q.setOptions(options.subList(0, Math.min(4, options.size())));
                    q.setCorrectAnswer(correct);
                }

                // TEMPLATE 10 — Platform generation number
                case 10 -> {
                    String platform = platforms.get(0);
                    int generation = PLATFORM_GENERATION.getOrDefault(platform, 0);

                    q.setQuestionText("What console generation does the platform '" + platform + "' belong to?");
                    q.setOptions(Collections.emptyList());
                    q.setCorrectAnswer(String.valueOf(generation));
                }
            }

            return q;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
