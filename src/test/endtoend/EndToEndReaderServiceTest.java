package test.endtoend;

import main.model.*;
import main.repository.ReadingRepo;
import main.repository.RepoFactory;
import main.service.ReaderService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EndToEndReaderServiceTest {

    ReadingRepo readingRepo;
    @Mock
    User user;

    Book book;

    @Before
    public void setUp() throws Exception {
        readingRepo = RepoFactory.getReadingRepo();
        MockitoAnnotations.initMocks(this);
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin"));
        book = new Book("refactoring", authors, new Publisher("Addison"), 5, 0);
    }

    @Test
    public void shouldBorrowBook() throws Exception {

        when(user.getUsername()).thenReturn("rajbharath");
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);

        ReaderService service = new ReaderService(readingRepo);

        assertTrue("should borrow book but got failed", service.borrowBook(user, book));

        verify(user).isAuthorized(Permission.BORROW_BOOK);

    }

    @Test
    public void shouldReturnBorrowedBook() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);

        ReaderService service = new ReaderService(readingRepo);

        service.borrowBook(user, book);
        assertTrue("should return book but got failed", service.returnBook(user, book));

        verify(user).isAuthorized(Permission.BORROW_BOOK);
    }

    @After
    public void tearDown() throws Exception {

    }
}
