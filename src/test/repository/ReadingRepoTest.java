package test.repository;

import main.model.Book;
import main.model.Reading;
import main.model.User;
import main.repository.BaseDataSource;
import main.repository.BookRepo;
import main.repository.ReadingRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ReadingRepoTest {
    BaseDataSource baseDataSource;

    @Mock
    User user;

    @Mock
    Book book;

    @Mock
    BookRepo bookRepo;

    @Before
    public void setUp() throws Exception {

        baseDataSource = new BaseDataSource();
        baseDataSource.setAutoCommit(false);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateReading() throws Exception {
        when(user.getUsername()).thenReturn("rajbharath");
        when(book.getName()).thenReturn("Refactoring");
        when(bookRepo.save(book)).thenReturn(true);

        ReadingRepo readingRepo = new ReadingRepo(baseDataSource, bookRepo);
        assertTrue("should create Reading failed", readingRepo.create(new Reading(user, book)));
    }

    @After
    public void tearDown() throws Exception {
        baseDataSource.rollback();
    }
}
