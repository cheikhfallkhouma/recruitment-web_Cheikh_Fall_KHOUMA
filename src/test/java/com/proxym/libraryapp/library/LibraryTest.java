package com.proxym.libraryapp.library;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proxym.libraryapp.Person.Resident;
import com.proxym.libraryapp.book.Book;
import com.proxym.libraryapp.book.BookRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.proxym.libraryapp.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proxym.libraryapp.Person.Student;


/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;
    private static List<Book> books;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new JavaTimeModule())
                .build();
        File booksJson = new File("src/test/resources/books.json");
        books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {});

        // Setup the library and book repository
        bookRepository = new BookRepository();
        bookRepository.saveAll(books);
        library = new LibraryImpl(bookRepository);
    }

    @Test
    void member_can_borrow_a_book_if_book_is_available() {
        //Assertions.fail("Implement me");
        // Arrange
        Member student = new Student(false, 20.0f); // Regular student
        Book availableBook = books.get(0);

        // Act
        Book borrowedBook = library.borrowBook(availableBook.getIsbn().getIsbnCode(), student, LocalDate.now());

        // Assert
        Assertions.assertNotNull(borrowedBook);
        Assertions.assertEquals(availableBook.getIsbn().getIsbnCode(), borrowedBook.getIsbn().getIsbnCode());

    }

    @Test
    void borrowed_book_is_no_longer_available() {
        //Assertions.fail("Implement me");
        // Arrange
        Member resident = new Resident(30.0f);
        Book availableBook = books.get(1);

        // Act
        library.borrowBook(availableBook.getIsbn().getIsbnCode(), resident, LocalDate.now());

        // Assert
        Book foundBook = bookRepository.findBook(availableBook.getIsbn().getIsbnCode());
        Assertions.assertNull(foundBook, "The borrowed book should no longer be available.");


    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        //Assertions.fail("Implement me");

        // Arrange
        Resident resident = new Resident(50.0f);
        Book book = books.get(2);
        LocalDate borrowedDate = LocalDate.now().minusDays(30); // Kept for 30 days

        // Act
        library.borrowBook(book.getIsbn().getIsbnCode(), resident, borrowedDate);
        library.returnBook(book, resident);

        // Assert
        Assertions.assertEquals(47.0f, resident.getWallet()); // Chargé 30 * 0.10

    }

    @Test
    void students_pay_10_cents_the_first_30days() {
        // Assertions.fail("Implement me");

        // Arrange
        Student student = new Student(false, 50.0f); // Regular student
        Book book = books.get(3);
        LocalDate borrowedDate = LocalDate.now().minusDays(30); // Exactly 30 days

        // Act
        library.borrowBook(book.getIsbn().getIsbnCode(), student, borrowedDate);
        library.returnBook(book, student);

        // Assert
        Assertions.assertEquals(47.0f, student.getWallet()); // Chargé 30 * 0.10
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        // Assertions.fail("Implement me");

        // Arrange
        Student firstYearStudent = new Student(true, 50.0f); // First year student
        Book book = books.get(4);
        LocalDate borrowedDate = LocalDate.now().minusDays(20); // Borrowed for 20 days

        // Act
        library.borrowBook(book.getIsbn().getIsbnCode(), firstYearStudent, borrowedDate);
        library.returnBook(book, firstYearStudent);

        // Assert
        Assertions.assertEquals(49.5f, firstYearStudent.getWallet()); // Chargé pour 5 jours * 0.10
    }

    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        //Assertions.fail("Implement me");

        // Arrange
        Resident resident = new Resident(50.0f);
        Book book = books.get(5);
        LocalDate borrowedDate = LocalDate.now().minusDays(70); // Borrowed for 70 days

        // Act
        library.borrowBook(book.getIsbn().getIsbnCode(), resident, borrowedDate);
        library.returnBook(book, resident);

        // Assert
        Assertions.assertEquals(45.0f, resident.getWallet()); // Chargé pour 60 * 0.10 + 10 * 0.20
    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books() {
        //Assertions.fail("Implement me");
        // Arrange
        Student student = new Student(false, 30.0f); // Regular student
        Book lateBook = books.get(6);
        LocalDate borrowedDate = LocalDate.now().minusDays(35); // Late book

        // Act
        library.borrowBook(lateBook.getIsbn().getIsbnCode(), student, borrowedDate);

        // Assert
        Assertions.assertThrows(HasLateBooksException.class, () -> {
            library.borrowBook(books.get(7).getIsbn().getIsbnCode(), student, LocalDate.now());
        });

    }
}
