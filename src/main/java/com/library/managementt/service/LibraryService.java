package com.library.managementt.service;

import com.library.managementt.entity.*;
import com.library.managementt.repository.*;
import com.library.managementt.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class LibraryService {
 private final BookRepository books; private final StudentRepository students; private final BookIssueRepository issues;
 public LibraryService(BookRepository b,StudentRepository s,BookIssueRepository i){books=b;students=s;issues=i;}
 public List<Book> books(String q){return q==null||q.isBlank()?books.findAll():books.search(q.trim());}
 public Book book(Long id){return books.findById(id).orElseThrow(()->new NotFoundException("Book not found: "+id));}
 @Transactional public Book saveBook(Book b,Long id){if(id==null){if(books.existsByIsbn(b.getIsbn()))throw new ConflictException("ISBN already exists");b.setAvailableQuantity(b.getQuantity());return books.save(b);}Book old=book(id);if(books.existsByIsbnAndIdNot(b.getIsbn(),id))throw new ConflictException("ISBN already exists");int issued=old.getQuantity()-old.getAvailableQuantity();if(b.getQuantity()<issued)throw new ConflictException("Quantity cannot be less than the number of copies currently issued");old.setTitle(b.getTitle());old.setAuthor(b.getAuthor());old.setIsbn(b.getIsbn());old.setCategory(b.getCategory());old.setPublisher(b.getPublisher());old.setPublicationYear(b.getPublicationYear());old.setQuantity(b.getQuantity());old.setAvailableQuantity(b.getQuantity()-issued);return books.save(old);}
 @Transactional public void deleteBook(Long id){Book b=book(id);if(issues.existsByBookIdAndStatus(id,"ISSUED"))throw new ConflictException("Return active copies before deleting this book");books.delete(b);}
 public List<Student> students(String q){return q==null||q.isBlank()?students.findAll():students.search(q.trim());}
 public Student student(Long id){return students.findById(id).orElseThrow(()->new NotFoundException("Student not found: "+id));}
 @Transactional public Student saveStudent(Student s,Long id){if(id==null){if(students.existsByStudentId(s.getStudentId()))throw new ConflictException("Student ID already exists");if(students.existsByEmail(s.getEmail()))throw new ConflictException("Email already exists");return students.save(s);}Student old=student(id);if(students.existsByStudentIdAndIdNot(s.getStudentId(),id))throw new ConflictException("Student ID already exists");if(students.existsByEmailAndIdNot(s.getEmail(),id))throw new ConflictException("Email already exists");old.setStudentId(s.getStudentId());old.setName(s.getName());old.setEmail(s.getEmail());old.setPhone(s.getPhone());old.setDepartment(s.getDepartment());return students.save(old);}
 @Transactional public void deleteStudent(Long id){Student s=student(id);if(issues.existsByStudentIdAndStatus(id,"ISSUED"))throw new ConflictException("Return active books before deleting this student");students.delete(s);}
 public List<BookIssue> issueHistory(String q,String status){return issues.search(q==null||q.isBlank()?null:q.trim(),status==null||status.isBlank()?null:status.toUpperCase(Locale.ROOT));}
 public List<BookIssue> activeIssues(){return issues.findByStatusOrderByIssueDateDesc("ISSUED");}
 @Transactional public BookIssue issue(Long bookId,Long studentId,LocalDate issueDate,LocalDate dueDate){if(issueDate==null||dueDate==null||dueDate.isBefore(issueDate))throw new IllegalArgumentException("Due date must be on or after issue date");Book b=book(bookId);if(b.getAvailableQuantity()<1)throw new ConflictException("No available copies of this book");if(issues.existsByBook_IdAndStudent_IdAndStatus(bookId,studentId,"ISSUED"))throw new ConflictException("This student already has an active issue for this book");Student s=student(studentId);b.setAvailableQuantity(b.getAvailableQuantity()-1);BookIssue i=new BookIssue();i.setBook(b);i.setStudent(s);i.setIssueDate(issueDate);i.setDueDate(dueDate);i.setStatus("ISSUED");books.save(b);return issues.save(i);}
 @Transactional public BookIssue returnBook(Long id){BookIssue i=issues.findById(id).orElseThrow(()->new NotFoundException("Issue not found: "+id));if(!"ISSUED".equals(i.getStatus()))throw new ConflictException("This issue has already been returned");Book b=i.getBook();if(b.getAvailableQuantity()>=b.getQuantity())throw new ConflictException("Book quantities are inconsistent; return was not recorded");i.setStatus("RETURNED");i.setReturnDate(LocalDate.now());b.setAvailableQuantity(b.getAvailableQuantity()+1);books.save(b);return issues.save(i);}
 public Map<String,Long> stats(){return Map.of("totalBooks",books.count(),"totalStudents",students.count(),"availableCopies",books.findAll().stream().mapToLong(Book::getAvailableQuantity).sum(),"issuedCopies",issues.countByStatus("ISSUED"));}
}
