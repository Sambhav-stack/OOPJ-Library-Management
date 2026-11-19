
package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> rowMapper = (rs, rowNum) ->
            new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("available_copies")
            );

    public int addBook(Book book) {
        String sql = "INSERT INTO books (id, title, author, category, available_copies) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableCopies());
    }

    public List<Book> getAllBooks() {
        return jdbcTemplate.query("SELECT * FROM books", rowMapper);
    }

    public Book getBookById(int id) {
        List<Book> books = jdbcTemplate.query(
                "SELECT * FROM books WHERE id = ?", rowMapper, id);
        return books.isEmpty() ? null : books.get(0);
    }

    public int updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, available_copies = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableCopies(),
                book.getId());
    }

    public int deleteBook(int id) {
        return jdbcTemplate.update(
                "DELETE FROM books WHERE id = ?", id);
    }
}
