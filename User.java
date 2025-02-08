package Onlinequiz;

public class User {
    private String username;
    private String name;
    private String email;
    private String password;
    private String role;
    private int score; // Store score for the user

    // Constructor to initialize User object
    public User(String username, String name, String email, String password, String role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.score = 0; // Initialize score to 0
    }

    // Getter for score
    public int getScore() {
        return this.score;
    }

    // Method to increase score by given points
    public void increaseScore(int points) {
        this.score += points;
    }

    // Getters and setters for other fields (if needed)
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Other methods if required
    


   

    public void increaseScore() {
        // Increment the instance variable score, not a local variable
        this.score++;
    }

}


