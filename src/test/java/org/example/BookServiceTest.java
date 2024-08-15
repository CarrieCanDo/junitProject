package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private List<Book> bookDatabaseMock;

    private BookService bookServiceSpy;

    @BeforeEach
    void setUp() {
        // Initialize the mocks and create a spy on BookService with the mocked list
        MockitoAnnotations.openMocks(this);
        bookServiceSpy = spy(new BookService(bookDatabaseMock)); // Spy on BookService with a mocked list
    }

    // ---------------------- searchBook Tests ----------------------

    @Test
    void searchBook_Positive() {
        // Positive test: Book exists that matches the search keyword
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        when(bookDatabaseMock.stream()).thenReturn(new ArrayList<>(List.of(book1)).stream());

        List<Book> result = bookServiceSpy.searchBook("Title1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book1, result.get(0));
    }

    @Test
    void searchBook_Negative() {
        // Negative test: No book matches the search keyword
        when(bookDatabaseMock.stream()).thenReturn(new ArrayList<Book>().stream());

        List<Book> result = bookServiceSpy.searchBook("NonExistentTitle");

        assertTrue(result.isEmpty());
    }

    @Test
    void searchBook_EdgeCase() {
        // Edge test: Searching with an empty string should return all books
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        Book book2 = new Book("Title2", "Author2", "Genre2", 12.99, new ArrayList<>());
        when(bookDatabaseMock.stream()).thenReturn(new ArrayList<>(List.of(book1, book2)).stream());

        List<Book> result = bookServiceSpy.searchBook("");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ---------------------- purchaseBook Tests ----------------------

    @Test
    void purchaseBook_Positive() {
        // Positive test: Book exists in the database
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        User user = new User("username", "password", "email", new ArrayList<>());
        when(bookDatabaseMock.contains(book1)).thenReturn(true);

        boolean result = bookServiceSpy.purchaseBook(user, book1);

        assertTrue(result);
    }

    @Test
    void purchaseBook_Negative() {
        // Negative test: Book does not exist in the database
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        User user = new User("username", "password", "email", new ArrayList<>());
        when(bookDatabaseMock.contains(book1)).thenReturn(false);

        boolean result = bookServiceSpy.purchaseBook(user, book1);

        assertFalse(result);
    }

    @Test
    void purchaseBook_EdgeCase() {
        // Edge test: Attempting to purchase a null book
        User user = new User("username", "password", "email", new ArrayList<>());

        boolean result = bookServiceSpy.purchaseBook(user, null);

        assertFalse(result);
    }

    // ---------------------- addBookReview Tests ----------------------

    @Test
    void addBookReview_Positive() {
        // Positive test: User has purchased the book
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        List<Book> purchasedBooks = List.of(book1);
        User user = new User("username", "password", "email", purchasedBooks);
        when(user.getPurchasedBooks()).thenReturn(purchasedBooks);

        boolean result = bookServiceSpy.addBookReview(user, book1, "Great book!");

        assertTrue(result);
        verify(book1.getReviews()).add("Great book!");
    }

    @Test
    void addBookReview_Negative() {
        // Negative test: User has not purchased the book
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        User user = new User("username", "password", "email", new ArrayList<>());

        boolean result = bookServiceSpy.addBookReview(user, book1, "Great book!");

        assertFalse(result);
        verify(book1.getReviews(), never()).add(anyString());
    }

    @Test
    void addBookReview_EdgeCase() {
        // Edge test: Review is an empty string
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        List<Book> purchasedBooks = List.of(book1);
        User user = new User("username", "password", "email", purchasedBooks);
        when(user.getPurchasedBooks()).thenReturn(purchasedBooks);

        boolean result = bookServiceSpy.addBookReview(user, book1, "");

        assertTrue(result);
        verify(book1.getReviews()).add("");
    }

    // ---------------------- addBook Tests ----------------------

    @Test
    void addBook_Positive() {
        // Positive test: Book does not exist in the database and is added
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        when(bookDatabaseMock.contains(book1)).thenReturn(false);

        boolean result = bookServiceSpy.addBook(book1);

        assertTrue(result);
        verify(bookDatabaseMock).add(book1);
    }

    @Test
    void addBook_Negative() {
        // Negative test: Book already exists in the database
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        when(bookDatabaseMock.contains(book1)).thenReturn(true);

        boolean result = bookServiceSpy.addBook(book1);

        assertFalse(result);
        verify(bookDatabaseMock, never()).add(book1);
    }

    @Test
    void addBook_EdgeCase() {
        // Edge test: Attempting to add a null book
        boolean result = bookServiceSpy.addBook(null);

        assertFalse(result);
        verify(bookDatabaseMock, never()).add(null);
    }

    // ---------------------- removeBook Tests ----------------------

    @Test
    void removeBook_Positive() {
        // Positive test: Book exists in the database and is removed
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        when(bookDatabaseMock.remove(book1)).thenReturn(true);

        boolean result = bookServiceSpy.removeBook(book1);

        assertTrue(result);
        verify(bookDatabaseMock).remove(book1);
    }

    @Test
    void removeBook_Negative() {
        // Negative test: Book does not exist in the database
        Book book1 = new Book("Title1", "Author1", "Genre1", 9.99, new ArrayList<>());
        when(bookDatabaseMock.remove(book1)).thenReturn(false);

        boolean result = bookServiceSpy.removeBook(book1);

        assertFalse(result);
    }

    @Test
    void removeBook_EdgeCase() {
        // Edge test: Attempting to remove a null book
        boolean result = bookServiceSpy.removeBook(null);

        assertFalse(result);
        verify(bookDatabaseMock, never()).remove(null);
    }
}
