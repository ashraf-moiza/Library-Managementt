package com.library.managementt.exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(NotFoundException.class) ResponseEntity<Map<String,String>> notFound(RuntimeException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler({ConflictException.class,DataIntegrityViolationException.class}) ResponseEntity<Map<String,String>> conflict(Exception e){String m=e instanceof ConflictException?e.getMessage():"A value must be unique, or this record is referenced by an issue.";return response(HttpStatus.CONFLICT,m);}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<Map<String,String>> badRequest(Exception e){return response(HttpStatus.BAD_REQUEST,e.getMessage());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,String>> invalid(MethodArgumentNotValidException e){String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Request validation failed");return response(HttpStatus.BAD_REQUEST,m);}
 private ResponseEntity<Map<String,String>> response(HttpStatus status,String message){return ResponseEntity.status(status).body(Map.of("error",message));}
}
