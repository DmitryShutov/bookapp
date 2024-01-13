package ru.shutov.library.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shutov.library.dao.BooksDAO;
import ru.shutov.library.dao.PersonDAO;
import ru.shutov.library.models.Book;
import ru.shutov.library.models.Person;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksDAO booksDAO;
    private final PersonDAO personDAO;

    public BooksController(BooksDAO booksDAO, PersonDAO personDAO) {
        this.booksDAO = booksDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getAll(Model model) {
        try {
            List<Book> allBooks = booksDAO.getAll();
            model.addAttribute("books", allBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {

        model.addAttribute("book", booksDAO.getOne(id));
        Book book = booksDAO.getOne(id);

        if (book.hasPerson()) {
            Person person = personDAO.show(book.getPerson());
            model.addAttribute("person", person);
        } else {
            List<Person> people = personDAO.index();
            model.addAttribute("people", people);
        }

        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksDAO.getOne(id));
        return "books/edit";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksDAO.save(book);

        return "redirect:/books";
    }

    @PatchMapping("{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksDAO.update(id, book);

        return "redirect:/books";
    }

    @PatchMapping("{id}/addPerson")
    public String addPerson(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        System.out.println("patch");
        booksDAO.updatePerson(id, book);
        return "redirect:/books/" + book.getId();
    }

    @DeleteMapping("{id}/addPerson")
    public String removePerson (@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        System.out.println("delete");
        booksDAO.deletePerson(id, book);
        return "redirect:/books/" + book.getId();
    }

}
