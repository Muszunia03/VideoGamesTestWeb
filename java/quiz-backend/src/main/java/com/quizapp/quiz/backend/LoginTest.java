package com.quizapp.quiz.backend;

import java.util.Scanner;

public class LoginTest {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "admin";

        AuthService authService = new AuthService(url, dbUser, dbPassword);

        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - Zaloguj się\n2 - Zarejestruj się\nTwój wybór:");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Podaj login (email lub username): ");
            String login = scanner.nextLine();
            System.out.print("Podaj hasło: ");
            String password = scanner.nextLine();

            boolean success = authService.login(login, password);
            if (success) {
                System.out.println(" Zalogowano pomyślnie!");
            } else {
                System.out.println(" Logowanie nie powiodło się.");
            }

        } else if (choice.equals("2")) {
            System.out.print("Wybierz nazwę użytkownika: ");
            String username = scanner.nextLine();
            System.out.print("Podaj adres email: ");
            String email = scanner.nextLine();
            System.out.print("Ustaw hasło: ");
            String password = scanner.nextLine();

            String result = authService.register(username, email, password);
            switch (result) {
                case "success" -> System.out.println(" Zarejestrowano pomyślnie!");
                case "username_taken" -> System.out.println(" Nazwa użytkownika zajęta.");
                case "email_taken" -> System.out.println(" Email już istnieje.");
                default -> System.out.println("️ Błąd rejestracji.");
            }
        } else {
            System.out.println(" Nieznana opcja.");
        }
    }
}
