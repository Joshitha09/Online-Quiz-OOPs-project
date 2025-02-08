package Onlinequiz;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {
    private List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
        loadQuestionsFromFile(); // Load questions at initialization
    }

    public void addQuestion(Question question) {
        questions.add(question);
        saveQuestionsToFile(); // Save questions after adding a new one
    }

    public void deleteQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            questions.remove(index);
            saveQuestionsToFile(); // Save questions after deletion
        }
    }

   

    public List<Question> getQuestions() {
        return questions;
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    public void loadQuestionsFromFile() {
        questions.clear(); // Clear any existing questions before loading
        try (BufferedReader reader = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String questionText = line.trim();
                String[] options = new String[4];

                for (int i = 0; i < 4; i++) {
                    line = reader.readLine();
                    if (line == null) {
                        System.out.println("Error: Missing option for question '" + questionText + "'.");
                        return;
                    }
                    options[i] = line.trim();
                }

                line = reader.readLine();
                int correctOption = Integer.parseInt(line.trim()); // Correct option is still an integer

                line = reader.readLine();
                String difficulty = line.trim(); // Difficulty is read as a string (no parsing)

                // Create the question object
                Question question = new Question(questionText, options, correctOption, difficulty);
                questions.add(question);

                // Read blank line separating questions
                reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error loading questions: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing the question file. Ensure the format is correct.");
        }
    }


    public void saveQuestionsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("questions.txt"))) {
            for (Question question : questions) {
                writer.write(question.getQuestionText());
                writer.newLine();
                for (String option : question.getOptions()) {
                    writer.write(option);
                    writer.newLine();
                }
                writer.write(String.valueOf(question.getCorrectOption()));
                writer.newLine();
                writer.write(question.getDifficulty());
                writer.newLine();
                writer.newLine(); // Separate questions by a blank line
            }
        } catch (IOException e) {
            System.out.println("Error saving questions: " + e.getMessage());
        }
    }
}
