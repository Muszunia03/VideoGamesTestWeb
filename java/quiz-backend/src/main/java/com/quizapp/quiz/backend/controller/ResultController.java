package com.quizapp.quiz.backend.controller;

import com.quizapp.quiz.backend.service.ResultService;
import com.quizapp.quiz.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    private final UserService userService;
    private final ResultService resultService;

    public ResultController(UserService userService, ResultService resultService) {
        this.userService = userService;
        this.resultService = resultService;
    }

    @PostMapping("/save")
    public String saveResult(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String quizType = (String) request.get("quizType");
        Number scoreNum = (Number) request.get("score");

        System.out.println("Otrzymano request: " + request);

        if (username == null || quizType == null || scoreNum == null) {
            return "Błąd: brakujące dane w request";
        }

        int score = scoreNum.intValue();

        Integer userId = userService.getUserIdByUsername(username);

        if (userId == null) {
            return "Błąd: użytkownik nie istnieje";
        }

        resultService.saveResult(userId, score, quizType);

        return "OK";
    }


    @GetMapping("/user/{username}")
    public List<Map<String, Object>> getResultsByUsername(@PathVariable String username) {

        Integer userId = userService.getUserIdByUsername(username);

        if (userId == null) {
            return List.of();
        }

        String query = "SELECT score, quiz_type, created_at FROM results_quiz WHERE user_id = ? ORDER BY created_at DESC";

        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "admin"
        );
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("score", rs.getInt("score"));
                row.put("quizType", rs.getString("quiz_type"));
                row.put("createdAt", rs.getTimestamp("created_at"));
                results.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
