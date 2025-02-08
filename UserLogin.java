package Onlinequiz;

import java.util.ArrayList;
import java.util.List;

public class UserLogin {
    private List<User> users;

    public UserLogin() {
        users = new ArrayList<>();
    }

    public String addUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return "Username already taken. Please choose a different username.";
            }
        }
        users.add(user);
        return "User registered successfully!";
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
	
