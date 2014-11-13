package main.model;

import java.util.Arrays;

public class Book {
    private String name;
    private Integer[] authorIds;
    private int publisherId;
    private int noOfCopies;

    public Book(String name, Integer[] authorIds, int publisherId, int noOfCopies) {
        this.name = name;
        this.authorIds = authorIds;
        this.publisherId = publisherId;
        this.noOfCopies = noOfCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (noOfCopies != book.noOfCopies) return false;
        if (publisherId != book.publisherId) return false;
        if (!Arrays.equals(authorIds, book.authorIds)) return false;
        if (!name.equals(book.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(authorIds);
        result = 31 * result + publisherId;
        result = 31 * result + noOfCopies;
        return result;
    }

    public void display() {
        System.out.println(name);
        System.out.println(publisherId);
        System.out.println(authorIds.toString());
        System.out.println(noOfCopies);

    }

    public Integer[] getAuthorIds() {
        return authorIds;
    }

    public String getName() {
        return name;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }
}
