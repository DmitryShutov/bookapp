package ru.shutov.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@NamedEntityGraph(name = "Book.owner", attributeNodes = @NamedAttributeNode("owner"))
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "year")
    @Min(1800)
    private int year;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    private String author;

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "id")
    private Person owner;

    @Column(name = "assigned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;

    @Transient
    private Boolean expired;

    public Book() {}

    public Book(int id, String name,  String author, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.author = author;
    }

    public Book(int id, String name, String author, int year, Person owner) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.author = author;
        this.owner = owner;
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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public boolean hasPerson() {
        return this.owner != null;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
