package com.library.managementt.controller;
import com.library.managementt.service.LibraryService; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController @RequestMapping("/api/dashboard") public class DashboardController {private final LibraryService service;public DashboardController(LibraryService s){service=s;} @GetMapping("/stats") public Map<String,Long> stats(){return service.stats();}}
