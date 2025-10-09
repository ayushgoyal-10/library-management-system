package com.library.model;

import java.time.LocalDateTime;

public class Book {
    private int book_id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int publishedYear;
    private int quantiyAvailable;
    private LocalDateTime createdDate;


    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getQuantityAvailable() {
        return quantiyAvailable;
    }

    public void setQuantityAvailable(int quantiyAvailable) {
        this.quantiyAvailable = quantiyAvailable;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Book() {
    }

    public Book(String title, String author, String isbn, String genre, int publishedYear, int quantiyAvailable) {
        this.title = title;
        this.book_id = book_id;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.quantiyAvailable = quantiyAvailable;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", publishedYear=" + publishedYear +
                ", quantiyAvailable=" + quantiyAvailable +
                ", createdDate=" + createdDate +
                '}';
    }
}