# Library Management System (LIMS)

## Description

Library Management System is a software application designed to manage the day-to-day operations of a library. This system can handle functions such as book issuing, return, popular book reports, and borrower management. The application is built using Java, Swing for GUI and MySQL for data persistence.

The scope of this project includes the management of book inventory, user registration, book lending, and returning processes. The system will also provide reporting details for overdue books and popular books. The scope does not include payment processing for late returns or damage, as these functions are handled manually by the library staff.

## Features

- Login Authentication
  - Librarian
  - Borrower 
- Book Management
  - Add books, update book , delete book, finalbooks and  find available books,
- Borrower Management
  - Register new borrowers, update their details, and delete borrower records, search borrower 
- Transaction Handling
  - Issue books to borrowers
  - Return books from borrowers
  - Keep track of overdue books
  - Generate reports on popular books and overdue items
- Book Issue Screen
  - Search Book, Search Borrower, Issue Books
- Book Return Screen
  - load books table, Return selected books
- Borrower Dashboard
  - viewAvailableBooks, viewBorrowingHistory


## Prerequisites

Before running the application, make sure you have the following installed:
- Java Development Kit (JDK) version 8 or above
- MySQL Server version 5.7 or above
- Install all the jar files required for the project (mysql connector jdbc)

## Installation

1. Clone the repository to your local machine using `git clone <repository-url>`.
2. Import the project into your favorite IDE, such as vscode, IntelliJ IDEA or Eclipse.
3. Configure the `DatabaseConnection` class with your MySQL credentials.

```java
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/LIMS_DB";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    // ...
}






1. Run the Main.java file to start the application.

Usage: After starting the application, you will be presented with a login screen. Use the following default credentials to log in:

- Librarian Access:
    Username: librarian(check DB)
    Password: librarian(check DB)
- Borrower Access:
    Username: borrower(check DB)
    Password: borrower(check DB)

Upon successful login, you will be directed to the respective dashboard, where you can navigate through different functionalities offered by the application.



To contribute to this project:

Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make changes and commit your changes (git commit -am 'Add some feature').
Push to the branch (git push origin feature-branch).
Create a new Pull Request.