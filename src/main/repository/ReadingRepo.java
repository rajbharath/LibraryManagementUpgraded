package main.repository;

import main.model.Book;
import main.model.Permission;
import main.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadingRepo {

    private static final long RENTAL_PERIOD = 1000 * 60 * 60 * 24 * 15;
    private static final String EFFECTIVE = "EFFECTIVE";
    private static final String OVER_DUE = "OVER_DUE";
    private static final String RETURNED = "RETURNED";
    private final Connection connection;
    private final BookRepo bookRepo;


    ReadingRepo(BaseDataSource baseDataSource, BookRepo bookRepo) throws SQLException, ClassNotFoundException {
        this.connection = baseDataSource.getConnection();
        this.bookRepo = bookRepo;
    }

    public boolean create(User user, Book book) throws Exception {
        if (!book.isAvailable()) throw new Exception("Book Not available");
        if (!user.isAuthorized(Permission.BORROW_BOOK)) throw new Exception("User not authorized to borrow book");

        String sql = "insert into reading(username,bookname,borrowed_date,due_date,status) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, book.getName());
        preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
        preparedStatement.setDate(4, new Date(System.currentTimeMillis() + RENTAL_PERIOD));
        preparedStatement.setString(5, EFFECTIVE);
        return preparedStatement.executeUpdate() > 0;
    }
}
