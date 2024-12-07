package com.example.limss;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel of the Library Management Application
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {// Create and display the login screen
                    new LoginScreen().setVisible(true);
                }
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
