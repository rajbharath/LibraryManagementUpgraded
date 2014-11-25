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
    public void shouldBorrowBookWithValidUserAndBook() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should borrow book got failed", service.borrowBook(user, book));

        verify(book).isAvailable();
        verify(book).increaseIssuedCountByOne();
        verify(user).isAuthorized(Permission.BORROW_BOOK);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionUserIsNull() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);
        service.borrowBook(null, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForBookNotAvailable() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(false);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);
        service.borrowBook(user, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToBorrowBookForUnauthorizedUser() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(false);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);
        service.borrowBook(user, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToBorrowBookWithBookNameSpaces() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("   ");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        service.borrowBook(user, book);

    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToBorrowBookWithUserNameSpaces() throws Exception {
        when(user.getUsername()).thenReturn("    ");
        when(book.getName()).thenReturn("P EAA");
        when(book.isAvailable()).thenReturn(true);
        when(user.isAuthorized(Permission.BORROW_BOOK)).thenReturn(true);
        when(readingRepo.save(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        service.borrowBook(user, book);

    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionBothUserAndBookIsNull() throws Exception {
        ReadingService service = new ReadingService(readingRepo);
        service.borrowBook(null, null);

    }

    @Test
    public void shouldReturnValidBookForValidUser() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath").thenReturn("rajbharath").thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should return book failed", service.returnBook(user, book));

        verify(user).isAuthorized(Permission.RETURN_BOOK);
        verify(book).decreaseIssuedCountByOne();
        verify(reading).returnReading();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToReturnBookWithUserIsNull() throws Exception {
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);
        service.returnBook(null, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToReturnBookForUnauthorizedUser() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath").thenReturn("rajbharath").thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(false);
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should return book failed", service.returnBook(user, book));

        verify(user).isAuthorized(Permission.RETURN_BOOK);
        verify(book).decreaseIssuedCountByOne();
        verify(reading).returnReading();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToReturnBookWithBookNameSpaces() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath").thenReturn("rajbharath").thenReturn("rajbharath");
        when(book.getName()).thenReturn(" ").thenReturn(" ").thenReturn("  ");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        service.returnBook(user, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToReturnBookWithUserNameSpaces() throws Exception {
        when(user.getUsername()).thenReturn("    ").thenReturn("   ").thenReturn("   ");
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        service.returnBook(user, book);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionToReturnBookBothUserAndBookIsNull() throws Exception {
        ReadingService service = new ReadingService(readingRepo);
        service.returnBook(null, null);
    }

    @Test
    public void sample() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath").thenReturn("rajbharath").thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA").thenReturn("P EAA").thenReturn("P EAA");
        when(user.isAuthorized(Permission.RETURN_BOOK)).thenReturn(true);
        when(readingRepo.findByUserAndBook(user, book)).thenReturn(reading);
        when(readingRepo.update(any(Reading.class))).thenReturn(true);
        ReadingService service = new ReadingService(readingRepo);

        assertTrue("should return book failed", service.returnBook(user, book));

        verify(user).isAuthorized(Permission.RETURN_BOOK);
        verify(book).decreaseIssuedCountByOne();
        verify(reading).returnReading();
    }

}
