package test;

import main.model.User;
import main.service.LibraryService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Raj Bharath K on 11/9/14.
 */
public class LibraryServiceTest {
    @Test
    public void testAuthenticateValidCredentials() throws Exception {
        LibraryService service = new LibraryService();
        User user = service.authenticate("rbrajbharath", "123456");
        assertEquals(new User("rbrajbharath", "123456"), user);
    }

    @Test
    public void testReturnNullForInvalidCredentials() throws Exception {
        LibraryService service = new LibraryService();
        User user = null;
        user = service.authenticate("invalidusername", "invalidpassword");
        assertNull(user);
    }

    @Test(expected = Exception.class)
    public void testExceptionForMissingCredentials() throws Exception {
        LibraryService service = new LibraryService();
        User user = service.authenticate("", "");
    }
}
