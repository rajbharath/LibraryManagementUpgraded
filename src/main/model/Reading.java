package main.model;

import java.util.Date;

public class Reading {
    private static final String EFFECTIVE = "EFFECTIVE";
    private static final String OVER_DUE = "OVER_DUE";
    private static final String RETURNED = "RETURNED";
    private static final long RENTAL_PERIOD = 1000 * 60 * 60 * 24 * 15;

    private User user;
    private Book book;
    private Date borrowedDate;
    private Date dueDate;
    private Date returnedDate;
    private String status;

    public Reading(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowedDate = new Date(System.currentTimeMillis());
        this.dueDate = new Date(borrowedDate.getTime() + RENTAL_PERIOD);
    }

    @Override
    public String toString() {
        return "Reading{" +
                "username='" + user.getUsername() + '\'' +
                ", bookName='" + book.getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public String getStatus() {
        if (returnedDate != null) return RETURNED;
        if (dueDate.getTime() >= System.currentTimeMillis()) return EFFECTIVE;
        return OVER_DUE;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getUsername(){
        return user.getUsername();
    }

    public String getBookName(){
        return book.getName();
    }
}
