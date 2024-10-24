package com.proxym.libraryapp.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();

    public void saveAll(List<Book> books){
        for (Book book : books) {
            save(book);
        }
    }

    public void save(Book book){
        availableBooks.put(book.getIsbn(), book);
    }

    public Book findBook(long isbnCode) {
        return availableBooks.get(new ISBN(isbnCode));
    }
}
