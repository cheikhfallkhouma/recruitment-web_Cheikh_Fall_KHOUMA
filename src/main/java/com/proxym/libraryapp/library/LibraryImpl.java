package com.proxym.libraryapp.library;

import com.proxym.libraryapp.book.Book;
import com.proxym.libraryapp.book.BookRepository;
import com.proxym.libraryapp.member.Member;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor

public class LibraryImpl implements Library{
    public final BookRepository bookRepository;
   // private List<Book> borrowedBooks = new ArrayList<>();

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {

        if (hasLateBooks(member)) {
            throw new HasLateBooksException();
        }

        Book book = bookRepository.findBook(isbnCode);
        if (book != null) {
            // Borrow a book
            book.setBorrowedAt(borrowedAt);
            member.borrowBook(book);
            return book;
        }
        return null;
    }


    private boolean hasLateBooks(Member member) {
        //Verify if a member has a overdue books
        return false;
    }

    @Override
    public void returnBook(Book book, Member member) {
        LocalDate returnDate = LocalDate.now();
        int borrowedDays = (int) ChronoUnit.DAYS.between(book.getBorrowedAt(), returnDate);
        member.payBook(borrowedDays);
        book.setBorrowedAt(null);
        bookRepository.save(book);
    }
}
