package test.service;

import main.model.Book;
import main.model.Permission;
import main.model.Reading;
import main.model.User;
import main.repository.ReadingRepo;
import main.service.ReadingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReadingServiceTest {

    @Mock
    ReadingRepo readingRepo;

    @Mock
    User user;

    @Mock
    Book book;

    @Mock
    Reading reading;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBorrowBook() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.create(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should borrow book got failed", service.borrowBook(user, book));

        verify(book).isAvailable();
        verify(book).increaseIssuedCountByOne();
        verify(user).isAuthorized(Permission.BORROW_BOOK);
    }

    @Test
    public void shouldReturnBook() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath").thenReturn("rajbharath").thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);
        when(readingRepo.retrieve(user,book)).thenReturn(reading);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should return book failed", service.returnBook(user, book));

        verify(user).isAuthorized(Permission.RETURN_BOOK);
        verify(book).decreaseIssuedCountByOne();
        verify(reading).returnReading();

    }

}
