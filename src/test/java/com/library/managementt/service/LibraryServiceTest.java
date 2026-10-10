package com.library.managementt.service;

import com.library.managementt.entity.*;
import com.library.managementt.repository.*;
import com.library.managementt.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
 @Mock BookRepository books; @Mock StudentRepository students; @Mock BookIssueRepository issues;
 LibraryService service;
 @BeforeEach void setup(){service=new LibraryService(books,students,issues);}
 @Test void issuingBookDecreasesAvailableQuantityAndSavesIssue(){Book b=new Book();b.setQuantity(3);b.setAvailableQuantity(2);Student s=new Student();when(books.findById(1L)).thenReturn(Optional.of(b));when(students.findById(2L)).thenReturn(Optional.of(s));when(issues.save(any())).thenAnswer(i->i.getArgument(0));LocalDate d=LocalDate.now();BookIssue result=service.issue(1L,2L,d,d.plusDays(14));assertEquals(1,b.getAvailableQuantity());assertEquals("ISSUED",result.getStatus());verify(books).save(b);verify(issues).save(result);}
 @Test void cannotIssueWhenNoCopiesAvailable(){Book b=new Book();b.setQuantity(1);b.setAvailableQuantity(0);when(books.findById(1L)).thenReturn(Optional.of(b));assertThrows(ConflictException.class,()->service.issue(1L,2L,LocalDate.now(),LocalDate.now().plusDays(1)));verifyNoInteractions(students);verify(issues,never()).save(any());}
 @Test void cannotIssueSameBookToStudentTwiceWhileFirstIssueIsActive(){Book b=new Book();b.setQuantity(2);b.setAvailableQuantity(2);when(books.findById(1L)).thenReturn(Optional.of(b));when(issues.existsByBook_IdAndStudent_IdAndStatus(1L,2L,"ISSUED")).thenReturn(true);assertThrows(ConflictException.class,()->service.issue(1L,2L,LocalDate.now(),LocalDate.now().plusDays(1)));assertEquals(2,b.getAvailableQuantity());verify(issues,never()).save(any());}
 @Test void rejectsDueDateBeforeIssueDate(){assertThrows(IllegalArgumentException.class,()->service.issue(1L,2L,LocalDate.now(),LocalDate.now().minusDays(1)));verifyNoInteractions(books,students,issues);}
 @Test void returningBookRestoresQuantityOnce(){Book b=new Book();b.setQuantity(2);b.setAvailableQuantity(1);BookIssue i=new BookIssue();i.setBook(b);i.setStatus("ISSUED");when(issues.findById(8L)).thenReturn(Optional.of(i));when(issues.save(i)).thenReturn(i);service.returnBook(8L);assertEquals(2,b.getAvailableQuantity());assertEquals("RETURNED",i.getStatus());assertNotNull(i.getReturnDate());assertThrows(ConflictException.class,()->service.returnBook(8L));verify(books, times(1)).save(b);}
}
