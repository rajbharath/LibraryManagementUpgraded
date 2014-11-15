package main.repository;

import java.sql.SQLException;

public class RepoFactory {
    private static BaseDataSource baseDataSource;
    private static ReadingRepo readingRepo;
    private static BookRepo bookRepo;
    private static UserRepo userRepo;

    static{
        baseDataSource = new BaseDataSource();
    }

    public static ReadingRepo getReadingRepo() throws SQLException, ClassNotFoundException {
        if (readingRepo == null) readingRepo = new ReadingRepo(baseDataSource, getBookRepo());
        return readingRepo;
    }

    public static BookRepo getBookRepo() throws SQLException, ClassNotFoundException {
        if (bookRepo == null)
            bookRepo = new BookRepo(baseDataSource, new PublisherRepo(baseDataSource), new AuthorRepo(baseDataSource));
        return bookRepo;
    }

    public static UserRepo getUserRepo() throws SQLException, ClassNotFoundException {
        if (userRepo == null) userRepo = new UserRepo(baseDataSource);
        return userRepo;
    }
}
