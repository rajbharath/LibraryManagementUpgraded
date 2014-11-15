package main.service;

import main.model.Book;
import main.model.Permission;
import main.model.Reading;
import main.model.User;
import main.repository.ReadingRepo;

import java.util.Date;


public class ReadingService {

    private final ReadingRepo readingRepo;


    public ReadingService(ReadingRepo readingRepo) {
        this.readingRepo = readingRepo;
    }

    public boolean borrowBook(User user, Book book) throws Exception {
        if (!book.isAvailable()) throw new Exception("Book Not available");
        if (!user.isAuthorized(Permission.BORROW_BOOK)) throw new Exception("User not authorized to borrow book");

        Reading reading = new Reading(user, book, new Date(System.currentTimeMillis()));
        book.increaseIssuedCountByOne();
        return readingRepo.create(reading);
    }

    public boolean returnBook(User user, Book book) throws Exception {
        if (!user.isAuthorized(Permission.RETURN_BOOK)) throw new Exception("User not authorized to return book");
        Reading reading = readingRepo.retrieve(user, book);
        if (reading == null) throw new Exception("User currently has no reading on the given book");
        book.decreaseIssuedCountByOne();
        reading.returnReading();
        return readingRepo.save(reading);
    }
}
