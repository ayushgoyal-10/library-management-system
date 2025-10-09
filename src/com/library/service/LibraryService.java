package com.library.service;

import com.library.dao.*;
import com.library.model.Book;
import com.library.model.Borrowing;
import com.library.model.Member;

import java.time.LocalDate;
import java.util.List;

public class LibraryService {
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private BorrowingDAO borrowingDAO;

    public LibraryService(){
        this.bookDAO= new BookDAOImpl();
        this.memberDAO= new MemberDAOImpl();
        this.borrowingDAO= new BorrowingDAOImpl();
    }
    public boolean addBook(String title, String author, String isbn, String genre, int publishedYear, int quantity){
        // business rule 1: validate the input data
        if(title==null || title.trim().isEmpty()){
            System.out.println("Book title cannot be empty!");
            return false;
        }
        if(author==null || author.trim().isEmpty()){
            System.out.println("Author cannot be empty!");
            return false;
        }
        // business rule 2: validate quantity
        if(quantity< 0){
            System.out.println("Quantity cannot be negative!");
            return false;
        }
        // business rule 3: check if the isbn is already exists
        List<Book> books= bookDAO.searchBooks(isbn);
        if(!books.isEmpty()){
            System.out.println("Book with this ISBN already exists!");
            return false;
        }

        // if all validations pass, create and add the book
        Book book= new Book(title, author, isbn, genre, publishedYear, quantity);
        bookDAO.addBook(book);
        System.out.println("Book added successfully to library!");
        return true;
    }

    public boolean registerMember(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Member name cannot be empty!");
            return false;
        }

        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email format!");
            return false;
        }

        Member existingMember = memberDAO.getMemberByEmail(email);
        if (existingMember != null) {
            System.out.println("Member with this email already exists!");
            return false;
        }

        Member member = new Member(name, email, phone, LocalDate.now());
        memberDAO.addMember(member);
        System.out.println("Member registered successfully! ID: " + member.getMember_id());
        return true;
    }

    // 🎯 SERVICE METHOD: Borrow book with comprehensive validation
    public boolean borrowBook(int bookId, int memberId) {
        System.out.println("\n🔍 Processing book borrowing request...");

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID " + bookId + " not found!");
            return false;
        }

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println("Member with ID " + memberId + " not found!");
            return false;
        }

        if (book.getQuantityAvailable() <= 0) {
            System.out.println("Sorry, '" + book.getTitle() + "' is currently out of stock!");
            return false;
        }

        if (!"ACTIVE".equals(member.getStatus())) {
            System.out.println("Member '" + member.getName() + "' cannot borrow books. Status: " + member.getStatus());
            return false;
        }

        List<Borrowing> activeBorrowings = borrowingDAO.getActiveBorrowings();
        for (Borrowing borrowing : activeBorrowings) {
            if (borrowing.getMemberId() == memberId && borrowing.getBookId() == bookId) {
                System.out.println("Member already has this book borrowed!");
                return false;
            }
        }

        System.out.println("Validations passed! Processing transaction...");
        System.out.println("Book: " + book.getTitle());
        System.out.println("Member: " + member.getName());

        boolean success = borrowingDAO.borrowBook(bookId, memberId);

        if (success) {
            System.out.println("Book borrowed successfully!");
            System.out.println("Due date: " + java.time.LocalDate.now().plusWeeks(2));
        } else {
            System.out.println("Failed to borrow book due to system error!");
        }
        return success;
    }

    // 🎯 SERVICE METHOD: Return book with receipt and fine info
    public boolean returnBook(int borrowId) {
        System.out.println("\n🔍 Processing book return request...");

        // BUSINESS RULE 1: Check if borrowing record exists and is active
        List<Borrowing> activeBorrowings = borrowingDAO.getActiveBorrowings();
        Borrowing targetBorrowing = null;

        for (Borrowing borrowing : activeBorrowings) {
            if (borrowing.getBorrowId() == borrowId) {
                targetBorrowing = borrowing;
                break;
            }
        }

        if (targetBorrowing == null) {
            System.out.println("No active borrowing found with ID: " + borrowId);
            System.out.println("Tip: Check if book is already returned or ID is incorrect");
            return false;
        }

        // Get book and member details for nice receipt
        Book book = bookDAO.getBookById(targetBorrowing.getBookId());
        Member member = memberDAO.getMemberById(targetBorrowing.getMemberId());

        if (book == null || member == null) {
            System.out.println("Error: Book or member data corrupted!");
            return false;
        }

        // Show return summary before processing
        System.out.println("RETURN SUMMARY:");
        System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
        System.out.println("Member: " + member.getName());
        System.out.println("Borrowed on: " + targetBorrowing.getBorrowDate());
        System.out.println("Due date: " + targetBorrowing.getDueDate());
        System.out.println("Returning on: " + java.time.LocalDate.now());

        // Check if overdue and calculate fine
        if (java.time.LocalDate.now().isAfter(targetBorrowing.getDueDate())) {
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                    targetBorrowing.getDueDate(), java.time.LocalDate.now());
            double fine = daysOverdue * 5.0;
            System.out.println("⚠OVERDUE! " + daysOverdue + " days late");
            System.out.println("Fine amount: rs" + fine);
        } else {
            System.out.println("Returned on time - No fine!");
        }

        // Process the return transaction
        System.out.println("\n🔄 Processing return transaction...");
        boolean success = borrowingDAO.returnBook(borrowId);

        if (success) {
            System.out.println("Book returned successfully!");

            // Show updated book status
            Book updatedBook = bookDAO.getBookById(targetBorrowing.getBookId());
            System.out.println("'" + updatedBook.getTitle() + "' now has " +
                    updatedBook.getQuantityAvailable() + " copies available");
        } else {
            System.out.println("Failed to return book due to system error!");
        }
        return success;
    }


    public List<Book> searchBooks(String keyword) {
        System.out.println("Searching books for: " + keyword);
        return bookDAO.searchBooks(keyword);
    }

    public List<Book> getAllBooks() {
        System.out.println("Getting all books...");
        return bookDAO.getAllBooks();
    }

    public List<Member> getAllMembers() {
        System.out.println("Getting all members...");
        return memberDAO.getAllMembers();
    }

    public Member findMember(String email) {
        System.out.println("Finding member with email: " + email);
        return memberDAO.getMemberByEmail(email);
    }

    public List<Borrowing> viewActiveBorrowings() {
        System.out.println("Viewing active borrowings...");
        return borrowingDAO.getActiveBorrowings();
    }

    public List<Borrowing> viewMemberHistory(int memberId) {
        System.out.println("Viewing history for member ID: " + memberId);
        return borrowingDAO.getBorrowingHistory(memberId);
    }
}
