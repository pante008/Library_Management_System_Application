package com.example.limss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private int availableCopies;
    private int totalCopies; // Added attribute for total copies of the book

    // Default constructor
    public Book() {
    }

    // Parameterized constructor
    public Book(int bookId, String title, String author, String isbn, int availableCopies, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }

    // Getters
    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public int getTotalCopies() { // Getter for total copies
        return totalCopies;
    }

    // Setters
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setTotalCopies(int totalCopies) { // Setter for total copies
        this.totalCopies = totalCopies;
    }

    public static Book findBookByIdOrIsbn(String idOrIsbn) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE book_id = ? OR isbn = ?")) {
            pstmt.setString(1, idOrIsbn);
            pstmt.setString(2, idOrIsbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setAvailableCopies(rs.getInt("available_copies"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    return book;
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return null;
    }

    public static Book findBookById(String id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE book_id = ?")) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setAvailableCopies(rs.getInt("available_copies"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    return book;
                }
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return null;
    }

    // Methods for database operations (placeholders for actual implementation)
    public void addBook() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, isbn, total_copies, available_copies) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, this.title);
                statement.setString(2, this.author);
                statement.setString(3, this.isbn);
                statement.setInt(4, this.totalCopies);
                statement.setInt(5, this.availableCopies);
                statement.executeUpdate();
            }
        }
    }

    public void updateBook() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(
                        "UPDATE books SET title = ?, author = ?, isbn = ?, available_copies = ?, total_copies = ? WHERE book_id = ?")) {

            conn.setAutoCommit(false); // If your connection by default is set to auto-commit, turn it off

            statement.setString(1, this.title);
            statement.setString(2, this.author);
            statement.setString(3, this.isbn);
            statement.setInt(4, this.availableCopies);
            statement.setInt(5, this.totalCopies);
            statement.setInt(6, this.bookId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                conn.commit(); // Commit the transaction
            } else {
                throw new Exception("Error updating book.");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteBook() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM books WHERE book_id = ?")) {

            conn.setAutoCommit(false); // If your connection by default is set to auto-commit, turn it off

            statement.setInt(1, bookId);
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                conn.commit(); // Commit the transaction
            } else {
                throw new Exception("No book found with the given ID.");
            }
        } catch (SQLException ex) {
            throw ex;
        }
     }

    public static List<Book> findAllBooks() throws Exception {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                int totalCopies = rs.getInt("total_copies");
                int availableCopies = rs.getInt("available_copies");

                books.add(new Book(id, title, author, isbn, availableCopies, totalCopies));

                // model.addRow(new Object[]{id, title, author, isbn, totalCopies, availableCopies});
            }
        } catch (SQLException ex) {
            throw new Exception("Error accessing the database: " + ex.getMessage());
        }

        return books;
    }

    public static List<Book> findAvailableBooks() throws Exception {
        List<Book> allBooks;
        try {
            allBooks = findAllBooks();
        } catch (Exception ex) {
            throw ex;
        }
        ArrayList<Book> availableBooks = new ArrayList<>();

        if (allBooks.isEmpty()) {
            return availableBooks;
        }

        for (Book book: allBooks) {
            if (book.availableCopies > 0) {
                availableBooks.add(book);
            }
        }

        return availableBooks;
    }
}
