package com.library.dao;

import com.library.model.Book;

import java.util.List;

public interface BookDAO {
    void addBook(Book book);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    void updateBook(Book book);
    void deleteBook(int id);
    List<Book> searchBooks(String keyword);
}
