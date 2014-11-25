package test.service;

import main.model.*;
import main.repository.AuthorRepo;
import main.repository.BaseDataSource;
import main.repository.BookRepo;
import main.repository.PublisherRepo;
import main.service.AdministrativeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdministrativeServiceTest {
    @Mock
    private BookRepo bookRepo;
    @Mock
    private AuthorRepo authorRepo;
    @Mock
    private PublisherRepo publisherRepo;

    @Mock
    User user;
    @Mock
    private BaseDataSource baseDataSource;

    private final List<Author> authors = Arrays.asList(new Author[]{new Author("Martin Fowler")});

    private final List<String> authorNames = Arrays.asList(new String[]{"Martin Fowler", "fowler"});

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddBookWithValidValues() throws Exception {

        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin Fowler"));
        authors.add(new Author("fowler"));
        Book expectedBook = new Book("P EAA", authors, new Publisher("Addison-Wesly"), 5);

        when(bookRepo.save(any(Book.class))).thenReturn(expectedBook);

        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);

        Book book = service.addBook(user, "P EAA", authorNames, "Addison-Wesly", 5);
        verify(user).isAuthorized(Permission.ADD_BOOK);
        assertEquals("Should Add Book Failed.", expectedBook, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToAddBookWithNegativeNoOfCopies() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, "Addison-Wesly", -1);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToAddBookWithZeroNoOfCopies() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, "Addison-Wesly", 0);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionUnauthorizedUserToAddBook() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(false);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNullUser() throws Exception {
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(null, "P EAA", authorNames, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNullBook() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNullAuthor() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", null, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNullBookName() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, null, authorNames, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionBookNameSpaces() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "   ", authorNames, "Addison-Wesly", 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForNotHavingPublisher() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, null, 5);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionPublisherNameSpaces() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.addBook(user, "P EAA", authorNames, "    ", 5);
    }

    @Test
    public void shouldRemoveBook() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        when(bookRepo.delete(any(Book.class))).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        boolean isDeleted = service.removeBook(user, new Book("P EAA", authors, new Publisher("Addison-Wesly"), 5));
        assertTrue("should Delete Book Failed. Expected: true Got: False", isDeleted);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveNullBook() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(user, null);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookUnauthorizedUser() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(false);
        when(bookRepo.delete(any(Book.class))).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(user, new Book("P EAA", authors, new Publisher("Addison-Wesly"), 5));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookUserIsNull() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book("P EAA", authors, new Publisher("Addison-Wesly"), 5));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookNullBookName() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book(null, authors, new Publisher("Addison-Wesly"), 5));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookWithBookNameSpaces() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book("         ", authors, new Publisher("Addison-Wesly"), 5));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookAuthorIsNull() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book("P EAA", null, new Publisher("Addison-Wesly"), 5));
    }


    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookPublisherIsNull() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book("P EAA", authors, null, 5));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToRemoveBookPublisherNameSpaces() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        service.removeBook(null, new Book("P EAA", authors, new Publisher("     "), 5));
    }
}
