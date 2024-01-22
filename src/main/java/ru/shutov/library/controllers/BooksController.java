package ru.shutov.library.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shutov.library.models.Book;
import ru.shutov.library.models.Person;
import ru.shutov.library.services.BooksService;
import ru.shutov.library.services.PeopleService;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String getAll(Model model,
                         @RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                         @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear) {
        if ((page != null && booksPerPage != null) || (sortByYear != null && sortByYear)) {
            System.out.println("here");
            model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
        } else {
            model.addAttribute("books", booksService.findAll());
        }
        return "books/index";
    }

    @GetMapping("/search")
    public String findByNameStartingWith(Model model,
                                         @RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            List<Book> books = booksService.findByNameStartingWith(search);
            System.out.println(books.get(0).getOwner().getName());
            model.addAttribute("books", books);
        }
        return "books/search";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,Model model) {

        Book book = booksService.findOne(id);

        model.addAttribute("book", book);

        if (book.getOwner() != null) {
           Person owner = book.getOwner();
           model.addAttribute("owner", owner);
        } else {
            List<Person> people = peopleService.findAll();
            model.addAttribute("people", people);
        }

        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book",booksService.findOne(id));
        return "books/edit";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int bookId, @RequestParam("owner") Integer ownerId) {
        Book book = booksService.findOne(bookId);
        if (book == null) {
            throw new EntityNotFoundException("Book with id " + bookId + " not found");
        }

        booksService.assign(bookId, ownerId);

        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/unassign")
    public String unassign(@PathVariable("id") int id) {
        Book book = booksService.findOne(id);
        if (book == null) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        book.setOwner(null);
        booksService.save(book);

        return "redirect:/books/" + id;
    }

}
