package com.example.limss;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReportsScreen extends JFrame {
    private JButton overdueBooksButton;
    private JButton popularBooksButton;
    private JTable reportsTable;
    private DefaultTableModel tableModel;

    public ReportsScreen() {
        // Frame setup
        setTitle("Reports Screen");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Button setup
        overdueBooksButton = new JButton("Overdue Books Report");
        popularBooksButton = new JButton("Popular Books Report");

        // Initialize the table with column names for Overdue Books
        String[] overdueColumns = {"ISBN", "Book ID", "Borrower ID", "Borrower Name", "Due Date"};
        tableModel = new DefaultTableModel(null, overdueColumns);
        reportsTable = new JTable(tableModel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(overdueBooksButton);
        buttonPanel.add(popularBooksButton);

        // Adding components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportsTable), BorderLayout.CENTER);

        // Event listeners for buttons
        overdueBooksButton.addActionListener(e -> generateOverdueBooksReport());
        popularBooksButton.addActionListener(e -> generatePopularBooksReport());

        // Automatically generate overdue books report on open
        generateOverdueBooksReport();
    }

    private void generateOverdueBooksReport() {
        // Reset to overdue books columns and clear previous report results
        String[] overdueColumns = {"ISBN", "Book ID", "Borrower ID", "Borrower Name", "Due Date"};
        tableModel.setColumnIdentifiers(overdueColumns);
        tableModel.setRowCount(0);

        try {
            ArrayList<Transaction> transactions = Transaction.findOverdueBooks();

            if (transactions == null) {
                JOptionPane.showMessageDialog(this, "No overdue books");
                return;
            }

            for (Transaction transaction : transactions) {
                Object[] rowData = new Object[]{
                    transaction.getIsbn(),
                    transaction.getBookId(),
                    transaction.getBorrowerId(),
                    transaction.getBorrowerName(),
                    transaction.getDueDate(),
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void generatePopularBooksReport() {
        // Reset to popular books columns and clear previous report results
        String[] popularBooksColumns = {"ISBN", "Book ID", "Title", "Author"};
        tableModel.setColumnIdentifiers(popularBooksColumns);
        tableModel.setRowCount(0);

        try {
            ArrayList<Transaction> transactions = Transaction.findPopularBooks();

            if (transactions == null) {
                JOptionPane.showMessageDialog(this, "No popular books");
                return;
            }

            for (Transaction transaction : transactions) {
                Object[] rowData = new Object[]{
                    transaction.getIsbn(),
                    transaction.getBookId(),
                    transaction.getTitle(),
                    transaction.getAuthor(),
                };
                tableModel.addRow(rowData);
            }
            tableModel.fireTableDataChanged();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

      

    }
}

