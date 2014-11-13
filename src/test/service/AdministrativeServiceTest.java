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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddBookWithValidBookDetails() throws Exception {

        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Martin Fowler"));
        authors.add(new Author("fowler"));
        Book expectedBook = new Book("P EAA", authors, new Publisher("Addison-Wesly"), 5);

        when(bookRepo.addBook(any(Book.class)))
                .thenReturn(expectedBook);

        when(user.isAuthorized(Permission.ADD_BOOK)).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);

        Book book = service.addBook(user, "P EAA", Arrays.asList(new String[]{"Martin Fowler", "fowler"}), "Addison-Wesly", 5);
        verify(user).isAuthorized(Permission.ADD_BOOK);
        assertEquals("Should Add Book Failed.", expectedBook, book);
    }


    @Test
    public void shouldRemoveBook() throws Exception {
        when(user.isAuthorized(Permission.REMOVE_BOOK)).thenReturn(true);
        when(bookRepo.delete(any(Book.class))).thenReturn(true);
        AdministrativeService service = new AdministrativeService(bookRepo);
        boolean isDeleted = service.removeBook(user, new Book("Refactoring II", null, null, 0));
        assertTrue("should Delete Book Failed. Expected: true Got: False", isDeleted);
    }

    
}
