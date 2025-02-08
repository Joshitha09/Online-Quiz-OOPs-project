package Onlinequiz;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class QuizApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserLogin userLogin = new UserLogin();
        Quiz quiz = new Quiz();
        quiz.loadQuestionsFromFile();

        // Preload the admin user
        User adminUser = new User("varshitha", "Admin", "admin@example.com", "1234", "Admin");
        userLogin.addUser(adminUser);

        System.out.println("Welcome to the Online Quiz System!");

        while (true) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    User user = signIn(scanner, userLogin);
                    if (user != null) {
                        if ("Admin".equalsIgnoreCase(user.getRole())) {
                            adminMenu(scanner, quiz);
                        } else {
                            studentMenu(scanner, user, quiz);
                        }
                    }
                }
                case 2 -> signUp(scanner, userLogin);
                case 3 -> {
                    System.out.println("Exiting the application. Thank you!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static User signIn(Scanner scanner, UserLogin userLogin) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
        String password = getPasswordInput("Enter your password: ");
        
        User user = userLogin.authenticate(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
        } else {
            System.out.println("Welcome, " + user.getName() + "!");
        }
        return user;
    }

    private static void signUp(Scanner scanner, UserLogin userLogin) {
        // Username input validation (only check if empty)
        String newUsername;
        while (true) {
            System.out.print("Enter a username: ");
            newUsername = scanner.nextLine();
            
            // Validate that the username is not empty
            if (newUsername.trim().isEmpty()) {
                System.out.println("Username cannot be empty. Please enter a valid username.");
            } else {
                break; // Proceed if the username is not empty
            }
        }

        // Password input validation (only check if empty)
        String newPassword;
        while (true) {
            System.out.print("Enter a password: ");
            newPassword = getPasswordInput(""); // getPasswordInput method should read input securely
            
            // Validate that the password is not empty
            if (newPassword.trim().isEmpty()) {
                System.out.println("Password cannot be empty. Please enter a valid password.");
            } else {
                break; // Proceed if the password is not empty
            }
        }

        // Name input
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Email input with validation
        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        // Role input with validation
        String role;
        while (true) {
            System.out.print("Enter role: ");
            role = scanner.nextLine();
            if (role.equalsIgnoreCase("Student")) {
                break;
            } else {
                System.out.println("Invalid role. Only 'Student' role is allowed for new sign-ups.");
            }
        }

        // Create new user and attempt to add them to the system
        User newUser = new User(newUsername, name, email, newPassword, role);
        String signUpResult = userLogin.addUser(newUser);
        System.out.println(signUpResult);
    }

    private static boolean isValidEmail(String email) {
        // Regular Expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    private static String getPasswordInput(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        } else {
            // Fallback for IDEs that don't support Console
            System.out.print(prompt);
            return new Scanner(System.in).nextLine(); // No masking if Console unavailable
        }
    }

 // In adminMenu method within QuizApp
    private static void adminMenu(Scanner scanner, Quiz quiz) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Question");
            System.out.println("2. Delete Question");
            System.out.println("3. Update Question");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addQuestion(scanner, quiz); // Calls helper method to handle input and add the question
                    break;
                case 2:
                    deleteQuestion(scanner, quiz);
                    break;
                case 3:
                    updateQuestion(scanner, quiz);
                    break;
                case 4:
                    return; // Exit to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addQuestion(Scanner scanner,Quiz quiz) {
        System.out.print("Enter the question text: ");
        String questionText = scanner.nextLine();

        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            System.out.print("Enter option " + (i + 1) + ": ");
            options[i] = scanner.nextLine();
        }

        int correctOption = getIntInput(scanner, "Enter the correct option number (1-4): ", 1, 4) - 1;

        System.out.print("Enter difficulty level (Easy, Medium, Hard): ");
        String difficulty = scanner.nextLine();

        // Create a new Question object
        Question newQuestion = new Question(questionText, options, correctOption, difficulty);

        // Add the question to the quiz
        quiz.addQuestion(newQuestion);

        quiz.saveQuestionsToFile(); // Save the new question to the file
        System.out.println("Question added successfully!");
    }


    private static void deleteQuestion(Scanner scanner, Quiz quiz) {
        // Categorize questions by difficulty level
        List<Question> easyQuestions = new ArrayList<>();
        List<Question> mediumQuestions = new ArrayList<>();
        List<Question> hardQuestions = new ArrayList<>();

        for (Question question : quiz.getQuestions()) {
            switch (question.getDifficulty().toLowerCase()) {
                case "easy" -> easyQuestions.add(question);
                case "medium" -> mediumQuestions.add(question);
                case "hard" -> hardQuestions.add(question);
            }
        }

        // Display difficulty options
        System.out.println("\nSelect Difficulty Level to Delete Question:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        // Get user choice for difficulty level
        int levelChoice = getIntInput(scanner, "Enter your choice (1-3): ", 1, 3);

        // Determine the selected questions list
        List<Question> selectedQuestions;
        switch (levelChoice) {
            case 1 -> selectedQuestions = easyQuestions;
            case 2 -> selectedQuestions = mediumQuestions;
            case 3 -> selectedQuestions = hardQuestions;
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        if (selectedQuestions.isEmpty()) {
            System.out.println("No questions available for the selected difficulty level.");
            return;
        }

        // Display the questions from the selected difficulty
        System.out.println("\nQuestions List:");
        for (int i = 0; i < selectedQuestions.size(); i++) {
            System.out.println((i + 1) + ": " + selectedQuestions.get(i).getQuestionText());
        }

        // Get the question to delete
        int questionIndex = getIntInput(scanner, "Enter the question number to delete: ", 1, selectedQuestions.size()) - 1;

        // Remove the question from the quiz
        Question questionToDelete = selectedQuestions.get(questionIndex);
        quiz.getQuestions().remove(questionToDelete);

        // Save changes to file
        quiz.saveQuestionsToFile();
        System.out.println("Question deleted successfully!");
    }


    

	// New updateQuestion method in QuizApp
    private static void updateQuestion(Scanner scanner, Quiz quiz) {
        // Categorize questions by difficulty
        List<Question> easyQuestions = new ArrayList<>();
        List<Question> mediumQuestions = new ArrayList<>();
        List<Question> hardQuestions = new ArrayList<>();

        for (Question question : quiz.getQuestions()) {
            switch (question.getDifficulty().toLowerCase()) {
                case "easy" -> easyQuestions.add(question);
                case "medium" -> mediumQuestions.add(question);
                case "hard" -> hardQuestions.add(question);
            }
        }

        // Display difficulty options
        System.out.println("\nSelect Difficulty Level to Update:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        int levelChoice = getIntInput(scanner, "Enter your choice (1-3): ", 1, 3);
        List<Question> selectedQuestions;
        switch (levelChoice) {
            case 1 -> selectedQuestions = easyQuestions;
            case 2 -> selectedQuestions = mediumQuestions;
            case 3 -> selectedQuestions = hardQuestions;
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        if (selectedQuestions.isEmpty()) {
            System.out.println("No questions available for the selected difficulty.");
            return;
        }

        // Display selected questions
        System.out.println("\nQuestions:");
        for (int i = 0; i < selectedQuestions.size(); i++) {
            System.out.println((i + 1) + ": " + selectedQuestions.get(i).getQuestionText());
        }

        int questionIndex = getIntInput(scanner, "Enter the question number to update: ", 1, selectedQuestions.size()) - 1;

        Question questionToUpdate = selectedQuestions.get(questionIndex);

        // Update question details
        System.out.print("Enter new question text: ");
        String questionText = scanner.nextLine();

        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            System.out.print("Enter new option " + (i + 1) + ": ");
            options[i] = scanner.nextLine();
        }

        int correctOption = getIntInput(scanner, "Enter the new correct option number (1-4): ", 1, 4) - 1;

        System.out.print("Enter new difficulty level (Easy, Medium, Hard): ");
        String difficulty = scanner.nextLine();

        questionToUpdate.setQuestionText(questionText);
        questionToUpdate.setOptions(options);
        questionToUpdate.setCorrectOption(correctOption);
        questionToUpdate.setDifficulty(difficulty);

        quiz.saveQuestionsToFile(); // Save updated questions
        System.out.println("Question updated successfully!");
    }

    // Utility method to get validated integer input
    private static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    

    
    private static void takePracticeTest(Scanner scanner, Quiz quiz, User student) {
        System.out.print("Choose difficulty level (Easy, Medium, Hard): ");
        String difficulty = scanner.nextLine();

        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            if (question.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(question);
            }
        }

        if (filteredQuestions.isEmpty()) {
            System.out.println("No questions available for the selected difficulty.");
            return;
        }

        quiz.shuffleQuestions();

        System.out.println("Practice Test (Answers will be shown after each question):");

        // Set up a 2-minute timer
        QuizTimer timer = new QuizTimer(120000, () -> {
            System.out.println("\nTime's up!");
            System.out.println("Practice Test ended.");
            System.exit(0);
        });

        for (Question question : filteredQuestions) {
            System.out.println(question.getQuestionText());
            for (int i = 0; i < question.getOptions().length; i++) {
                System.out.println((i + 1) + ": " + question.getOptions()[i]);
            }

            System.out.print("Your answer (1-4): ");
            int answer = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            if (question.isCorrectAnswer(answer)) {
                System.out.println("Correct!");
                student.increaseScore();
            } else {
                System.out.println("Incorrect. The correct answer is: " + question.getOptions()[question.getCorrectAnswerIndex()]);
            }
            System.out.println();
        }

        timer.cancel(); // Cancel timer if test finishes within the time
        System.out.println("Practice Test Completed!");
        System.out.println("Your total score: " + student.getScore());
    }

    private static void takeGradedTest(Scanner scanner, Quiz quiz, User student) {
        System.out.print("Choose difficulty level (Easy, Medium, Hard): ");
        String difficulty = scanner.nextLine();

        // Filter questions based on the selected difficulty level
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            if (question.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(question);
            }
        }

        if (filteredQuestions.isEmpty()) {
            System.out.println("No questions available for the selected difficulty.");
            return;
        }

        // Shuffle the filtered questions to ensure randomness
        Collections.shuffle(filteredQuestions);

        // Select only 3 random questions (or fewer if there are not enough questions)
        List<Question> selectedQuestions = filteredQuestions.subList(0, Math.min(3, filteredQuestions.size()));

        int correctCount = 0;
        int incorrectCount = 0;
        List<Question> incorrectQuestions = new ArrayList<>();

        System.out.println("Graded Test (No feedback during the test):");

        // Set up a 2-minute timer
        QuizTimer timer = new QuizTimer(120000, () -> {
            System.out.println("\nTime's up!");
            System.out.println("Graded Test ended.");
            System.exit(0);
        });

        // Loop through the selected questions
        for (Question question : selectedQuestions) {
            System.out.println(question.getQuestionText());
            for (int i = 0; i < question.getOptions().length; i++) {
                System.out.println((i + 1) + ": " + question.getOptions()[i]);
            }

            System.out.print("Your answer (1-4): ");
            int answer = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            if (question.isCorrectAnswer(answer)) {
                correctCount++;
                System.out.println("Correct!");
            } else {
                incorrectCount++;
                System.out.println("Incorrect. The correct answer is: " + question.getOptions()[question.getCorrectAnswerIndex()]);
                incorrectQuestions.add(question); // Store the incorrectly answered questions
            }

            System.out.println(); // Line gap after each question
        }

        timer.cancel(); // Cancel timer if test finishes within the time
        System.out.println("\nTest Report");
        System.out.println("===========");
        System.out.println("Score     : " + correctCount + "/" + selectedQuestions.size());
        System.out.println("Correct   : " + correctCount);
        System.out.println("Incorrect : " + incorrectCount);

        // Display the incorrectly answered questions
        if (!incorrectQuestions.isEmpty()) {
            System.out.println("\nIncorrectly answered questions:");
            for (Question question : incorrectQuestions) {
                System.out.println("Question             : " + question.getQuestionText());
                System.out.println("Correct answer       : " + question.getOptions()[question.getCorrectAnswerIndex()]);
                System.out.println(); // Line gap
            }
        }
    }




    private static void displayQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            System.out.println("No questions available.");
        } else {
            for (int i = 0; i < questions.size(); i++) {
                System.out.println((i + 1) + ": " + questions.get(i).getQuestionText());
            }
        }
    }


    private static void studentMenu(Scanner scanner, User student, Quiz quiz) {
        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. Take Practice Test");
            System.out.println("2. Take Graded Test");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    takePracticeTest(scanner, quiz, student);
                    break;
                case 2:
                    takeGradedTest(scanner, quiz, student); // Call the graded test
                    break;
                case 3:
                    System.out.println("Exiting student menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void takeQuiz(Scanner scanner, Quiz quiz, User user) {
        System.out.print("Choose difficulty level (Easy, Medium, Hard): ");
        String difficulty = scanner.nextLine();

        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            if (question.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(question);
            }
        }

        if (filteredQuestions.isEmpty()) {
            System.out.println("No questions available for the selected difficulty.");
            return;
        }

        quiz.shuffleQuestions();
        final int[] score = {0};

        // Set up the timer to end the quiz after 2 minutes (120000 milliseconds)
        QuizTimer quizTimer = new QuizTimer(120000, () -> {
            System.out.println("\nTime's up!");
            System.out.println("Quiz ended. Your score: " + score[0] + " out of 3");
            System.exit(0);
        });

        for (int i = 0; i < Math.min(3, filteredQuestions.size()); i++) {
            Question question = filteredQuestions.get(i);
            System.out.println(question.getQuestionText());
            for (int j = 0; j < question.getOptions().length; j++) {
                System.out.println((j + 1) + ": " + question.getOptions()[j]);
            }
            System.out.print("Your answer (1-4): ");
            int answer = scanner.nextInt() - 1;
            scanner.nextLine();

            if (question.isCorrectAnswer(answer)) {
                System.out.println("Correct!");
                score[0]++;
            } else {
                System.out.println("Incorrect.");
            }
        }

        quizTimer.cancel(); // Cancel the timer if quiz completed in time
        user.increaseScore();
        System.out.println("Quiz completed! Your score: " + score[0]);
    }
}
