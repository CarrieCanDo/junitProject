package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private List<Book> bookDatabase;

    // Default constructor initializes the bookDatabase as a new ArrayList
    public BookService() {
        this.bookDatabase = new ArrayList<>();
    }

    // Constructor for injecting a custom bookDatabase (useful for testing)
    public BookService(List<Book> bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    // Search book by title, author, or genre
    public List<Book> searchBook(String keyword) {
        return bookDatabase.stream()
                .filter(book -> book.getTitle().contains(keyword) ||
                        book.getAuthor().contains(keyword) ||
                        book.getGenre().contains(keyword))
                .collect(Collectors.toList());
    }

    // Simulates a book purchase
    public boolean purchaseBook(User user, Book book) {
        return bookDatabase.contains(book);
    }

    // Adds a book review if the user has purchased the book
    public boolean addBookReview(User user, Book book, String review) {
        if (!user.getPurchasedBooks().contains(book)) {
            return false; // User has not purchased this book
        }

        book.getReviews().add(review);
        return true; // Review added successfully
    }

    // Adds a book to the database
    public boolean addBook(Book book) {
        if (bookDatabase.contains(book)) {
            return false; // Book is already in the database
        }

        bookDatabase.add(book);
        return true; // Book added successfully
    }

    // Removes a book from the database
    public boolean removeBook(Book book) {
        return bookDatabase.remove(book); // Book removed successfully if it was in the database
    }
}
