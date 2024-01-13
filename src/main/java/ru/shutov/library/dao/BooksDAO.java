package ru.shutov.library.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.shutov.library.models.Book;

import java.util.List;

@Component
public class BooksDAO {
    private final JdbcTemplate jdbcTemplate;

    public BooksDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES(?, ?, ?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public List<Book> getAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getOne(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book set name=?, author=?, year=?, person=? WHERE id=?",
                book.getName(), book.getAuthor(), book.getYear(), book.getPerson(), id);
    }

    public void updatePerson(Integer id, Book book) {
        jdbcTemplate.update("UPDATE book set person=? WHERE id=?", book.getPerson(), id);
    }

    public void deletePerson(Integer id, Book book) {
        jdbcTemplate.update("UPDATE book set person=? WHERE id=?", null, id);
    }

    public List<Book> findBooksByPersonId(int personId) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person=?", new Object[]{personId},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
