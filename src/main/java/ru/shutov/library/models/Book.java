package ru.shutov.library.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class Book {
    private int id;


    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Min(1800)
    private int year;
    @NotEmpty(message = "Author should not be empty")
    private String author;

    private Integer person;

    public Book() {}

    public Book(int id, String name,  String author, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.author = author;
    }

    public Book(int id, String name, String author, int year, Integer person) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.author = author;
        this.person = person;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public boolean hasPerson() {
        return this.person != null;
    }
}
