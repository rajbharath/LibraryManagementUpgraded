package main.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDataSource {
    String dbDriver;
    String dbUrl;
    String dbUser;
    String dbPassword;
    private boolean autoCommit = true;
    private Connection connection;

    public BaseDataSource(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        this.dbDriver = dbDriver;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName(getDbDriver());
        connection = DriverManager
                .getConnection(getDbUrl(),
                        getDbUser(), getDbPassword());
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (connection != null) connection.setAutoCommit(false);

        this.autoCommit = autoCommit;

    }

    public void rollback() throws SQLException {
        connection.rollback();
    }
}
