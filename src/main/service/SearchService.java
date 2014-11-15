package main.service;

import main.model.Book;
import main.repository.BookRepo;

import java.sql.SQLException;
import java.util.List;

public class SearchService {
    private BookRepo bookRepo;

    public SearchService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> searchBookByName(String name) throws SQLException {
        return bookRepo.searchBookByName(name);
    }
}
