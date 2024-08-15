package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

public class UserServiceTest {

    @Mock
    private UserService userService;
    private Map<String, User> userDatabaseMock;

    @BeforeEach
    public void setUp() {
        userDatabaseMock = mock(Map.class);
        userService = new UserService();
    }

    @Test
    public void testRegisterUser_UserDoesNotExist_ShouldReturnTrue() {
        // Arrange
        User newUser = new User("john_doe", "password123", "john@example.com");

        when(userDatabaseMock.containsKey(newUser.getUsername())).thenReturn(false);

        // Act
        boolean result = userService.registerUser(newUser);

        // Assert
        assertTrue(result);
        verify(userDatabaseMock).put(newUser.getUsername(), newUser);
    }


    @Test
    public void testRegisterUser_UserAlreadyExists_ShouldReturnFalse() {
        // Arrange
        User existingUser = new User("jane_doe", "password456", "jane@example.com");

        when(userDatabaseMock.containsKey(existingUser.getUsername())).thenReturn(true);

        // Act
        boolean result = userService.registerUser(existingUser);

        // Assert
        assertFalse(result);
        verify(userDatabaseMock, never()).put(anyString(), any(User.class));
    }

    @Test
    public void testLoginUser_UserExistsAndPasswordCorrect_ShouldReturnUser() {
        // Arrange
        String username = "john_doe";
        String password = "password123";
        User existingUser = new User(username, password, "john@example.com");

        when(userDatabaseMock.get(username)).thenReturn(existingUser);

        // Act
        User result = userService.loginUser(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(existingUser, result);
    }

    @Test
    public void testLoginUser_UserDoesNotExist_ShouldReturnNull() {
        // Arrange
        String username = "non_existent_user";
        String password = "password123";

        when(userDatabaseMock.get(username)).thenReturn(null);

        // Act
        User result = userService.loginUser(username, password);

        // Assert
        assertNull(result);
    }

    @Test
    public void testLoginUser_PasswordIncorrect_ShouldReturnNull() {
        // Arrange
        String username = "john_doe";
        String correctPassword = "password123";
        String incorrectPassword = "wrong_password";
        User existingUser = new User(username, correctPassword, "john@example.com");

        when(userDatabaseMock.get(username)).thenReturn(existingUser);

        // Act
        User result = userService.loginUser(username, incorrectPassword);

        // Assert
        assertNull(result);
    }
}
