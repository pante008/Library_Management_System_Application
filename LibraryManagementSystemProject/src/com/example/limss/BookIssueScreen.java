package com.example.limss;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookIssueScreen extends JFrame {
    private JTextField searchBookField, searchBorrowerField;
    private JButton searchBookButton, searchBorrowerButton, issueButton;
    private JTable booksTable, borrowersTable;
    private DefaultTableModel booksTableModel, borrowersTableModel;
    private JFormattedTextField issueDateField, dueDateField;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BookIssueScreen() {
        // Frame setup
        setTitle("Book Issue Screen (Librarian)");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize the table with column names
        booksTableModel = new DefaultTableModel(new Object[]{"Book ID", "Title", "Author", "ISBN"}, 0);
        booksTable = new JTable(booksTableModel);
        booksTable.setPreferredScrollableViewportSize(new Dimension(450, 70));
        booksTable.setFillsViewportHeight(true);

        borrowersTableModel = new DefaultTableModel(new Object[]{"Borrower ID", "Name"}, 0);
        borrowersTable = new JTable(borrowersTableModel);
        borrowersTable.setPreferredScrollableViewportSize(new Dimension(450, 70));
        borrowersTable.setFillsViewportHeight(true);

        // Central panel with tables
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // divide the central part into two
        centerPanel.add(new JScrollPane(booksTable));
        centerPanel.add(new JScrollPane(borrowersTable));

        // Search fields and buttons
        JPanel searchPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        searchBookField = new JTextField();
        searchBorrowerField = new JTextField();
        searchBookButton = new JButton("Search Book");
        searchBorrowerButton = new JButton("Search Borrower");

        searchPanel.add(searchBookField);
        searchPanel.add(searchBookButton);
        searchPanel.add(searchBorrowerField);
        searchPanel.add(searchBorrowerButton);

        // Issue and date fields
        JPanel issuePanel = new JPanel();
        issueDateField = new JFormattedTextField(dateFormat);
        dueDateField = new JFormattedTextField(dateFormat);
        issueButton = new JButton("Issue Book");

        issuePanel.add(new JLabel("Issue Date:"));
        issuePanel.add(issueDateField);
        issuePanel.add(new JLabel("Due Date:"));
        issuePanel.add(dueDateField);
        issuePanel.add(issueButton);

        // Adding components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(issuePanel, BorderLayout.SOUTH);

        // Set the date fields to the current date as default
        issueDateField.setText(dateFormat.format(new Date()));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14); // Default due date is two weeks from now
        dueDateField.setText(dateFormat.format(c.getTime()));

        // Event listeners (Implement search logic in the methods)
        searchBookButton.addActionListener(e -> searchBook());
        searchBorrowerButton.addActionListener(e -> searchBorrower());
        issueButton.addActionListener(e -> issueBook());
    }

    // Methods searchBook, searchBorrower, issueBook (Implement these methods with the appropriate logic)
    private void searchBook() {

        // Clear previous search results
        booksTableModel.setRowCount(0);

        if (searchBookField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }

        try {
            Book book = Book.findBookByIdOrIsbn(searchBookField.getText().trim());
            if (book != null) {
                // And add it to the table model
                booksTableModel.addRow(new Object[]{book.getBookId(), book.getTitle(), book.getAuthor(), book.getIsbn()});
            } else {
                JOptionPane.showMessageDialog(this, "Book not found!");
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void searchBorrower() {
        // Clear previous search results
        borrowersTableModel.setRowCount(0);

        if (searchBorrowerField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }

        try {
            Borrower borrower = Borrower.findBorrowerById(Integer.parseInt(searchBorrowerField.getText().trim()));
            if (borrower == null) {
                JOptionPane.showMessageDialog(this, "Borrower not found");
                return;
            }

            borrowersTableModel.addRow(new Object[]{borrower.getBorrowerId(), borrower.getName()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
    }


    private void issueBook() {
        int selectedBookRow = booksTable.getSelectedRow();
        int selectedBorrowerRow = borrowersTable.getSelectedRow();

        if (selectedBookRow != -1 && selectedBorrowerRow != -1) {
            int bookId = (int) booksTableModel.getValueAt(selectedBookRow, 0);
            int borrowerId = (int) borrowersTableModel.getValueAt(selectedBorrowerRow, 0);
            String issueDate = issueDateField.getText();
            String dueDate = dueDateField.getText();

            Transaction transaction = new Transaction();
            transaction.setBookId(bookId);
            transaction.setBorrowerId(borrowerId);
            transaction.setIssueDate(issueDate);
            transaction.setDueDate(dueDate);

            try {
                transaction.issueBook();
                JOptionPane.showMessageDialog(this, "Book issued successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book and a borrower.");
        }
    }
    

   
}