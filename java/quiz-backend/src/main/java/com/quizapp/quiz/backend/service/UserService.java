package com.quizapp.quiz.backend.service;

import com.quizapp.quiz.backend.model.UserProfileDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final JdbcTemplate jdbc;

    public UserService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Integer getUserIdByUsername(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT id FROM users WHERE LOWER(username) = LOWER(?)",
                    Integer.class,
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Nie znaleziono użytkownika: '" + username + "'");
            return null;
        }
    }

    public UserProfileDto getUserProfile(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT u.username, u.email, u.created_at, " +
                            "COALESCE(SUM(r.score), 0) AS total_score " +
                            "FROM users u " +
                            "LEFT JOIN results r ON r.user_id = u.id " +
                            "WHERE LOWER(u.username) = LOWER(?) " +
                            "GROUP BY u.id",
                    (rs, rowNum) -> new UserProfileDto(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("created_at"),
                            rs.getInt("total_score")
                    ),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
