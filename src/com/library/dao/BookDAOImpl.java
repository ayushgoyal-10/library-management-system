package com.library.dao;

import com.library.model.Book;
import com.library.util.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO{
    @Override
    public void addBook(Book book){
        String sql= "INSERT INTO books(title, author, isbn, genre, published_year, quantity_available) VALUES(?,?,?,?,?,?)";
        try (Connection connection= DatabaseConnection.getConnection();
             PreparedStatement statement= connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            // setting parameters for PreparedStatement
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPublishedYear());
            statement.setInt(6, book.getQuantityAvailable());

            // execute the insert
            int affectedRows= statement.executeUpdate();
            if(affectedRows > 0){
                // getting the auto generated book id
                try(ResultSet generatedKeys= statement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        book.setBook_id(generatedKeys.getInt(1));
                        System.out.println("Book added successfully with ID : " + book.getBook_id());
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Error adding book : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public Book getBookById(int id){
        Book book= null;
        String sql= "SELECT * FROM books WHERE book_id= ?";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){

            statement.setInt(1, id);
            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    book= new Book();
                    book.setBook_id(resultSet.getInt(1));
                    book.setTitle(resultSet.getString(2));
                    book.setAuthor(resultSet.getString(3));
                    book.setIsbn(resultSet.getString(4));
                    book.setGenre(resultSet.getString(5));
                    book.setPublishedYear(resultSet.getInt(6));
                    book.setQuantityAvailable(resultSet.getInt(7));

                    Timestamp createdTimeStamp= resultSet.getTimestamp(8);
                    if(createdTimeStamp!=null) {
                        book.setCreatedDate(createdTimeStamp.toLocalDateTime());
                    }
                    System.out.println("Found Book : " + book.getBook_id());
                }else{
                    System.out.println("No book found with ID : " + id);
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting book by ID: " + e.getMessage());
        }
        return book;
    }
    @Override
    public List<Book> getAllBooks(){
        List<Book> books= new ArrayList<>();
        String sql= "SELECT * FROM books ORDER BY book_id";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Book book= new Book();
                book.setBook_id(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setAuthor(resultSet.getString(3));
                book.setIsbn(resultSet.getString(4));
                book.setGenre(resultSet.getString(5));
                book.setPublishedYear(resultSet.getInt(6));
                book.setQuantityAvailable(resultSet.getInt(7));

                Timestamp createdTimeStamp= resultSet.getTimestamp(8);
                if(createdTimeStamp!=null) {
                    book.setCreatedDate(createdTimeStamp.toLocalDateTime());
                }
                books.add(book);
                System.out.println("DEBUG: Added book - " + book.getTitle());
            }
            System.out.println("Retrieved " + books.size() + " books from database");
        }catch (SQLException e){
            System.out.println("Error retreiving books : " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }
    @Override
    public void updateBook(Book book){
        String sql= "UPDATE books SET title= ?, author= ?, isbn= ?, genre= ?, published_year= ?, quantity_available= ? WHERE book_id= ?";

        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPublishedYear());
            statement.setInt(6, book.getQuantityAvailable());
            statement.setInt(7, book.getBook_id());

            int affectedRows= statement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Book updated successfully with ID : " + book.getBook_id());
            }else{
                System.out.println("No book fount with ID : " + book.getBook_id());
            }
        } catch (SQLException e) {
            System.out.println("Error updating book : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void deleteBook(int bookId){
        String sql= "DELETE FROM books WHERE book_id= ?";

        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, bookId);
            int affectedRows= statement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Book deleted successfully with ID : " + bookId);
            }else{
                System.out.println("No book found with ID : " + bookId);
            }
        }catch (SQLException e){
            System.out.println("Error deleting book : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public List<Book> searchBooks(String keyword){
        String sql= "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        List<Book> books= new ArrayList<>();
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            String searchPattern= "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try(ResultSet resultSet= statement.executeQuery()){
                while(resultSet.next()){
                    Book book= new Book();
                    book.setBook_id(resultSet.getInt(1));
                    book.setTitle(resultSet.getString(2));
                    book.setAuthor(resultSet.getString(3));
                    book.setIsbn(resultSet.getString(4));
                    book.setGenre(resultSet.getString(5));
                    book.setPublishedYear(resultSet.getInt(6));
                    book.setQuantityAvailable(resultSet.getInt(7));

                    Timestamp createdTimeStamp = resultSet.getTimestamp(8);
                    if (createdTimeStamp != null) {
                        book.setCreatedDate(createdTimeStamp.toLocalDateTime());
                    }
                    books.add(book);
                }
            }
            System.out.println("Found " + books.size() + " books matching: " + keyword);
        }catch(SQLException e){
            System.out.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    public void clearAllBooks() {
        String sql = "DELETE FROM books";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int rowsDeleted = statement.executeUpdate();
            System.out.println("Cleared " + rowsDeleted + " books from database");

            // Reset auto-increment (optional)
            String resetSql = "ALTER TABLE books AUTO_INCREMENT = 1";
            try (PreparedStatement resetStmt = connection.prepareStatement(resetSql)) {
                resetStmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error clearing books: " + e.getMessage());
        }
    }
}
