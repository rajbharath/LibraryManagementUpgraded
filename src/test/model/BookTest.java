package test.model;

import main.model.Author;
import main.model.Book;
import main.model.Publisher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookTest {
    @Test
    public void shouldBeAvailable() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 5);
        assertTrue("should be available failed", book.isAvailable());
    }

    @Test
    public void shouldNotBeAvailable() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 0);
        assertFalse("should be available failed", book.isAvailable());
    }

    @Test
    public void shouldIncreaseIssuedCount() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 1);
        book.increaseIssuedCountByOne();
        assertFalse("should Increase issued count.", book.isAvailable());
    }
}
