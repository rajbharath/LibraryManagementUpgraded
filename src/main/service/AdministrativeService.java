package main.service;

import main.model.Book;
import main.repository.AuthorRepo;
import main.repository.BookRepo;
import main.repository.PublisherRepo;

import java.sql.SQLException;
import java.util.List;

public class AdministrativeService {

    private final BookRepo bookRepo;
    private final PublisherRepo publisherRepo;
    private final AuthorRepo authorRepo;

    public AdministrativeService(BookRepo bookRepo, AuthorRepo authorRepo, PublisherRepo publisherRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
    }

    public int addBook(String name, List<String> authorNames, String publisherName, int noOfCopies) throws SQLException {

        Integer[] authorIds = populateAuthorIds(authorNames);
        int publisherId = populatePublisherId(publisherName);

        Book book = new Book(name, authorIds, publisherId, noOfCopies);
        int addedBookId = -1;

        try {
            addedBookId = bookRepo.addBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return addedBookId;
        }

    }

    private int populatePublisherId(String publisherName) throws SQLException {
        int publisherId = publisherRepo.retrieveId(publisherName);
        if (publisherId == -1)
            publisherId = publisherRepo.create(publisherName);
        return publisherId;
    }

    private Integer[] populateAuthorIds(List<String> authorNames) throws SQLException {
        Integer[] authorIds = new Integer[authorNames.size()];

        int i = 0;
        for (String authorName : authorNames) {
            int id = authorRepo.retrieveId(authorName);
            if (id != -1)
                authorIds[i] = id;
            else
                authorIds[i] = authorRepo.create(authorName);
            i++;
        }
        return authorIds;
    }

    public boolean removeBook(Book book) throws SQLException {
        return bookRepo.delete(book);
    }

    public List<Book> searchBookByName(String name) throws SQLException {
        return bookRepo.retrieveByName(name);
    }
}
