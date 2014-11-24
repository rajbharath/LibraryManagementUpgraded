package test.service;

import main.model.Permission;
import main.model.User;
import main.repository.UserRepo;
import main.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @MockitoAnnotations.Mock
    private UserRepo userRepo;
    @MockitoAnnotations.Mock
    private List<Permission> permissions;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authenticateValidCredentials() throws Exception {
        when(userRepo.findByUsernameAndPassword("rbrajbharath", "123456")).thenReturn(new User("rbrajbharath", permissions));
        AuthenticationService service = new AuthenticationService(userRepo);
        User user = service.authenticate("rbrajbharath", "123456");
        assertEquals(new User("rbrajbharath", permissions), user);
    }

    @Test(expected = Exception.class)
    public void returnNullForInvalidCredentials() throws Exception {
        when(userRepo.findByUsernameAndPassword("invalidusername", "invalidpassword")).thenThrow(new Exception("User not found"));
        AuthenticationService service = new AuthenticationService(userRepo);
        service.authenticate("invalidusername", "invalidpassword");
    }

    @Test(expected = Exception.class)
    public void exceptionForMissingCredentials() throws Exception {
        when(userRepo.findByUsernameAndPassword("", "")).thenThrow(new Exception());
        AuthenticationService service = new AuthenticationService(userRepo);
        service.authenticate("", "");
    }


}
