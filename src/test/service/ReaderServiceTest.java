package test.service;

import main.model.Book;
import main.model.User;
import main.repository.ReadingRepo;
import main.service.ReaderService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ReaderServiceTest {

    @Mock
    ReadingRepo readingRepo;

    @Mock
    User user;

    @Mock
    Book book;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBorrowBook() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("P EAA");
        ReaderService service = new ReaderService(readingRepo);
        assertTrue("should borrow book got failed", service.borrowBook(user, book));

    }

    @After
    public void tearDown() throws Exception {

    }
}
