package main.service;

import main.model.Book;
import main.model.User;
import main.repository.ReadingRepo;


public class ReaderService {

    private final ReadingRepo readingRepo;

    public ReaderService(ReadingRepo readingRepo) {
        this.readingRepo = readingRepo;
    }

    public boolean borrowBook(User user, Book book) throws Exception {
        return readingRepo.create(user, book);
    }
}
