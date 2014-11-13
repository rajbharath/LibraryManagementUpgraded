package main.model;

import java.util.List;
import java.util.stream.Collectors;

public class Book {
    private String name;
    private int noOfCopies;
    private List<Author> authors;
    private Publisher publisher;

    public Book(String name, List<Author> authors, Publisher publisher, int noOfCopies) {
        this.name = name;
        this.noOfCopies = noOfCopies;
        this.authors = authors;
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", noOfCopies=" + noOfCopies +
                ", authors=" + authors.stream().map(a -> a.getName()).collect(Collectors.joining(",")) +
                ", publisher=" + publisher.getName() +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (noOfCopies != book.noOfCopies) return false;
        if (!authors.equals(book.authors)) return false;

        return name.equals(book.name) && publisher.equals(book.publisher);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + noOfCopies;
        result = 31 * result + authors.hashCode();
        result = 31 * result + publisher.hashCode();
        return result;
    }

    public boolean isAvailable() {
        return getNoOfCopies() > 0;
    }


}
