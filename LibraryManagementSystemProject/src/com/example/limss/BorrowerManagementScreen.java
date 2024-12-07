package com.example.limss;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BorrowerManagementScreen extends JFrame {

    private JButton addButton;
    private JButton updateDeleteButton;
    private JButton searchButton;

    public BorrowerManagementScreen() {
        // Frame setup
        setTitle("Borrower Management");
        setSize(600, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 3, 10, 10)); // GridLayout for buttons
        
        // Initialize buttons
        addButton = new JButton("Add Borrower");
        updateDeleteButton = new JButton("Update/Delete Borrower");
        searchButton = new JButton("Search Borrower");

        // Add buttons to the frame
        add(addButton);
        add(updateDeleteButton);
        add(searchButton);

        // Event listeners for buttons
        addButton.addActionListener(e -> addBorrower());
        updateDeleteButton.addActionListener(e -> updateDeleteBorrower());
        searchButton.addActionListener(e -> searchBorrower());
    }

    public void addBorrower() {
        // Open a form to add a new borrower
        AddBorrowerForm addBorrowerForm = new AddBorrowerForm(this);
        addBorrowerForm.setVisible(true);
    }

    public void updateDeleteBorrower() {
        // Open a list of borrowers with options to edit or delete
        UpdateDeleteBorrowerForm updateDeleteBorrowerForm = new UpdateDeleteBorrowerForm(this);
        updateDeleteBorrowerForm.setVisible(true);
    }

    public void searchBorrower() {
        // Open a search dialog
        SearchBorrowerForm searchBorrowerForm = new SearchBorrowerForm(this);
        searchBorrowerForm.setVisible(true);
    }

    // Inner Class for adding a borrower
    class AddBorrowerForm extends JDialog {
        private JTextField nameField, emailField, borrowerIdField;
        private JFormattedTextField membershipDateField;
        private JButton submitButton;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public AddBorrowerForm(Frame owner) {
            super(owner, "Add Borrower", true);
            setSize(400, 200);
            setLayout(new GridLayout(5, 2, 10, 10)); // GridLayout for form fields
            setLocationRelativeTo(owner);
            
            nameField = new JTextField();
            emailField = new JTextField();
            borrowerIdField = new JTextField();
            borrowerIdField.setEnabled(false); // borrower_id is auto-incremented
            membershipDateField = new JFormattedTextField(dateFormat);
            membershipDateField.setValue(new Date()); // default to current date
            submitButton = new JButton("Submit");
            
            add(new JLabel("Name:"));
            add(nameField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Membership Date (YYYY-MM-DD):"));
            add(membershipDateField);
            add(submitButton);

            submitButton.addActionListener(e -> submitBorrowerInfo());
        }
        
        public void submitBorrowerInfo() {
            Borrower br= new Borrower();
            br.setName(nameField.getText());
            br.setEmail(emailField.getText());
            br.setMembershipDate(membershipDateField.getText());

            // Database operation to insert borrower information
            try {
                br.registerBorrower();
                JOptionPane.showMessageDialog(this, "Borrower added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }


    // Inner class for searching a borrower
    class SearchBorrowerForm extends JDialog {
        private JTextField searchField;
        private JButton searchButton;
        private JTable resultsTable;

        public SearchBorrowerForm(Frame owner) {
            super(owner, "Search Borrower", true);
            setSize(500, 300);
            setLayout(new BorderLayout());
            setLocationRelativeTo(owner);
            
            JPanel searchPanel = new JPanel();
            searchField = new JTextField(20);
            searchButton = new JButton("Search");
            searchPanel.add(new JLabel("Borrower ID:"));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);
            
            add(searchPanel, BorderLayout.NORTH);

            // Table for displaying search results
            resultsTable = new JTable();
            add(new JScrollPane(resultsTable), BorderLayout.CENTER);
            
            searchButton.addActionListener(e -> performSearch());
        }
        
        public void performSearch() {
            if (searchField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Borrower ID.");
                return;
            }

            try {
                Borrower borrower = Borrower.findBorrowerById(Integer.parseInt(searchField.getText()));
                if (borrower == null) {
                    JOptionPane.showMessageDialog(this, "Borrower not found");
                    return;
                }

                DefaultTableModel model = new DefaultTableModel(new String[]{"Borrower ID", "Name", "Email", "Membership Date"}, 0);    
                model.addRow(new Object[]{borrower.getBorrowerId(), borrower.getName(), borrower.getEmail(), borrower.getMembershipDate()});

                resultsTable.setModel(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }
        }
    }



    // Inner class for updating/deleting a borrower
class UpdateDeleteBorrowerForm extends JDialog {
    private JTextField borrowerIdField;
    private JButton updateButton, deleteButton;
    private UpdateBorrowerForm updateForm;

    public UpdateDeleteBorrowerForm(Frame owner) {
        super(owner, "Update/Delete Borrower", true);
        setSize(400, 200);
        setLayout(new FlowLayout());
        setLocationRelativeTo(owner);

        borrowerIdField = new JTextField(10);
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        add(new JLabel("Borrower ID:"));
        add(borrowerIdField);
        add(updateButton);
        add(deleteButton);

        updateButton.addActionListener(e -> openUpdateForm());
        deleteButton.addActionListener(e -> deleteBorrower());
    }

    public void openUpdateForm() {
        String borrowerIdStr = borrowerIdField.getText();
        if (borrowerIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Borrower ID.");
            return;
        }

        int borrowerId = Integer.parseInt(borrowerIdStr);
        updateForm = new UpdateBorrowerForm(this, borrowerId);
        updateForm.setVisible(true);
    }

    public void deleteBorrower() {
        String borrowerIdStr = borrowerIdField.getText();
        if (borrowerIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Borrower ID.");
            return;
        }

        int borrowerId = Integer.parseInt(borrowerIdStr);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this borrower?");
        if (confirm == JOptionPane.YES_OPTION) {
            // Perform the delete operation
            String sql = "DELETE FROM borrowers WHERE borrower_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, borrowerId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Borrower deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No borrower found with the given ID.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage());
            }
        }
    }
}

    // Inner class for updating a borrower
    class UpdateBorrowerForm extends JDialog {
        private JTextField nameField, emailField;
        private JFormattedTextField membershipDateField;
        private JButton submitButton;
        private int borrowerId;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        public UpdateBorrowerForm(JDialog owner, int borrowerId) {
            super(owner, "Update Borrower", true);
            this.borrowerId = borrowerId;
            setSize(400, 200);
            setLayout(new GridLayout(4, 2, 10, 10));
            setLocationRelativeTo(owner);

            nameField = new JTextField();
            emailField = new JTextField();
            membershipDateField = new JFormattedTextField(dateFormat);
            submitButton = new JButton("Submit");

            add(new JLabel("Name:"));
            add(nameField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Membership Date (YYYY-MM-DD):"));
            add(membershipDateField);
            add(submitButton);

            // Load current borrower data
            loadBorrowerData();

            submitButton.addActionListener(e -> updateBorrowerInfo());
        }

        public void loadBorrowerData() {
            try {
                Borrower borrower = Borrower.findBorrowerById(borrowerId);
                if (borrower == null) {
                    JOptionPane.showMessageDialog(this, "Borrower not found");
                    return;
                }

                nameField.setText(borrower.getName());
                emailField.setText(borrower.getEmail());
                membershipDateField.setValue(java.sql.Date.valueOf(borrower.getMembershipDate()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }
        }

        public void updateBorrowerInfo() {
            String name = nameField.getText();
            String email = emailField.getText();
            String membershipDate = membershipDateField.getText();
            // Update the borrower information in the database

            Borrower borrower = new Borrower(borrowerId, name, email, membershipDate);
            try {
            borrower.updateBorrower();
            JOptionPane.showMessageDialog(this, "Borrower updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }


    

    
}
