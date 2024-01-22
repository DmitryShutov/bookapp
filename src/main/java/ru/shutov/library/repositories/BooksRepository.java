package ru.shutov.library.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shutov.library.models.Book;
import ru.shutov.library.models.Person;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    @EntityGraph(value = "Book.owner", type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByNameStartingWith(String name);
}
