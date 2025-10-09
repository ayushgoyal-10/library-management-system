# Library Management System 📚

A complete Library Management System built with Java, JDBC, and MySQL implementing professional software architecture patterns.

## 🚀 Features

- **Book Management** - Add, search, update, and manage books
- **Member Management** - Register and manage library members
- **Borrowing System** - Issue and return books with transaction support
- **Fine Calculation** - Automatic fine calculation for overdue books
- **Search & Reports** - Search books and view borrowing history

## 🛠️ Tech Stack

- **Java** - Core application logic
- **JDBC** - Database connectivity and operations
- **MySQL** - Relational database management
- **DAO Pattern** - Data Access Object design pattern
- **Layered Architecture** - Separation of concerns

## 🏗️ Project Architecture
LibraryApp (Main Class)
↓
LibraryService (Business Logic Layer)
↓
BookDAO MemberDAO BorrowingDAO (Data Access Layer)
↓
MySQL Database


## 📊 Database Schema

- **books** - Stores book information and availability
- **members** - Stores member details and status
- **borrowings** - Tracks book borrowing/returning with fines

## 🚀 Getting Started

1. Import the project in IDE
2. Set up MySQL database
3. Update database credentials
4. Run `LibraryApp.java`

*See database/schema.sql for database setup.*