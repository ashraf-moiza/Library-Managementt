package com.library.managementt.controller;
import com.library.managementt.entity.Student; import com.library.managementt.service.LibraryService;
import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/students")
public class StudentController { private final LibraryService service; public StudentController(LibraryService s){service=s;}
 @GetMapping public List<Student> all(@RequestParam(required=false) String q){return service.students(q);}
 @GetMapping("/{id}") public Student one(@PathVariable Long id){return service.student(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Student create(@Valid @RequestBody Student s){return service.saveStudent(s,null);}
 @PutMapping("/{id}") public Student update(@PathVariable Long id,@Valid @RequestBody Student s){return service.saveStudent(s,id);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.deleteStudent(id);}
}
