package com.mongoclient.demo.controller;

import com.mongoclient.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
public class HomeController {
    @Autowired
    BookService bookService;

    @GetMapping(path="/findall")
    public List<Object> findallbooks(){
        return bookService.getalls();
    }
    @PostMapping(path="/addOne")
    public String addOne(@RequestBody Map<String, Object> book) {
        return bookService.addOne(book);
    }
    @DeleteMapping(path="/delete")
    public String deltedById(@RequestParam Object id){
        return bookService.deletedByid(id);
    }
    @PutMapping(path="/updated/{id}")
    public String updatedById(@RequestBody Map<String, Object> book, @PathVariable("id") String id){

        return bookService.updatedById(book, id);
    }
   @GetMapping("/Page")
    public Map<String, Object> getAllBooksByPage(@RequestParam(value ="pageno", defaultValue = "0") int pageNo,
           @RequestParam(value ="pagesize", defaultValue="5") int pageSize,
           @RequestParam(value ="fields", defaultValue="price,name") String[] fields,
           @RequestParam(value = "sortby", defaultValue = "id") String sortBy){
           return bookService.getAllBooksPage(pageNo, pageSize, fields, sortBy);
    }
   @GetMapping(path="/countpage")
    public Map<String, Object> countPage(){
        return bookService.countpage();
    }

    @GetMapping(path="/getmax")
    public String getmax(){
        return "The Max Price:  " +bookService.gettotalbooks()+"\n"+"the Min price: "+bookService.getminPrice();
    }
}
