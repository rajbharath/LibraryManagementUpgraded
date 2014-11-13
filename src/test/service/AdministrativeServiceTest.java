package test.service;

import main.model.Book;
import main.repository.AuthorRepo;
import main.repository.BaseDataSource;
import main.repository.BookRepo;
import main.repository.PublisherRepo;
import main.service.AdministrativeService;
import org.junit.After;
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
import static org.mockito.Mockito.when;

public class AdministrativeServiceTest {
    @Mock
    private BookRepo bookRepo;
    @Mock
    private AuthorRepo authorRepo;
    @Mock
    private PublisherRepo publisherRepo;

    private BaseDataSource baseDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        baseDataSource = new BaseDataSource();
        baseDataSource.setAutoCommit(false);
    }

    @Test
    public void shouldAddBookWithValidBookDetails() throws Exception {
        when(bookRepo.addBook(any(Book.class))).thenReturn(1);
        AdministrativeService service = new AdministrativeService(bookRepo, authorRepo, publisherRepo);
        int addedBookId = service.addBook("P EAA", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        assertEquals("Should Add Book Failed. Expected : 1 , Got : " + addedBookId, 1, addedBookId);
    }

    @Test
    public void endToEndShouldAddBookWithValidBookDetails() throws Exception {
        BookRepo bookRepo = new BookRepo(baseDataSource);
        AuthorRepo authorRepo = new AuthorRepo(baseDataSource);
        PublisherRepo publisherRepo = new PublisherRepo(baseDataSource);
        AdministrativeService service = new AdministrativeService(bookRepo, authorRepo, publisherRepo);
        int addedBookId = service.addBook("Refactoring II", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        assertTrue("Should Add Book Failed. Expected : positive number , Got : " + addedBookId, addedBookId > 0);

    }

    @Test
    public void endToEndShouldRemoveBook() throws Exception {
        BookRepo bookRepo = new BookRepo(baseDataSource);
        AuthorRepo authorRepo = new AuthorRepo(baseDataSource);
        PublisherRepo publisherRepo = new PublisherRepo(baseDataSource);
        AdministrativeService service = new AdministrativeService(bookRepo, authorRepo, publisherRepo);
        int addedBookId = service.addBook("Refactoring II", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        assertTrue("Should Add Book Failed. Expected : positive number , Got : " + addedBookId, addedBookId > 0);
        boolean isDeleted = service.removeBook(new Book("Refactoring II", null, 0, 0));
        assertTrue("should Delete Book Failed. Expected: true Got: False", isDeleted);
    }

    @Test
    public void endToEndShouldSearchBooksByName() throws Exception {
        BookRepo bookRepo = new BookRepo(baseDataSource);
        AuthorRepo authorRepo = new AuthorRepo(baseDataSource);
        PublisherRepo publisherRepo = new PublisherRepo(baseDataSource);
        AdministrativeService service = new AdministrativeService(bookRepo, authorRepo, publisherRepo);
        int addedBookId = service.addBook("Refactoring II", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        assertTrue("Should Add Book Failed. Expected : positive number , Got : " + addedBookId, addedBookId > 0);
        List<Book> books = service.searchBookByName("Ref");
        List<Book> expected = new ArrayList<Book>();
        expected.add(new Book("Refactoring", null, 0, 0));
        assertEquals("should retrieve Book . Expected: List of Books Got: " + books.get(0).getName(), books.get(0).getName(), expected.get(0).getName());
    }

    //TODO
    @Test
    public void testSearchBookByAuthor() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        baseDataSource.rollback();

    }
}
