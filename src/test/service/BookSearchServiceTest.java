package test.service;

import main.repository.BookRepo;
import main.service.BookSearchService;
import org.junit.Test;
import org.mockito.Mock;

public class BookSearchServiceTest {
    @Mock
    private BookRepo bookRepo;

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWithSearchStringSpaces() throws Exception {
        BookSearchService service = new BookSearchService(bookRepo);
        service.searchBookByName("     ");

    }
}
