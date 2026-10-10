package com.library.managementt.controller;
import com.library.managementt.entity.Book; import com.library.managementt.service.LibraryService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/books")
public class BookController { private final LibraryService service; public BookController(LibraryService s){service=s;}
 @GetMapping public List<Book> all(@RequestParam(required=false) String q){return service.books(q);}
 @GetMapping("/{id}") public Book one(@PathVariable Long id){return service.book(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Book create(@Valid @RequestBody Book b){return service.saveBook(b,null);}
 @PutMapping("/{id}") public Book update(@PathVariable Long id,@Valid @RequestBody Book b){return service.saveBook(b,id);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.deleteBook(id);}
}
