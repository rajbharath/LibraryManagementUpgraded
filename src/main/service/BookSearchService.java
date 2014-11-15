package main.service;

import main.model.Book;
import main.repository.BookRepo;

import java.sql.SQLException;
import java.util.List;

public class BookSearchService {
    private BookRepo bookRepo;

    public BookSearchService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> searchBookByName(String name) throws SQLException {
        return bookRepo.searchBookByName(name);
    }
}
