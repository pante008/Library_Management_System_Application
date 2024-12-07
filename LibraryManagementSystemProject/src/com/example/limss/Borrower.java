package com.example.limss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;

//import javax.swing.JOptionPane;
//import javax.swing.table.DefaultTableModel;

public class Borrower {

    private int borrowerId;
    private String name;
    private String email;
    private String membershipDate;

    public Borrower() {

    }

    public Borrower(int borrowerId, String name, String email, String membershipDate) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.email = email;
        this.membershipDate = membershipDate;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
    }

    public static Borrower findBorrowerById(int id) throws Exception {
        String sql = "SELECT * FROM borrowers WHERE borrower_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            Borrower borrower = new Borrower();
            while (rs.next()) {
                borrower = new Borrower(rs.getInt("borrower_id"), rs.getString("name"), rs.getString("email"),
                        rs.getDate("membership_date").toString());
            }
            return borrower;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void registerBorrower() throws Exception {
        String sql = "INSERT INTO borrowers (name, email, membership_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.name);
            pstmt.setString(2, this.email);
            pstmt.setDate(3, java.sql.Date.valueOf(this.membershipDate)); // Use java.sql.Date here
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
            } else {
                throw new Exception("Could not add borrower.");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void updateBorrower() throws Exception {
        String sql = "UPDATE borrowers SET name = ?, email = ?, membership_date = ? WHERE borrower_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // If your connection by default is set to auto-commit, turn it off

            pstmt.setString(1, this.name);
            pstmt.setString(2, this.email);
            pstmt.setDate(3, java.sql.Date.valueOf(this.membershipDate));
            pstmt.setInt(4, this.borrowerId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit(); // Commit the transaction
            } else {
                throw new Exception("Error updating borrower.");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    //public void deleteBorrower(Long borrowerId) {
        // TODO: Implement deleteBorrower method
    //}

    //public List<Borrower> findAllBorrowers() {
        // TODO: Implement findAllBorrowers method
      //  return null;
    //}
}
