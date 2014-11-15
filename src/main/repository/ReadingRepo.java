package main.repository;

import main.model.Reading;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadingRepo {


    private final Connection connection;
    private final BookRepo bookRepo;


    public ReadingRepo(BaseDataSource baseDataSource, BookRepo bookRepo) throws SQLException, ClassNotFoundException {
        this.connection = baseDataSource.getConnection();
        this.bookRepo = bookRepo;
    }

    public boolean create(Reading reading) throws Exception {
        boolean resultCode = true;
        String sql = "insert into reading(username,bookname,borrowed_date,due_date,status) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, reading.getUsername());
        preparedStatement.setString(2, reading.getBookName());
        preparedStatement.setDate(3, new Date(reading.getBorrowedDate().getTime()));
        preparedStatement.setDate(4, new Date(reading.getDueDate().getTime()));
        preparedStatement.setString(5, reading.getStatus());
        resultCode &= preparedStatement.executeUpdate() > 0;
        resultCode &= bookRepo.save(reading.getBook());
        return resultCode;
    }
}
