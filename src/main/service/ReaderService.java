package main.service;

import main.model.Book;
import main.model.Permission;
import main.model.Reading;
import main.model.User;
import main.repository.ReadingRepo;


public class ReaderService {

    private final ReadingRepo readingRepo;


    public ReaderService(ReadingRepo readingRepo) {
        this.readingRepo = readingRepo;
    }

    public boolean borrowBook(User user, Book book) throws Exception {
        if (!book.isAvailable()) throw new Exception("Book Not available");
        if (!user.isAuthorized(Permission.BORROW_BOOK)) throw new Exception("User not authorized to borrow book");

        book.increaseIssuedCountByOne();
        Reading reading = new Reading(user, book);

        return readingRepo.create(reading);
    }

}
