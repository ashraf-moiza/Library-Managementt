package com.library.managementt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "books")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false) private String title;
    @NotBlank @Column(nullable = false) private String author;
    @NotBlank @Column(nullable = false, unique = true) private String isbn;
    @NotBlank @Column(nullable = false) private String category;
    private String publisher;
    @Min(0) private Integer publicationYear;
    @Min(0) @Column(nullable = false) private int quantity;
    @Min(0) @Column(nullable = false) private int availableQuantity;

    public Book() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getAuthor(){return author;} public void setAuthor(String v){author=v;}
    public String getIsbn(){return isbn;} public void setIsbn(String v){isbn=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getPublisher(){return publisher;} public void setPublisher(String v){publisher=v;}
    public Integer getPublicationYear(){return publicationYear;} public void setPublicationYear(Integer v){publicationYear=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
    public int getAvailableQuantity(){return availableQuantity;} public void setAvailableQuantity(int v){availableQuantity=v;}
}
