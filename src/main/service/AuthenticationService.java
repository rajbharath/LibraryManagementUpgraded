package main.service;

import main.model.User;
import main.repository.UserRepo;

import java.sql.SQLException;

public class AuthenticationService {


    private final UserRepo userRepo;

    public AuthenticationService(UserRepo userRepo) throws SQLException, ClassNotFoundException {
        this.userRepo = userRepo;
    }

    public User authenticate(String username, String password) throws Exception {
        if (username == null || username.length() == 0) {
            throw new Exception("Username should be entered");
        }
        if (password == null || password.length() == 0) {
            throw new Exception("Password should be entered");
        }
        User user = userRepo.retrieve(username, password);

        if (user == null) throw new Exception("User not found");

        return user;
    }

}
