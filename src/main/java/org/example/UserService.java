package org.example;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    // The userDatabase stores user data with the username as the key and the User object as the value
    private Map<String, User> userDatabase;

    // Default constructor initializes the userDatabase as a new HashMap
    public UserService() {
        this.userDatabase = new HashMap<>();
    }

    // Constructor that allows injecting a custom userDatabase (used for testing)
    public UserService(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    // Registers a new user if the username doesn't already exist in the database
    public boolean registerUser(User user) {
        // Check if the username already exists
        if (userDatabase.containsKey(user.getUsername())) {
            return false; // User already exists, so registration fails
        }

        // Add the user to the database
        userDatabase.put(user.getUsername(), user);
        return true; // User registered successfully
    }

    // Logs in a user if the username exists and the password matches
    public User loginUser(String username, String password) {
        // Retrieve the user from the database using the username
        User user = userDatabase.get(username);

        // If the user doesn't exist, return null
        if (user == null) {
            return null; // User not found
        }

        // Check if the provided password matches the user's password
        if (!user.getPassword().equals(password)) {
            return null; // Password is incorrect
        }

        return user; // Login successful, return the user
    }

    // Updates the user's profile information (username, password, and email)
    public boolean updateUserProfile(User user, String newUsername, String newPassword, String newEmail) {
        // Check if the new username is already taken by another user
        if (userDatabase.containsKey(newUsername)) {
            return false; // New username is already taken, so update fails
        }

        // Update the user's information
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setEmail(newEmail);

        // Store the updated user in the database with the new username
        userDatabase.put(newUsername, user);
        return true; // User profile updated successfully
    }
}
