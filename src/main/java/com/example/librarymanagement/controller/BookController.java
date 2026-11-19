
package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.repository.BookRepository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        if (book.getId() == null ||
            book.getTitle() == null || book.getTitle().isBlank() ||
            book.getAuthor() == null || book.getAuthor().isBlank() ||
            book.getCategory() == null || book.getCategory().isBlank() ||
            book.getAvailableCopies() == null ||
            book.getAvailableCopies() < 0) {

            return ResponseEntity.badRequest()
                    .body("Please provide valid book details.");
        }

        try {
            bookRepository.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Book added successfully.");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A book with this ID already exists.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepository.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        Book book = bookRepository.getBookById(id);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found.");
        }

        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable int id,
            @RequestBody Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank() ||
            book.getAuthor() == null || book.getAuthor().isBlank() ||
            book.getCategory() == null || book.getCategory().isBlank() ||
            book.getAvailableCopies() == null ||
            book.getAvailableCopies() < 0) {

            return ResponseEntity.badRequest()
                    .body("Please provide valid book details.");
        }

        book.setId(id);

        int updated = bookRepository.updateBook(book);

        if (updated == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found.");
        }

        return ResponseEntity.ok("Book updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        int deleted = bookRepository.deleteBook(id);

        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found.");
        }

        return ResponseEntity.ok("Book deleted successfully.");
    }
}
