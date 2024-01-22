package ru.shutov.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shutov.library.models.Book;
import ru.shutov.library.models.Person;
import ru.shutov.library.repositories.BooksRepository;
import ru.shutov.library.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findByOwner(Person person) {
        List<Book> books = booksRepository.findByOwner(person);
        for (Book book : books) {
            book.setExpired(isExpired(book));
        }
        return books;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(Integer page, Integer itemsPerPage, Boolean sortByYear) {
        Sort sort = Sort.by("year");
        System.out.println(sortByYear);
        if (!sortByYear) {
            sort = Sort.unsorted();
        }
        int requestedPage = (page != null) ? page : 0;
        int requestedItemPerPage = (itemsPerPage != null) ? itemsPerPage : Integer.MAX_VALUE;
        PageRequest pageRequest = PageRequest.of(requestedPage, requestedItemPerPage, sort);
        return booksRepository.findAll(pageRequest).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()) {
            Book foundBook = book.get();
            foundBook.setExpired(isExpired(foundBook));
            return foundBook;
        }
        return null;
    }

    private boolean isExpired(Book book) {
        if (book.getAssignedAt() == null) {
            return false;
        }
        long diffInMillis = Math.abs(new Date().getTime() - book.getAssignedAt().getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diff > 10;
    }

    public List<Book> findByNameStartingWith(String search) {
        return booksRepository.findByNameStartingWith(search);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void assign(int bookId, Integer ownerId) {
        Book updatedBook = findOne(bookId);
        if (ownerId != null && updatedBook.getOwner() == null) {
            updatedBook.setAssignedAt(new Date());
        }
        Optional<Person> person = peopleRepository.findById(ownerId);
        person.ifPresent(updatedBook::setOwner);
        booksRepository.save(updatedBook);
    }
}
