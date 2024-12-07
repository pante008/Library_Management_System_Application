package com.example.limss;

import java.awt.*;
import javax.swing.*;

public class LibrarianDashboard extends JFrame {

    public LibrarianDashboard(String librarianName) {
        // Frame setup
        setTitle("Librarian Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Border layout for splitting the frame
        getContentPane().setBackground(new Color(240, 240, 240)); // Set background color of content pane
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 10)); // Add light green border
        

        // Create left panel for librarian image
        ImageIcon librarianImage = new ImageIcon("src/com/example/limss/librarian.jpeg");
        JLabel librarianLabel = new JLabel(librarianImage);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(librarianLabel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Create right panel for buttons
        JPanel rightPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Grid layout for buttons
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome " + librarianName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16)); // Bold font
        rightPanel.add(welcomeLabel);

        // Buttons for different librarian actions
        JButton bookManagementButton = new JButton("Book Management");
        JButton borrowerManagementButton = new JButton("Borrower Management");
        JButton bookIssueButton = new JButton("Book Issued");
        JButton bookReturnButton = new JButton("Book Returned");
        JButton reportsButton = new JButton("Reports");

        // Add buttons to the right panel
        rightPanel.add(bookManagementButton);
        rightPanel.add(borrowerManagementButton);
        rightPanel.add(bookIssueButton);
        rightPanel.add(bookReturnButton);
        rightPanel.add(reportsButton);

        // Add the right panel to the frame
        add(rightPanel, BorderLayout.CENTER);

        // Event listeners for buttons
        bookManagementButton.addActionListener(e -> openBookManagement());
        borrowerManagementButton.addActionListener(e -> openBorrowerManagement());
        bookIssueButton.addActionListener(e -> openBookIssueScreen());
        bookReturnButton.addActionListener(e -> openBookReturnScreen());
        reportsButton.addActionListener(e -> openReportsScreen());
    }

    public void openBookManagement() {
        // Open the book management screen
        BookManagementScreen bookManagementScreen = new BookManagementScreen();
        bookManagementScreen.setVisible(true);
    }

    public void openBorrowerManagement() {
        // Open the borrower management screen
        BorrowerManagementScreen borrowerManagementScreen = new BorrowerManagementScreen();
        borrowerManagementScreen.setVisible(true);
    }

    public void openBookIssueScreen() {
        // Open the book issue screen
        BookIssueScreen bookIssueScreen = new BookIssueScreen();
        bookIssueScreen.setVisible(true);
    }

    public void openBookReturnScreen() {
        // Open the book return screen
        BookReturnScreen bookReturnScreen = new BookReturnScreen();
        bookReturnScreen.setVisible(true);
    }

    public void openReportsScreen() {
        // Open the reports screen
        ReportsScreen reportsScreen = new ReportsScreen();
        reportsScreen.setVisible(true);
    }

    
}
