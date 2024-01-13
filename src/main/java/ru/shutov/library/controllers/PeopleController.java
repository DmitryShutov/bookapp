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


/**
 * @author Dmitry Shutov
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final BooksDAO booksDAO;

    public PeopleController(PersonDAO personDAO, BooksDAO booksDAO) {

        this.personDAO = personDAO;
        this.booksDAO = booksDAO;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        List<Book> books = booksDAO.findBooksByPersonId(id);
        model.addAttribute("books", booksDAO.findBooksByPersonId(id));
        return "people/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        if(bindingResult.hasErrors()) {
            return "people/edit";
        }

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}