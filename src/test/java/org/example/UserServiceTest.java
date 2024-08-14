package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void registerUser_Success() {
        User user = new User("jo blow", "password123", "joblow@example.com");
        boolean result = userService.registerUser(user);
        assertTrue(result, "User should be registered successfully");
    }

    @Test
    void registerUser_UserAlreadyExists() {
        User user1 = new User("jo blow", "password123", "joblow@example.com");
        userService.registerUser(user1);

        User user2 = new User("jo blow", "password456", jo.blow@example.com);
        boolean result = userService.registerUser(user2);
        assertFalse(result, "User should not be registered if username already exists");
    }

    @Test
    void registerUser_EmptyUsername() {
        User user = new User("", "password123", "joblow@example.com");
        boolean result = userService.registerUser(user);
        assertFalse(result, "User should not be registered with an empty username");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loginUser() {
    }






}