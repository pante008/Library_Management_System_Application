package com.example.limss;

//import packages to be used
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class LoginScreen extends JFrame {
    // Define the login screen attributes
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;

    public LoginScreen() {
        setTitle("Library Management System");
        setSize(1200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add border layout to the frame
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Set background color of content pane
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 10)); // Add light green border
        
        // Create a panel for the image
        ImageIcon imageIcon = new ImageIcon("src/com/example/limss/download.jpeg");
        JLabel imageLabel = new JLabel(imageIcon);
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(240, 240, 240)); // Set background color of image panel
        imagePanel.add(imageLabel);
        
        // Create a panel for login components
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 5, 5)); // Adjusted grid layout
        loginPanel.setBackground(new Color(240, 240, 240)); // Set background color of login panel
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        
        // Add role selection buttons to the login panel
        JPanel roleSelectionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        roleComboBox = new JComboBox<>(new String[]{"Librarian", "Borrower"});
        roleSelectionPanel.add(new JLabel("Select Role: "));
        roleSelectionPanel.add(roleComboBox);
        loginPanel.add(roleSelectionPanel);
        loginPanel.add(new JLabel()); // Placeholder for empty space
        
        // Add labels and text fields to the login panel
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(lblUsername.getFont().deriveFont(Font.BOLD | Font.ITALIC)); // Bold and italic font
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(lblPassword.getFont().deriveFont(Font.BOLD | Font.ITALIC)); // Bold and italic font
        
        usernameField = new JTextField();
        usernameField.setBackground(new Color(255, 165, 0)); // Set orange background
        usernameField.setPreferredSize(new Dimension(150, 20)); // Set preferred size
        
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(255, 165, 0)); // Set orange background
        passwordField.setPreferredSize(new Dimension(150, 20)); // Set preferred size
        
        loginPanel.add(lblUsername);
        loginPanel.add(usernameField);
        loginPanel.add(lblPassword);
        loginPanel.add(passwordField);
        
        // Add login button to the login panel
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        loginPanel.add(buttonPanel);
        
        // Add components to the frame
        add(loginPanel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);
        
        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                if (authenticate(username, password, role)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    openDashboard(role, username);
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed!");
                }
            }
        });
    }

    public void openDashboard(String role, String username) {
        // Dispose of the login window
        dispose();

        // Based on the role, instantiate and display the appropriate dashboard
        if ("Librarian".equals(role)) {
            LibrarianDashboard librarianDashboard = new LibrarianDashboard(username);
            librarianDashboard.setVisible(true);
        } else if ("Borrower".equals(role)) {
            BorrowerDashboard borrowerDashboard = new BorrowerDashboard(username); // You will need to create this class
            borrowerDashboard.setVisible(true);
        }
    }

    public boolean authenticate(String username, String password, String role) {
        // You should encrypt the password and compare it with an encrypted version stored in the database.
        // For the sake of this example, plaintext is used.
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Error: " + ex.getMessage());
            return false;
        }
    }

  
}
