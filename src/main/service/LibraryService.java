package main.service;

import main.dao.UserDao;
import main.model.User;

/**
 * Created by ganeswari on 11/9/14.
 */
public class LibraryService {
    private UserDao userDao;

    public LibraryService() {
        userDao = new UserDao();
    }

    public User authenticate(String username, String password) throws Exception {
        if (username == null || username.length() == 0) {
            throw new Exception("Username should be entered");
        }
        if (password == null || password.length() == 0) {
            throw new Exception("Username should be entered");
        }
        User user = userDao.retrieveByUsername(username);

        if (user == null) return null;

        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }
}
