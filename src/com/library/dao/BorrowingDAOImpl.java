package com.library.dao;

import com.library.model.Borrowing;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAOImpl implements  BorrowingDAO{

    @Override
    public boolean borrowBook(int bookId, int memberId) {
        Connection connection= null;
        try{
            connection= DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // transaction start= All or nothing

            String checkBookSQL= "SELECT quantity_available FROM books WHERE book_id= ?";
            // checking the quantity of the books, if they are available or not in db
            try(PreparedStatement checkStmt= connection.prepareStatement(checkBookSQL)){
                checkStmt.setInt(1, bookId);
                ResultSet resultSet = checkStmt.executeQuery();
                if(!resultSet.next() || resultSet.getInt("quantity_available") <=0){
                    System.out.println("The book is not available for borrowing : ");
                    connection.rollback();
                    return false;
                }
            }
            String checkMemberSQL= "SELECT status FROM members WHERE member_id= ?";
            // checking the member is active or not
            try(PreparedStatement checkStmt= connection.prepareStatement(checkMemberSQL)){
                checkStmt.setInt(1, memberId);
                ResultSet resultSet = checkStmt.executeQuery();
                if(!resultSet.next() || !resultSet.getString("status").equals("ACTIVE")){
                    System.out.println("Member cannot borrow books (inactive/suspended)");
                    connection.rollback();
                    return false;
                }
            }
            String updateBookSQL= "UPDATE books SET quantity_available= quantity_available -1 WHERE book_id= ?";
            // decreasing the book quantity by 1
            try(PreparedStatement updateStmt= connection.prepareStatement(updateBookSQL)){
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
            // creating borrowing record
            String insertBorrowSQL= "INSERT INTO borrowings(book_id, member_id, borrow_date, due_date) values(?,?,?,?)";
            try(PreparedStatement borrowStmt= connection.prepareStatement(insertBorrowSQL)){
                borrowStmt.setInt(1, bookId);
                borrowStmt.setInt(2, memberId);
                borrowStmt.setDate(3, Date.valueOf(LocalDate.now()));
                borrowStmt.setDate(4, Date.valueOf(LocalDate.now().plusWeeks(2)));

                borrowStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Book borrowed successfully!");
            return true;
        }catch (Exception e){
            try {
                if (connection != null) {
                    connection.rollback();
                }
            }catch (SQLException rollbackEx){
                System.out.println("Rollback failed : " + rollbackEx.getMessage());
            }
            System.out.println("Error borrowing book : " + e.getMessage());
            return false;
        } finally {
            try{
                if(connection!=null){
                    connection.setAutoCommit(true);
                    connection.close();
                }
            }catch (SQLException e){
                System.out.println("Error closing connection : " + e.getMessage());
            }
        }
    }

    @Override
    public boolean returnBook(int borrowId) {
        Connection connection = null;
        try{
            // starting transaction
            connection= DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            // get borrowing details
            String borrowSQL= "SELECT book_id, due_date FROM borrowings WHERE borrow_id= ? AND return_date IS NULL";
            int bookId;
            LocalDate dueDate;
            try(PreparedStatement getStmt= connection.prepareStatement(borrowSQL)){
                getStmt.setInt(1, borrowId);
                ResultSet resultSet = getStmt.executeQuery();
                if(!resultSet.next()){
                    System.out.println("No active borrowing found with ID : " + borrowId);
                    connection.rollback();
                    return false;
                }
                bookId= resultSet.getInt("book_id");
                dueDate= resultSet.getDate("due_date").toLocalDate();
            }
            // increase book quantity back
            String updateBookSQL= "UPDATE books SET quantity_available= quantity_available + 1 WHERE book_id= ?";
            try(PreparedStatement updateStmt= connection.prepareStatement(updateBookSQL)){
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
            // update borrowing record as returned
            String updateBorrowingSQL= "UPDATE borrowings SET return_date= ?, status= 'RETURNED' WHERE borrow_id= ?";
            try(PreparedStatement updateStmt= connection.prepareStatement(updateBorrowingSQL)){
                updateStmt.setDate(1, Date.valueOf(LocalDate.now()));
                updateStmt.setInt(2, borrowId);
                updateStmt.executeUpdate();
            }
            // calculate and apply fine if overdue
            double fine= 0.0;
            if(LocalDate.now().isAfter(dueDate)){
                long overDueDays= ChronoUnit.DAYS.between(dueDate, LocalDate.now());
                fine= overDueDays * 5.0; // 5rs per day fine
                String updateFine= "UPDATE borrowings SET fine_amount= ? WHERE borrow_id= ?";

                try(PreparedStatement fineStmt= connection.prepareStatement(updateFine)){
                    fineStmt.setDouble(1, fine);
                    fineStmt.setInt(2, borrowId);
                    fineStmt.executeUpdate();
                }
                System.out.println("Book returned late! Fine in rupees : " + fine);
            }
            // commit transaction
            connection.commit();
            System.out.println("Book returned successfully : " + (fine > 0 ? "Fine: rs" + fine : ""));
            return true;
        }catch(SQLException e){
            try {
                if (connection != null) {
                    connection.rollback();
                }
            }catch (SQLException rollbackEx){
                System.out.println("Rollback failed : " + rollbackEx.getMessage());
            }
            System.out.println("Error returning book : " + e.getMessage());
            return false;
        }finally {
            try {
                if(connection!=null){
                    connection.setAutoCommit(true);
                    connection.close();
                }
            }catch (SQLException e){
                System.out.println("Error closing conneciton : " + e.getMessage());
            }
        }
    }

    @Override
    public List<Borrowing> getActiveBorrowings() {
        List<Borrowing> borrowings= new ArrayList<>();
        String sql= "SELECT * FROM borrowings WHERE return_date IS NULL";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql);
        ResultSet resultSet= statement.executeQuery()){
            while(resultSet.next()){
                Borrowing borrowing= new Borrowing();
                borrowing.setBorrowId(resultSet.getInt("borrow_id"));
                borrowing.setBookId(resultSet.getInt("book_id"));
                borrowing.setMemberId(resultSet.getInt("member_id"));
                borrowing.setBorrowDate(resultSet.getDate("borrow_date").toLocalDate());
                borrowing.setDueDate(resultSet.getDate("due_date").toLocalDate());
                borrowing.setStatus(resultSet.getString("status"));

                borrowings.add(borrowing);
            }
            System.out.println("Found " + borrowings.size() + " active borrowings!");
        }catch (SQLException e){
            System.out.println("Error getting borrowings : " + e.getMessage());
            e.printStackTrace();
        }
        return borrowings;
    }

    @Override
    public List<Borrowing> getBorrowingHistory(int memberId) {
        List<Borrowing> history= new ArrayList<>();
        String sql= "SELECT * FROM borrowings WHERE member_id= ?";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement stmt= connection.prepareStatement(sql)){
            stmt.setInt(1, memberId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Borrowing borrowing= createBorrowingFromResultSet(resultSet);
                history.add(borrowing);
            }
        }catch(SQLException e){
            System.out.println("Error getting history : " + e.getMessage());
        }
        return history;
    }

    @Override
    public List<Borrowing> getOverdueBorrowings() {
        List<Borrowing> dueBorrowings= new ArrayList<>();
        String sql= "SELECT * FROM borrowings WHERE due_date < CURDATE() AND return_date IS NULL";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement stmt= connection.prepareStatement(sql)){
            ResultSet resultSet= stmt.executeQuery();
            while(resultSet.next()){
                Borrowing borrowing= createBorrowingFromResultSet(resultSet);
                dueBorrowings.add(borrowing);
            }
        }catch (SQLException e){
            System.out.println("Error getting overdue : " + e.getMessage());
        }
        return dueBorrowings;
    }

    private Borrowing createBorrowingFromResultSet(ResultSet resultSet) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowId(resultSet.getInt("borrow_id"));
        borrowing.setBookId(resultSet.getInt("book_id"));
        borrowing.setMemberId(resultSet.getInt("member_id"));
        borrowing.setBorrowDate(resultSet.getDate("borrow_date").toLocalDate());
        borrowing.setDueDate(resultSet.getDate("due_date").toLocalDate());
        borrowing.setStatus(resultSet.getString("status"));
        return borrowing;
    }

    @Override
    public double calculateFine(int borrowId) {
        return 0;
    }
}
