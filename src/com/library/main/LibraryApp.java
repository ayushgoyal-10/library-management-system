package com.library.main;

import com.library.service.LibraryService;
import com.library.model.*;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private LibraryService libraryService;
    private Scanner scanner;

    public LibraryApp() {
        this.libraryService = new LibraryService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("🏛️  ================================");
        System.out.println("     WELCOME TO LIBRARY MANAGEMENT SYSTEM");
        System.out.println("🏛️  ================================\n");

        while (true) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            handleMenuChoice(choice);
        }
    }

    private void showMainMenu() {
        System.out.println("\n📋 MAIN MENU:");
        System.out.println("1. 📚 Book Management");
        System.out.println("2. 👥 Member Management");
        System.out.println("3. 📖 Borrow/Return Books");
        System.out.println("4. 📊 View Reports");
        System.out.println("5. ❌ Exit");
        System.out.println("----------------------------");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                bookManagementMenu();
                break;
            case 2:
                memberManagementMenu();
                break;
            case 3:
                borrowingMenu();
                break;
            case 4:
                reportsMenu();
                break;
            case 5:
                System.out.println("👋 Thank you for using Library Management System!");
                System.exit(0);
                break;
            default:
                System.out.println("❌ Invalid choice! Please try again.");
        }
    }

    // 📚 BOOK MANAGEMENT MENU
    private void bookManagementMenu() {
        while (true) {
            System.out.println("\n📚 BOOK MANAGEMENT:");
            System.out.println("1. ➕ Add New Book");
            System.out.println("2. 🔍 Search Books");
            System.out.println("3. 📖 View All Books");
            System.out.println("4. ↩️ Back to Main Menu");
            System.out.println("----------------------------");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addBookMenu();
                    break;
                case 2:
                    searchBooksMenu();
                    break;
                case 3:
                    viewAllBooks();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void addBookMenu() {
        System.out.println("\n➕ ADD NEW BOOK:");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        int publishedYear = getIntInput("Enter published year: ");
        int quantity = getIntInput("Enter quantity: ");

        libraryService.addBook(title, author, isbn, genre, publishedYear, quantity);
    }

    private void searchBooksMenu() {
        System.out.print("\n🔍 Enter search keyword: ");
        String keyword = scanner.nextLine();

        List<Book> books = libraryService.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("❌ No books found matching: " + keyword);
        } else {
            System.out.println("\n📚 SEARCH RESULTS:");
            for (Book book : books) {
                System.out.println("ID: " + book.getBook_id() + " | " + book.getTitle() +
                        " by " + book.getAuthor() + " | Available: " + book.getQuantityAvailable());
            }
        }
    }

    private void viewAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("❌ No books in library!");
        } else {
            System.out.println("\n📚 ALL BOOKS IN LIBRARY:");
            for (Book book : books) {
                System.out.println("ID: " + book.getBook_id() + " | " + book.getTitle() +
                        " by " + book.getAuthor() + " | Available: " + book.getQuantityAvailable());
            }
        }
    }

    // 👥 MEMBER MANAGEMENT MENU
    private void memberManagementMenu() {
        while (true) {
            System.out.println("\n👥 MEMBER MANAGEMENT:");
            System.out.println("1. ➕ Register New Member");
            System.out.println("2. 🔍 Find Member by Email");
            System.out.println("3. 📋 View All Members");
            System.out.println("4. ↩️ Back to Main Menu");
            System.out.println("----------------------------");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerMemberMenu();
                    break;
                case 2:
                    findMemberMenu();
                    break;
                case 3:
                    viewAllMembers();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void registerMemberMenu() {
        System.out.println("\n➕ REGISTER NEW MEMBER:");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        libraryService.registerMember(name, email, phone);
    }

    private void findMemberMenu() {
        System.out.print("\n🔍 Enter member email: ");
        String email = scanner.nextLine();

        Member member = libraryService.findMember(email);
        if (member == null) {
            System.out.println("❌ No member found with email: " + email);
        } else {
            System.out.println("\n✅ MEMBER FOUND:");
            System.out.println("ID: " + member.getMember_id() + " | Name: " + member.getName() +
                    " | Email: " + member.getEmail() + " | Status: " + member.getStatus());
        }
    }

    private void viewAllMembers() {
        List<Member> members = libraryService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("❌ No members registered!");
        } else {
            System.out.println("\n👥 ALL REGISTERED MEMBERS:");
            for (Member member : members) {
                System.out.println("ID: " + member.getMember_id() + " | " + member.getName() +
                        " | " + member.getEmail() + " | Status: " + member.getStatus());
            }
        }
    }

    // 📖 BORROWING MENU
    private void borrowingMenu() {
        while (true) {
            System.out.println("\n📖 BORROWING MANAGEMENT:");
            System.out.println("1. 📚 Borrow a Book");
            System.out.println("2. ↩️ Return a Book");
            System.out.println("3. 📋 View Active Borrowings");
            System.out.println("4. ↩️ Back to Main Menu");
            System.out.println("----------------------------");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    borrowBookMenu();
                    break;
                case 2:
                    returnBookMenu();
                    break;
                case 3:
                    viewActiveBorrowings();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void borrowBookMenu() {
        System.out.println("\n📚 BORROW A BOOK:");
        int bookId = getIntInput("Enter Book ID: ");
        int memberId = getIntInput("Enter Member ID: ");

        libraryService.borrowBook(bookId, memberId);
    }

    private void returnBookMenu() {
        System.out.println("\n↩️ RETURN A BOOK:");
        int borrowId = getIntInput("Enter Borrowing ID: ");

        libraryService.returnBook(borrowId);
    }

    private void viewActiveBorrowings() {
        List<Borrowing> borrowings = libraryService.viewActiveBorrowings();
        if (borrowings.isEmpty()) {
            System.out.println("✅ No active borrowings!");
        } else {
            System.out.println("\n📖 ACTIVE BORROWINGS:");
            for (Borrowing borrowing : borrowings) {
                System.out.println("Borrow ID: " + borrowing.getBorrowId() +
                        " | Book ID: " + borrowing.getBookId() +
                        " | Member ID: " + borrowing.getMemberId() +
                        " | Due: " + borrowing.getDueDate());
            }
        }
    }

    // 📊 REPORTS MENU
    private void reportsMenu() {
        while (true) {
            System.out.println("\n📊 REPORTS:");
            System.out.println("1. 👤 Member Borrowing History");
            System.out.println("2. ↩️ Back to Main Menu");
            System.out.println("----------------------------");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    memberHistoryMenu();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void memberHistoryMenu() {
        int memberId = getIntInput("Enter Member ID to view history: ");

        List<Borrowing> history = libraryService.viewMemberHistory(memberId);
        if (history.isEmpty()) {
            System.out.println("❌ No borrowing history for member ID: " + memberId);
        } else {
            System.out.println("\n📊 BORROWING HISTORY for Member ID: " + memberId);
            for (Borrowing borrowing : history) {
                System.out.println("Book ID: " + borrowing.getBookId() +
                        " | Borrowed: " + borrowing.getBorrowDate() +
                        " | Due: " + borrowing.getDueDate() +
                        " | Returned: " + (borrowing.getReturnDate() != null ? borrowing.getReturnDate() : "Not returned"));
            }
        }
    }

    // Utility method
    private int getIntInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number!");
            scanner.next();
            System.out.print(message);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return input;
    }

    public static void main(String[] args) {
        LibraryApp app = new LibraryApp();
        app.start();
    }
}