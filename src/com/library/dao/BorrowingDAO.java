package com.library.dao;

import com.library.model.Borrowing;

import java.util.List;

public interface BorrowingDAO {
    boolean borrowBook(int bookId, int memberId);
    boolean returnBook(int borrowId);
    List<Borrowing> getActiveBorrowings();
    List<Borrowing> getBorrowingHistory(int memberId);
    List<Borrowing> getOverdueBorrowings();
    double calculateFine(int borrowId);
}
