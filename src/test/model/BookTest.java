package test.model;

import main.model.Author;
import main.model.Book;
import main.model.Publisher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void shouldIncreaseIssuedCount() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 1);
        book.increaseIssuedCountByOne();
        assertEquals("Failed Increasing the issued Count", 0, book.getRemainingCount());
    }

    @Test
    public void shouldThrowExceptionIncreaseIssuedCountUnavailableBook() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Book is not available");
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 0);
        book.increaseIssuedCountByOne();
    }

    @Test
    public void shouldDecreaseIssuedCount() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 1);
        book.increaseIssuedCountByOne();
        book.decreaseIssuedCountByOne();
        assertEquals("Failed Increasing the issued Count", 1, book.getRemainingCount());
    }

    @Test
    public void shouldThrowExceptionDecreaseIssuedCountByOne() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Book has not been issued");
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        Book book = new Book("refactoring", authors, new Publisher("Addison"), 1);
        book.decreaseIssuedCountByOne();
        book.decreaseIssuedCountByOne();
    }
}
