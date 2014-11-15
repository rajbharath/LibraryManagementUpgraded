package main;

import main.model.Book;
import main.model.User;
import main.repository.BaseDataSource;
import main.repository.DataSourceBuilder;
import main.service.*;
import main.util.IOUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private DataSourceBuilder dataSourceBuilder;
    private AdministrativeService administrativeService;
    private AuthenticationService authenticationService;
    private ReadingService readingService;
    private BookSearchService bookSearchService;

    User currentUser = null;
    Book selectedBook = null;
    boolean loggedOut = false;


    Client() {
        try {
            initializeSetup();
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void start() {
        while (!loggedOut) {
            if (isLoggedIn())
                IOUtil.println("Hi " + currentUser.getUsername());
            showOptions();
            int choice = getChoice();
            processChoice(choice);
        }
    }

    private void processChoice(int choice) {
        if (choice > 1 && choice < 8 && !isLoggedIn()) {
            IOUtil.println("Please login to do this operation");
            return;
        }
        switch (choice) {
            case 1:
                if (isLoggedIn()) {
                    IOUtil.println("Already logged in.");
                    return;
                }
                IOUtil.println("Logging in");
                IOUtil.println("Enter the username: ");
                String username = IOUtil.readString();
                IOUtil.println("Enter the password: ");
                String password = IOUtil.readString();

                try {
                    currentUser = authenticationService.authenticate(username, password);
                } catch (Exception e) {
                    IOUtil.println(e.getMessage());
                }
                break;
            case 2:

                IOUtil.println("Type the book name you want to search..: ");
                IOUtil.println("Enter the book name");
                String criteria = IOUtil.readString();
                try {
                    List<Book> books = bookSearchService.searchBookByName(criteria);
                    int index = 1;
                    for (Book book : books) {
                        IOUtil.println(index + " - " + book.toString());
                        index++;
                    }
                    IOUtil.println("0 - go back to main menu");
                    IOUtil.println("Select Book by its number.");
                    IOUtil.println("Choose any option: ");
                    int bookIndex = IOUtil.readInt();

                    while (bookIndex < 0 || bookIndex > books.size()) {
                        IOUtil.println(bookIndex + " Not a valid option");
                        bookIndex = IOUtil.readInt();
                    }
                    if (bookIndex == 0) break;
                    IOUtil.println("You have selected the below book");
                    IOUtil.println(books.get(bookIndex - 1).toString());
                    selectedBook = books.get(bookIndex - 1);
                } catch (SQLException e) {
                    IOUtil.println(e.getMessage());
                }
                break;
            case 3:

                if (!hasSelectedBook()) {
                    IOUtil.println("To borrow any book, you need to select a book searching by its name");
                    break;
                }
                try {
                    if (readingService.borrowBook(currentUser, selectedBook)) {
                        selectedBook = null;
                        IOUtil.println("Book has been borrowed successfully");
                    } else {
                        IOUtil.println("Processing Error..Please try again.");
                    }
                } catch (Exception e) {
                    IOUtil.println(e.getMessage());
                }

                break;
            case 4:

                if (!hasSelectedBook()) {
                    IOUtil.println("To return any book, you need to select a book searching by its name");
                    break;
                }
                try {
                    if (readingService.returnBook(currentUser, selectedBook)) {
                        selectedBook = null;
                        IOUtil.println("Returned the selected book successfully");
                    } else {
                        IOUtil.println("Processing Error..Please try again.");
                    }
                } catch (Exception e) {
                    IOUtil.println(e.getMessage());
                }
                break;
            case 5:
                IOUtil.println("Add more books to library");
                IOUtil.println("Enter the book name");
                String bookName = IOUtil.readLine();
                IOUtil.println("Enter the publisher name");
                String publisherName = IOUtil.readLine();
                IOUtil.println("Enter the no of copies");
                int noOfCopies = IOUtil.readInt();
                IOUtil.println("Enter the issued count");
                int issuedCount = IOUtil.readInt();
                IOUtil.println("Enter the authors' name(s). To stop giving the author names type: stop");

                List<String> authorNames = new ArrayList<>();
                String authorName = IOUtil.readLine();
                while (!authorName.equalsIgnoreCase("stop")) {
                    authorNames.add(authorName);
                    authorName = IOUtil.readLine();
                }
                try {
                    administrativeService.addBook(currentUser, bookName, authorNames, publisherName, noOfCopies, issuedCount);
                } catch (Exception e) {
                    IOUtil.println(e.getMessage());
                }
                break;
            case 6:
                if (!hasSelectedBook()) {
                    IOUtil.println("To remove any book, you need to select a book searching by its name");
                    break;
                }
                IOUtil.println("Remove books from library");
                try {
                    administrativeService.removeBook(currentUser, selectedBook);
                } catch (Exception e) {
                    IOUtil.println(e.getMessage());
                }
                break;
            case 7:
                IOUtil.println("Logging Out");
                logout();
                break;
            default:
                IOUtil.println("Invalid Option");
                break;

        }
    }

    private void logout() {
        loggedOut = true;
        currentUser = null;
    }

    private int getChoice() {
        int choice = IOUtil.readInt();
        while (choice < 1 || choice > 7) {
            IOUtil.println(choice + "is not a valid option. Please Enter again");
            choice = IOUtil.readInt();
        }
        return choice;
    }

    private void showOptions() {
        IOUtil.println("Welcome To the Library Management System");
        IOUtil.println("Choose any of the below options");
        IOUtil.println("1. Login");
        IOUtil.println("2. Search books by its name");
        IOUtil.println("3. Borrow the selected book");
        IOUtil.println("4. Return the selected book");
        IOUtil.println("5. Add more books to library");
        IOUtil.println("6. Remove books from library");
        IOUtil.println("7. Logout");
    }

    private void initializeSetup() throws SQLException, ClassNotFoundException {
        BaseDataSource baseDataSource = DataSourceBuilder.build("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/library_mgmt_upgraded", "postgres", "1");
        ServiceManager serviceManager = new ServiceManager(baseDataSource);
        administrativeService = serviceManager.getAdministrativeService();
        authenticationService = serviceManager.getAuthenticationService();
        readingService = serviceManager.getReadingService();
        bookSearchService = serviceManager.getBookSearchService();
    }

    private boolean isLoggedIn() {
        return currentUser != null;
    }

    private boolean hasSelectedBook() {
        return selectedBook != null;
    }

    public static void main(String[] a) {
        Client client = new Client();
    }
}
