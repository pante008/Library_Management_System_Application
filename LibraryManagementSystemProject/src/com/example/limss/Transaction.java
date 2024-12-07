package com.example.limss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int borrowerId;
    private String issueDate;
    private String dueDate;
    private String returnDate;
    private String borrowerName;
    private String isbn;
    private String title;
    private String author;


    // Default constructor
    public Transaction() {
    }

    // Parameterized constructor
    public Transaction(int transactionId, int bookId, int borrowerId, String issueDate, String dueDate,
            String returnDate, String borroweName, String isbn) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.borrowerName = borroweName;
        this.isbn = isbn;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }



    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    //method to issue book (interaction with database)
    public void issueBook() throws Exception {
        String sql = "INSERT INTO transactions (book_id, borrower_id, issue_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pstmt.setInt(1, this.bookId);
            pstmt.setInt(2, this.borrowerId);
            pstmt.setDate(3, java.sql.Date.valueOf(this.issueDate));
            pstmt.setDate(4, java.sql.Date.valueOf(this.dueDate));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
            } else {
                throw new Exception("Failed to perform transaction");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    //method to find issued books (interaction with database and return of arraylist of transactions)
    public static ArrayList<Transaction> findIssuedBooks() throws Exception {
        String sql = "SELECT tr.transaction_id, tr.book_id, bk.title, tr.borrower_id, br.name, tr.issue_date, tr.due_date, bk.isbn "
                +
                "FROM transactions tr " +
                "JOIN books bk ON tr.book_id = bk.book_id " +
                "JOIN borrowers br ON tr.borrower_id = br.borrower_id " +
                "WHERE tr.return_date IS NULL"; // Only select transactions where the book hasn't been returned

        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowerId(rs.getInt("borrower_id"));
                transaction.setBorrowerName(rs.getString("name"));
                transaction.setIssueDate(rs.getString("issue_date"));
                transaction.setDueDate(rs.getString("due_date"));
                transaction.setIsbn(rs.getString("isbn"));

                transactions.add(transaction);
            };
            return transactions;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    //  method to return book (interaction with database)
    public static void returnBookForTransactionId(int transactionID, String returnDate) throws Exception {
        String sql = "UPDATE transactions SET return_date = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, returnDate);
            pstmt.setInt(2, transactionID);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                conn.commit();
            } else {
                throw new Exception("Failed to return the book.");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    //method to find overdue books (interaction with database and return of arraylist of transactions)
    public static ArrayList<Transaction> findOverdueBooks() throws Exception {
        String sql = "SELECT bk.isbn, tr.book_id, br.borrower_id, br.name, tr.due_date " +
                     "FROM transactions tr " +
                     "JOIN books bk ON tr.book_id = bk.book_id " +
                     "JOIN borrowers br ON tr.borrower_id = br.borrower_id " +
                     "WHERE tr.due_date < CURDATE() AND tr.return_date IS NULL";

        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowerId(rs.getInt("borrower_id"));
                transaction.setBorrowerName(rs.getString("name"));
                transaction.setDueDate(rs.getString("due_date"));
                transaction.setIsbn(rs.getString("isbn"));

                transactions.add(transaction);
            };
            return transactions;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    //method to find popular books (interaction with database and return of arraylist of transactions)
    public static ArrayList<Transaction> findPopularBooks() throws Exception {
        String sql = "SELECT bk.isbn, bk.book_id, bk.title, bk.author, COUNT(tr.book_id) as borrow_count " +
                     "FROM transactions tr " +
                     "JOIN books bk ON tr.book_id = bk.book_id " +
                     "GROUP BY bk.book_id " +
                     "ORDER BY borrow_count DESC " +
                     "LIMIT 10";

        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setIsbn(rs.getString("isbn"));
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setTitle(rs.getString("title"));
                transaction.setAuthor(rs.getString("author"));

                transactions.add(transaction);
            };
            return transactions;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    //method to find transactions by borrower id
    public static ArrayList<Transaction> findTransactionsByBorrowerId(int borrowerId) throws Exception {
        String sql = "SELECT t.book_id, t.borrower_id, t.issue_date, t.due_date, t.return_date, b.title " +
                        "FROM transactions t " +
                        "JOIN books b ON t.book_id = b.book_id " +
                        "WHERE t.borrower_id = ?";

        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, borrowerId);

                    System.out.println(pstmt.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setBookId(rs.getInt("book_id"));
                transaction.setBorrowerId(rs.getInt("borrower_id"));
                transaction.setIssueDate(rs.getString("issue_date"));
                transaction.setDueDate(rs.getString("due_date"));
                transaction.setReturnDate(rs.getString("return_date"));
                transaction.setTitle(rs.getString("title"));

                transactions.add(transaction);
            };
            return transactions;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
