package com.library.managementt.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name="book_issues")
public class BookIssue {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.EAGER) @JoinColumn(nullable=false) private Book book;
    @ManyToOne(optional=false, fetch=FetchType.EAGER) @JoinColumn(nullable=false) private Student student;
    @Column(nullable=false) private LocalDate issueDate;
    @Column(nullable=false) private LocalDate dueDate;
    private LocalDate returnDate;
    @Column(nullable=false) private String status="ISSUED";
    public BookIssue(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Book getBook(){return book;} public void setBook(Book v){book=v;}
    public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public LocalDate getIssueDate(){return issueDate;} public void setIssueDate(LocalDate v){issueDate=v;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
    public LocalDate getReturnDate(){return returnDate;} public void setReturnDate(LocalDate v){returnDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
