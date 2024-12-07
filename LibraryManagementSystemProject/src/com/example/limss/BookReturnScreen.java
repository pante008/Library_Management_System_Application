package com.example.limss;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookReturnScreen extends JFrame {
    private JTable booksTable;
    private JButton returnButton;
    private JTextField returnDateField;
    private DefaultTableModel tableModel;

    public BookReturnScreen() {
        // Frame setup
        setTitle("Books to be Returned");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        booksTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Transaction ID", "Book ID", "Borrower ID", "Borrower Name", "Issue Date", "Due Date", "ISBN"}, 0);
        booksTable.setModel(tableModel);
        loadBooksData(); // You need to implement this method to load data from the database
        
        // Layout setup
        setLayout(new BorderLayout());
        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        // Return date input
        JPanel returnPanel = new JPanel();
        returnDateField = new JTextField(10);
        returnPanel.add(new JLabel("Due Date:"));
        returnPanel.add(returnDateField);

        // Return button
        returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnSelectedBook());
        returnPanel.add(returnButton);
        
        add(returnPanel, BorderLayout.SOUTH);
    }

    public void loadBooksData() {
        tableModel.setRowCount(0);

        try {
            ArrayList<Transaction> transactions = Transaction.findIssuedBooks();

            if (transactions == null) {
                JOptionPane.showMessageDialog(this, "No returnable books found");
                return;
            }

            for (Transaction transaction : transactions) {
                Object[] rowData = new Object[]{
                    transaction.getTransactionId(),
                    transaction.getBookId(),
                    transaction.getBorrowerId(),
                    transaction.getBorrowerName(),
                    transaction.getIssueDate(),
                    transaction.getDueDate(),
                    transaction.getIsbn()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public void returnSelectedBook() {
        // Get the selected book from the table
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String returnDate = returnDateField.getText();
        Integer transactionID = (Integer) tableModel.getValueAt(selectedRow, 0); // Replace 0 with the actual column index for transaction_id

        try {
            Transaction.returnBookForTransactionId(transactionID, returnDate);
            loadBooksData();
        } catch (Exception ex) {
            loadBooksData();
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }


    // Main method for testing this screen standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookReturnScreen().setVisible(true);
        });
    }
}