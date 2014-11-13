package main.repository;

import main.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepo {
    private final Connection connection;

    public BookRepo(BaseDataSource dataSource) throws SQLException, ClassNotFoundException {
        connection = dataSource.getConnection();
    }

    public int addBook(Book book) throws SQLException {
        Statement statement = connection.createStatement();
        int id = -1;

        StringBuilder authorIdsSql = new StringBuilder("");
        Integer[] authorIds = book.getAuthorIds();
        for (int authorId : authorIds) {
            authorIdsSql.append(authorId);
            authorIdsSql.append(",");
        }

        authorIdsSql.replace(authorIdsSql.length() - 1, authorIdsSql.length(), "");
        String sql = "insert into book(name,author_ids,publisher_id,no_of_copies) values('" + book.getName() + "',ARRAY[" + authorIdsSql + "]," + book.getPublisherId() + "," + book.getNoOfCopies() + ")";
        statement.executeUpdate(sql);
        sql = "select last_value from book_id_seq;";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    public Book retrieve(int addedBookId) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select name,author_ids,publisher_id,no_of_copies from book where id=" + addedBookId;
        ResultSet resultSet = statement.executeQuery(sql);
        return buildBookFromResultSet(resultSet);
    }

    private Book buildBookFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            java.sql.Array sqlArray = resultSet.getArray("author_ids");
            Object obj = sqlArray.getArray();
            Integer[] authorIds = (Integer[]) obj;
            int publisherId = resultSet.getInt("publisher_id");
            int noOfCopies = resultSet.getInt("no_of_copies");
            return new Book(name, authorIds, publisherId, noOfCopies);

        }
        return null;
    }

    public boolean delete(Book book) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from book where name='" + book.getName() + "'";
        int returnCode = statement.executeUpdate(sql);
        return returnCode == 1;
    }

    public List<Book> retrieveByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select name,author_ids,publisher_id,no_of_copies from book where lower(name) like'%" + name.toLowerCase() + "%'";
        ResultSet resultSet = statement.executeQuery(sql);
        List<Book> books = new ArrayList<Book>();
        while (!resultSet.isAfterLast()) {
            books.add(buildBookFromResultSet(resultSet));
        }
        return books;
    }
}
