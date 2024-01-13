package ru.shutov.library.models;

import jakarta.validation.constraints.*;

/**
 * @author Dmitry Shutov
 */
public class Person {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30")
    private String name;

    @Min(value = 1900, message = "Birth year must be more than 1900")
    @Max(value = 2020, message = "You're so young")
    private int year;

    private int id;
    public Person() {}

    public Person(int id, String name, int year) {
        this.name = name;
        this.year = year;
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

}
