/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizapp.quiz.backend;

/**
 *
 * @author machm
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main application class for the Quiz Backend.
 * <p>
 * This class uses the {@code @SpringBootApplication} annotation,
 * which is a convenience annotation that adds:
 * <ul>
 * <li>{@code @Configuration}: Tags the class as a source of bean definitions.</li>
 * <li>{@code @EnableAutoConfiguration}: Tells Spring Boot to start adding beans based on classpath settings.</li>
 * <li>{@code @ComponentScan}: Tells Spring to look for other components, configurations, and services in the {@code com.quizapp.quiz.backend} package, allowing it to find controllers, repositories, etc.</li>
 * </ul>
 *
 * @author machm
 */

@SpringBootApplication
public class QuizBackendApplication {

    /**
     * The entry point of the Spring Boot application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(QuizBackendApplication.class, args);
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) for the application.
     * <p>
     * This setup allows resources from the specified origin ({@code http://localhost:5173})
     * to access endpoints defined in this backend application ({@code "/**"}).
     * This is typically used to enable communication with a frontend application running on a different port.
     *
     * @return A {@code WebMvcConfigurer} bean configured for CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:5173");
            }
        };
    }
}