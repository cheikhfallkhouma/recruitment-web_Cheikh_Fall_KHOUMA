package com.proxym.libraryapp.book;

import com.proxym.libraryapp.Person.Resident;
import com.proxym.libraryapp.member.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter @Setter


/**
 * A simple representation of a book
 */
public class Book {
    String title;
    String author;
    ISBN isbn;
    LocalDate borrowedAt;

    public Book() {}

    public boolean isLate(Member member) {
        if (borrowedAt == null) return false;

        int allowedDays;
        if (member instanceof Resident) {
            allowedDays = 60; // Default value for the Residents
        } else {
            allowedDays = 30; // Default value for the Students
        }

        LocalDate dueDate = borrowedAt.plusDays(allowedDays);
        return LocalDate.now().isAfter(dueDate);
    }
}
