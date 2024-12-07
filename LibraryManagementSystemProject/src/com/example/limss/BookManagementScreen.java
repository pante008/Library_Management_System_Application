package com.example.limss;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookManagementScreen extends JFrame {

    public BookManagementScreen() {
        // Frame setup
        setTitle("Book Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 3, 10, 10));

        // Buttons for different book management actions
        JButton addButton = new JButton("Add Books");
        JButton updateButton = new JButton("Update Books");
        JButton deleteButton = new JButton("Delete Book");
        JButton findAllButton = new JButton("Find all Books");
        JButton findAvailableButton = new JButton("Find Available Books");

        // Adding components to the frame
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(findAllButton);
        add(findAvailableButton);

        // Adding action listeners to each button
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        findAllButton.addActionListener(e -> findAllBooks());
        findAvailableButton.addActionListener(e -> findAvailableBooks());
    }

    private void addBook() {
        AddBookForm addBookForm = new AddBookForm();
        addBookForm.setVisible(true);
    }

    private void updateBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter the Book ID to update:");
        if (bookIdStr != null && !bookIdStr.trim().isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                BookUpdateForm bookUpdateForm = new BookUpdateForm(bookId);
                bookUpdateForm.setVisible(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Book ID.");
            }
        }
    }

    private void deleteBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter the Book ID to delete:");
        if (bookIdStr != null && !bookIdStr.trim().isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                deleteBookFromDatabase(bookId);
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Book ID.");
            }
        }
    }

    private void findAllBooks() {
        java.util.List<Book> books;
        try {
            books = Book.findAllBooks();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books found.");
            return;
        }

        String[] columnNames = { "Book ID", "Title", "Author", "ISBN", "Total Copies", "Available Copies" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Book book : books) {
            model.addRow(new Object[] {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getTotalCopies(),
                    book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JDialog bookListDialog = new JDialog(this, "Book List", Dialog.ModalityType.APPLICATION_MODAL);
        bookListDialog.setLayout(new BorderLayout());
        bookListDialog.setSize(600, 400);
        bookListDialog.setLocationRelativeTo(this);
        bookListDialog.add(scrollPane, BorderLayout.CENTER);
        bookListDialog.setVisible(true);
    }

    private void findAvailableBooks() {
        java.util.List<Book> availableBooks;
        try {
            availableBooks = Book.findAvailableBooks();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        if (availableBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available books found.");
            return;
        }

        String[] columnNames = { "Book ID", "Title", "Author", "ISBN", "Total Copies", "Available Copies" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Book book : availableBooks) {
            model.addRow(new Object[] {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getTotalCopies(),
                    book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JDialog bookListDialog = new JDialog(this, "Book List", Dialog.ModalityType.APPLICATION_MODAL);
        bookListDialog.setLayout(new BorderLayout());
        bookListDialog.setSize(600, 400);
        bookListDialog.setLocationRelativeTo(this);
        bookListDialog.add(scrollPane, BorderLayout.CENTER);
        bookListDialog.setVisible(true);
    }

    class AddBookForm extends JDialog {
        private JTextField titleField, authorField, isbnField, copiesField, totalCopiesField;
        private JButton submitButton;

        public AddBookForm() {
            setTitle("Add Book");
            setLayout(new GridLayout(6, 2, 10, 10));
            setSize(400, 250);
            setLocationRelativeTo(BookManagementScreen.this);

            titleField = new JTextField();
            authorField = new JTextField();
            isbnField = new JTextField();
            copiesField = new JTextField();
            totalCopiesField = new JTextField();
            submitButton = new JButton("Add");

            add(new JLabel("Book Title:"));
            add(titleField);
            add(new JLabel("Author:"));
            add(authorField);
            add(new JLabel("ISBN:"));
            add(isbnField);
            add(new JLabel("Available Copies:"));
            add(copiesField);
            add(new JLabel("Total Copies:"));
            add(totalCopiesField);
            add(submitButton);

            submitButton.addActionListener(e -> submitBookInfo());
        }

        private void submitBookInfo() {
            Book book = new Book();
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setIsbn(isbnField.getText());
            book.setAvailableCopies(Integer.parseInt(copiesField.getText()));
            book.setTotalCopies(Integer.parseInt(totalCopiesField.getText()));

            try {
                book.addBook();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for copies.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error accessing the database: " + ex.getMessage());
            }
        }
    }

    class BookUpdateForm extends JDialog {
        private JTextField titleField, authorField, isbnField, copiesField, totalCopiesField;
        private JButton updateButton;
        private int bookId;

        public BookUpdateForm(int bookId) {
            setTitle("Update Book");
            setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            setLayout(new GridLayout(6, 2, 10, 10));
            setSize(400, 250);
            setLocationRelativeTo(BookManagementScreen.this);
            this.bookId = bookId;

            titleField = new JTextField();
            authorField = new JTextField();
            isbnField = new JTextField();
            copiesField = new JTextField();
            totalCopiesField = new JTextField();
            updateButton = new JButton("Update");

            add(new JLabel("Book Title:"));
            add(titleField);
            add(new JLabel("Author:"));
            add(authorField);
            add(new JLabel("ISBN:"));
            add(isbnField);
            add(new JLabel("Available Copies:"));
            add(copiesField);
            add(new JLabel("Total Copies:"));
            add(totalCopiesField);
            add(updateButton);

            updateButton.addActionListener(e -> updateBookInDatabase());

            try {
                Book book = findBookById(bookId);
                if (book != null) {
                    titleField.setText(book.getTitle());
                    authorField.setText(book.getAuthor());
                    isbnField.setText(book.getIsbn());
                    copiesField.setText(String.valueOf(book.getAvailableCopies()));
                    totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!");
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }

        private Book findBookById(int bookId) throws Exception {
            Book foundBook = Book.findBookById(String.valueOf(bookId));
            return foundBook;
        }

        private void updateBookInDatabase() {
            try {
                Book book = new Book(bookId, titleField.getText(), authorField.getText(), isbnField.getText(),
                        Integer.parseInt(copiesField.getText()), Integer.parseInt(totalCopiesField.getText()));
                book.updateBook();
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for copies.");
            }
        }
    }

    private void deleteBookFromDatabase(int bookId) {
        Book book = new Book();
		book.setBookId(bookId);
        try {
		    book.deleteBook();
		    JOptionPane.showMessageDialog(this, "Book deleted successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookManagementScreen().setVisible(true);
        });
    }
}
