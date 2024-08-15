package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    // Mocking the internal map to control behavior during the test
    @Mock
    private Map<String, User> userDatabaseMock;

    // The spy will allow us to test real methods but still control the behavior of the internal map
    private UserService userServiceSpy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); //Initialize the mocks and spies

        // Create a spy on the UserService with the mocked userDatabase
        userServiceSpy = spy(new UserService(userDatabaseMock)); // Spy on a UserService with a mocked map
    }

    @Test
    public void testRegisterUser_UserDoesNotExist_ShouldReturnTrue() {
        // Arrange: Prepare the user object and mock the behavior of the userDatabase
        User newUser = new User("john_doe", "password123", "john@example.com");
        when(userDatabaseMock.containsKey(newUser.getUsername())).thenReturn(false);

        // Act: Call the registerUser method on the spy
        boolean result = userServiceSpy.registerUser(newUser);

        // Assert: Verify the result and that the put method was called on the mocked map
        assertTrue(result); // Expect the registration to succeed
        verify(userDatabaseMock).put(newUser.getUsername(), newUser); // Ensure the user was added to the map
    }

    @Test
    public void testRegisterUser_UserAlreadyExists_ShouldReturnFalse() {
        // Arrange: Prepare a user that already exists and mock the behavior of the userDatabase
        User existingUser = new User("jane_doe", "password456", "jane@example.com");
        when(userDatabaseMock.containsKey(existingUser.getUsername())).thenReturn(true);

        // Act: Call the registerUser method on the spy
        boolean result = userServiceSpy.registerUser(existingUser);

        // Assert: Verify the result and ensure the put method was never called
        assertFalse(result); // Expect the registration to fail because the user already exists
        verify(userDatabaseMock, never()).put(anyString(), any(User.class)); // Ensure the user was not added
    }

    @Test
    public void testLoginUser_UserExistsAndPasswordCorrect_ShouldReturnUser() {
        // Arrange: Prepare the user and mock the behavior of the userDatabase
        String username = "john_doe";
        String password = "password123";
        User existingUser = new User(username, password, "john@example.com");
        when(userDatabaseMock.get(username)).thenReturn(existingUser);

        // Act: Call the loginUser method on the spy
        User result = userServiceSpy.loginUser(username, password);

        // Assert: Verify the result
        assertNotNull(result); // Expect the login to succeed
        assertEquals(existingUser, result); // Ensure the returned user matches the mock
    }

    @Test
    public void testLoginUser_UserDoesNotExist_ShouldReturnNull() {
        // Arrange: Mock the behavior of the userDatabase to return null for a non-existent user
        String username = "non_existent_user";
        String password = "password123";
        when(userDatabaseMock.get(username)).thenReturn(null);

        // Act: Call the loginUser method on the spy
        User result = userServiceSpy.loginUser(username, password);

        // Assert: Verify the result
        assertNull(result); // Expect the login to fail because the user doesn't exist
    }

    @Test
    public void testLoginUser_PasswordIncorrect_ShouldReturnNull() {
        // Arrange: Prepare the user and mock the behavior of the userDatabase
        String username = "john_doe";
        String correctPassword = "password123";
        String incorrectPassword = "wrong_password";
        User existingUser = new User(username, correctPassword, "john@example.com");
        when(userDatabaseMock.get(username)).thenReturn(existingUser);

        // Act: Call the loginUser method on the spy with the incorrect password
        User result = userServiceSpy.loginUser(username, incorrectPassword);

        // Assert: Verify the result
        assertNull(result); // Expect the login to fail due to the incorrect password
    }
}
