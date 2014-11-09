package main.dao;

import main.model.User;

import java.sql.*;

/**
 * Created by Raj Bharath K on 11/9/14.
 */
public class UserDao {
    private Connection connection;

    public UserDao() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "1");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // methods for different types of CRUD operations
    public User retrieveByUsername(String username) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            String sql = "select username,password from \"user\" where username='" + username + "'";
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildUserFromResultSet(resultSet);
    }

    private User buildUserFromResultSet(ResultSet resultSet) {
        User user = null;
        try {
            if (resultSet != null && resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                user = new User(username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
