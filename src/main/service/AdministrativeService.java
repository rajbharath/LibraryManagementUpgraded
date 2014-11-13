package main.service;

import main.model.*;
import main.repository.BookRepo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministrativeService {

    private final BookRepo bookRepo;

    public AdministrativeService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book addBook(User user, String name, List<String> authorNames, String publisherName, int noOfCopies) throws Exception {
        if (!user.isAuthorized(Permission.ADD_BOOK)) throw new Exception("User Not Authorized");
        List<Author> authors = new ArrayList<>();
        for (String authorName : authorNames) {
            authors.add(new Author(authorName));
        }
        Publisher publisher = new Publisher(publisherName);

        Book book = new Book(name, authors, publisher, noOfCopies);
        Book addedBook = bookRepo.addBook(book);
        return addedBook;

    }

    public boolean removeBook(User user, Book book) throws Exception {
        if (!user.isAuthorized(Permission.REMOVE_BOOK)) throw new Exception("User not authorized for this operation");
        return bookRepo.delete(book);
    }

    public List<Book> searchBookByName(String name) throws SQLException {
        return bookRepo.searchBookByName(name);
    }


}
