package main.service;

import main.repository.RepoFactory;

import java.sql.SQLException;

public class ServiceManager {
    private static AdministrativeService administrativeService;
    private static AuthenticationService authenticationService;
    private static ReaderService readerService;
    private static SearchService searchService;

    public static AdministrativeService getAdministrativeService() throws SQLException, ClassNotFoundException {
        if (administrativeService == null) administrativeService = new AdministrativeService(RepoFactory.getBookRepo());
        return administrativeService;
    }

    public static AuthenticationService getAuthenticationService() throws SQLException, ClassNotFoundException {
        if (authenticationService == null) authenticationService = new AuthenticationService(RepoFactory.getUserRepo());
        return authenticationService;
    }

    public static ReaderService getReaderService() throws SQLException, ClassNotFoundException {
        if (readerService == null) readerService = new ReaderService(RepoFactory.getReadingRepo());
        return readerService;
    }

    public static SearchService getSearchService() throws SQLException, ClassNotFoundException {
        if (searchService == null) searchService = new SearchService(RepoFactory.getBookRepo());
        return searchService;
    }
}
