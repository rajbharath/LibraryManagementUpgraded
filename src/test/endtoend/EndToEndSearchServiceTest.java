package test.endtoend;

import main.model.Book;
import main.model.Permission;
import main.repository.RepoFactory;
import main.service.SearchService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EndToEndSearchServiceTest {

    @Test
    public void endToEndShouldSearchBooksByName() throws Exception {

        SearchService service  = new SearchService(RepoFactory.getBookRepo());
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.ADD_BOOK);
        permissions.add(Permission.SEARCH_BY_BOOKNAME);

        List<Book> books = service.searchBookByName("Ref");
        List<Book> expected = new ArrayList<>();
        expected.add(new Book("Refactoring", null, null, 0));
        assertEquals("should retrieve Book . Expected: List of Books Got: " + books.get(0).getName(), books.get(0).getName(), expected.get(0).getName());
    }
}
