package com.library.managementt.controller;
import com.library.managementt.entity.BookIssue; import com.library.managementt.service.LibraryService;
import jakarta.validation.constraints.NotNull; import org.springframework.format.annotation.DateTimeFormat; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.List;
@RestController @RequestMapping("/api/issues")
public class IssueController { private final LibraryService service; public IssueController(LibraryService s){service=s;}
 public record IssueRequest(@NotNull Long bookId,@NotNull Long studentId,@NotNull @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate issueDate,@NotNull @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate dueDate){}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public BookIssue create(@jakarta.validation.Valid @RequestBody IssueRequest r){return service.issue(r.bookId(),r.studentId(),r.issueDate(),r.dueDate());}
 @GetMapping public List<BookIssue> all(@RequestParam(required=false) String q,@RequestParam(required=false) String status){return service.issueHistory(q,status);}
 @GetMapping("/active") public List<BookIssue> active(){return service.activeIssues();}
 @PutMapping("/{id}/return") public BookIssue returnBook(@PathVariable Long id){return service.returnBook(id);}
}
