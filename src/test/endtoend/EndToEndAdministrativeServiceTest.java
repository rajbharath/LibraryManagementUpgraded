package test.endtoend;

import main.model.*;
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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class EndToEndAdministrativeServiceTest {
    @Mock
    private BookRepo bookRepo;
    @Mock
    private AuthorRepo authorRepo;
    @Mock
    private PublisherRepo publisherRepo;

    private BaseDataSource baseDataSource;

    @Mock
    User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        baseDataSource = new BaseDataSource();
        baseDataSource.setAutoCommit(false);
    }


    @Test
    public void endToEndShouldAddBookWithValidBookDetails() throws Exception {
        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AuthorRepo authorRepo = new AuthorRepo(baseDataSource);
        PublisherRepo publisherRepo = new PublisherRepo(baseDataSource);
        BookRepo bookRepo = new BookRepo(baseDataSource, publisherRepo, authorRepo);
        AdministrativeService service = new AdministrativeService(bookRepo);
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin Fowler"));
        authors.add(new Author("fowler"));
        Book expectedBook = new Book("P EAAA", authors, new Publisher("Addison-Wesly"), 5);
        Book book = service.addBook(user, "P EAAA", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        assertTrue("end To End Should Add Book With Valid Book Details failed", expectedBook.toString().equals(book.toString()));
    }




    @After
    public void tearDown() throws Exception {
        baseDataSource.rollback();

    }
}
