package com.example.limss;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class BorrowerDashboard extends JFrame {

    public BorrowerDashboard(String borrowerName) {
        // Frame setup
        setTitle("Borrower Dashboard");
        setSize(1000, 500); // Increased size for better visibility
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2, 10, 10)); // Split the frame into two parts
        setLayout(new BorderLayout()); // Border layout for splitting the frame
        getContentPane().setBackground(new Color(240, 240, 240)); // Set background color of content pane
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 10)); // Add light green border

        // Right panel for displaying borrower info and buttons
        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome " + borrowerName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16)); // Bold font
        rightPanel.add(welcomeLabel);

        // Buttons for different borrower actions
        JButton viewAvailableBooksButton = new JButton("View Available Books");
        JButton viewBorrowingHistoryButton = new JButton("View Borrowing History");
        rightPanel.add(viewAvailableBooksButton);
        rightPanel.add(viewBorrowingHistoryButton);

        // Event listeners for buttons
        viewAvailableBooksButton.addActionListener(e -> viewAvailableBooks());
        viewBorrowingHistoryButton.addActionListener(e -> viewBorrowingHistory());

        add(rightPanel);

        add(rightPanel, BorderLayout.CENTER);

        // Left panel for displaying image
        JPanel leftPanel = new JPanel(new BorderLayout());
        ImageIcon borrowerImage = new ImageIcon("src/com/example/limss/borrower.png"); // Assuming borrower.jpeg is in
                                                                                       // the project directory
        JLabel imageLabel = new JLabel(borrowerImage);
        leftPanel.add(imageLabel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

    }

    public void viewAvailableBooks() {

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

        String[] columnNames = { "Book ID", "Title", "Available Copies" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Book book : availableBooks) {
            model.addRow(new Object[] {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JDialog bookListDialog = new JDialog(this, "Available Books", Dialog.ModalityType.APPLICATION_MODAL);
        bookListDialog.setLayout(new BorderLayout());
        bookListDialog.setSize(600, 400);
        bookListDialog.setLocationRelativeTo(this);
        bookListDialog.add(scrollPane, BorderLayout.CENTER);
        bookListDialog.setVisible(true);
    }

    public void viewBorrowingHistory() {
        // Prompt the user to enter borrower ID
        String borrowerID = JOptionPane.showInputDialog(this, "Enter Borrower ID:");

        if (borrowerID != null && !borrowerID.isEmpty()) {

            java.util.List<Transaction> history;
        try {
            history = Transaction.findTransactionsByBorrowerId(Integer.parseInt(borrowerID));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No borrowing history found.");
            return;
        }

        String[] columnNames = { "Book ID", "Borrower ID", "Issue Date", "Due Date", "Return Date", "Title" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Transaction temp : history) {
            model.addRow(new Object[] {
                temp.getBookId(),
                temp.getBorrowerId(),
                temp.getIssueDate(),
                temp.getDueDate(),
                temp.getReturnDate(),
                temp.getTitle()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JDialog bookListDialog = new JDialog(this, "Borrowing history", Dialog.ModalityType.APPLICATION_MODAL);
        bookListDialog.setLayout(new BorderLayout());
        bookListDialog.setSize(600, 400);
        bookListDialog.setLocationRelativeTo(this);
        bookListDialog.add(scrollPane, BorderLayout.CENTER);
        bookListDialog.setVisible(true);

        }
    }

    // Method to convert ResultSet to TableModel
    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int column = 1; column <= columnCount; column++) {
            columnNames[column - 1] = metaData.getColumnName(column);
        }

        // Get data
        Object[][] data = new Object[100][columnCount]; // Assuming a maximum of 100 rows
        int row = 0;
        while (resultSet.next() && row < 100) {
            for (int column = 1; column <= columnCount; column++) {
                data[row][column - 1] = resultSet.getObject(column);
            }
            row++;
        }

        return new DefaultTableModel(data, columnNames);
    }

    
}
