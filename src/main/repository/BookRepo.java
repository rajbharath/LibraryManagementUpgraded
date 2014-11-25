package main.repository;

import main.model.Author;
import main.model.Book;
import main.model.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepo {
    private Connection connection;
    private PublisherRepo publisherRepo;
    private AuthorRepo authorRepo;

    public BookRepo(BaseDataSource dataSource, PublisherRepo publisherRepo, AuthorRepo authorRepo) throws SQLException, ClassNotFoundException {
        connection = dataSource.getConnection();
        this.publisherRepo = publisherRepo;
        this.authorRepo = authorRepo;
    }

    public Book save(Book book) throws SQLException {

        Integer[] authorIds = populateAuthorIds(book.getAuthors());
        int publisherId = populatePublisherId(book.getPublisher());

        String sql = "insert into book(name,author_ids,publisher_id,no_of_copies,issued_count) values(?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, book.getName());
        statement.setArray(2, connection.createArrayOf("int", authorIds));
        statement.setInt(3, publisherId);
        statement.setInt(4, book.getTotalNoOfCopies());
        statement.setInt(5, book.getIssuedCount());
        statement.executeUpdate();
        return book;
    }

    public boolean update(Book book) throws SQLException {
        Integer[] authorIds = populateAuthorIds(book.getAuthors());
        int publisherId = populatePublisherId(book.getPublisher());
        Array authorIdsSql = connection.createArrayOf("int", authorIds);

        String sql = "update book set author_ids=? , publisher_id=?,no_of_copies=?,issued_count=? where lower(name)=?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setArray(1, authorIdsSql);
        statement.setInt(2, publisherId);
        statement.setInt(3, book.getTotalNoOfCopies());
        statement.setInt(4, book.getIssuedCount());
        statement.setString(5, book.getName().toLowerCase());

        return statement.executeUpdate() > 0;
    }

    public boolean delete(Book book) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from book where name='" + book.getName() + "'";
        int returnCode = statement.executeUpdate(sql);
        return returnCode == 1;
    }

    public Book findByName(String bookname) throws SQLException {
        String sql = "select name,author_ids,publisher_id,no_of_copies,issued_count from book where lower(name)=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, bookname.toLowerCase());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
            return buildBookFromResultSet(resultSet);
        return null;
    }

    public List<Book> findBooksByName(String name) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "select name,author_ids,publisher_id,no_of_copies,issued_count from book where lower(name) like'%" + name.toLowerCase() + "%'";

        ResultSet resultSet = statement.executeQuery(sql);
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            books.add(buildBookFromResultSet(resultSet));
        }
        return books;
    }

    private Book buildBookFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        java.sql.Array sqlArray = resultSet.getArray("author_ids");
        Object obj = sqlArray.getArray();
        int noOfCopies = resultSet.getInt("no_of_copies");

        Integer[] authorIds = (Integer[]) obj;
        int publisherId = resultSet.getInt("publisher_id");
        List<Author> authors = populateAuthors(authorIds);
        Publisher publisher = populatePublisher(publisherId);
        int issuedCount = resultSet.getInt("issued_count");
        return new Book(name, authors, publisher, noOfCopies);
    }

    private int populatePublisherId(Publisher publisher) throws SQLException {
        int publisherId = publisherRepo.findIdByName(publisher.getName());
        if (publisherId == -1)
            publisherId = publisherRepo.save(publisher.getName());
        return publisherId;
    }

    private Integer[] populateAuthorIds(List<Author> authors) throws SQLException {
        List<String> authorNames = authors.stream().map(a -> a.getName()).collect(Collectors.toList());
        Integer[] authorIds = new Integer[authorNames.size()];
        int i = 0;
        for (String authorName : authorNames) {
            int id = authorRepo.findIdByName(authorName);
            if (id != -1)
                authorIds[i] = id;
            else
                authorIds[i] = authorRepo.save(authorName);
            i++;
        }
        return authorIds;
    }

    private List<Author> populateAuthors(Integer[] authorIds) throws SQLException {
        List<Author> authors = new ArrayList<>();
        for (Integer authorId : authorIds) {
            authors.add(authorRepo.findById(authorId));

        }
        return authors;
    }

    private Publisher populatePublisher(int publisherId) throws SQLException {
        return publisherRepo.findById(publisherId);
    }


}

