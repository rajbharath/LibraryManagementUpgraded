package main.repository;

import main.model.Author;
import main.model.Book;
import main.model.Publisher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Book addBook(Book book) throws SQLException {
        Statement statement = connection.createStatement();

        StringBuilder authorIdsSql = new StringBuilder("");
        Integer[] authorIds = populateAuthorIds(book.getAuthors());
        int publisherId = populatePublisherId(book.getPublisher());
        for (int authorId : authorIds) {
            authorIdsSql.append(authorId);
            authorIdsSql.append(",");
        }

        authorIdsSql.replace(authorIdsSql.length() - 1, authorIdsSql.length(), "");
        String sql = "insert into book(name,author_ids,publisher_id,no_of_copies) values('" + book.getName() + "',ARRAY[" + authorIdsSql + "]," + publisherId + "," + book.getNoOfCopies() + ")";
        statement.executeUpdate(sql);

        return book;
    }

     public boolean delete(Book book) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from book where name='" + book.getName() + "'";
        int returnCode = statement.executeUpdate(sql);
        return returnCode == 1;
    }

    public List<Book> searchBookByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select name,author_ids,publisher_id,no_of_copies from book where lower(name) like'%" + name.toLowerCase() + "%'";
        ResultSet resultSet = statement.executeQuery(sql);
        List<Book> books = new ArrayList<>();
        while (!resultSet.isAfterLast()) {
            books.add(buildBookFromResultSet(resultSet));
        }
        return books;
    }

    private Book buildBookFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            java.sql.Array sqlArray = resultSet.getArray("author_ids");
            Object obj = sqlArray.getArray();
            int noOfCopies = resultSet.getInt("no_of_copies");

            Integer[] authorIds = (Integer[]) obj;
            int publisherId = resultSet.getInt("publisher_id");
            List<Author> authors = populateAuthors(authorIds);
            Publisher publisher = populatePublisher(publisherId);

            return new Book(name, authors, publisher, noOfCopies);

        }
        return null;
    }

    private int populatePublisherId(Publisher publisher) throws SQLException {
        int publisherId = publisherRepo.retrieveId(publisher.getName());
        if (publisherId == -1)
            publisherId = publisherRepo.create(publisher.getName());
        return publisherId;
    }

    private Integer[] populateAuthorIds(List<Author> authors) throws SQLException {
        List<String> authorNames = authors.stream().map(a -> a.getName()).collect(Collectors.toList());
        Integer[] authorIds = new Integer[authorNames.size()];
        int i = 0;
        for (String authorName : authorNames) {
            int id = authorRepo.retrieveId(authorName);
            if (id != -1)
                authorIds[i] = id;
            else
                authorIds[i] = authorRepo.create(authorName);
            i++;
        }
        return authorIds;
    }

    private List<Author> populateAuthors(Integer[] authorIds) throws SQLException {
        List<Author> authors = new ArrayList<>();
        for (Integer authorId : authorIds) {
            authors.add(authorRepo.retrieveAuthorById(authorId));

        }
        return authors;
    }

    private Publisher populatePublisher(int publisherId) throws SQLException {
        return publisherRepo.retrieveById(publisherId);
    }

}

