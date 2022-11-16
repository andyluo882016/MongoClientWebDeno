package com.mongoclient.demo.service;

import com.mongoclient.demo.repository.BookRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class BookService {
     @Autowired
    BookRepository bookRepository;
    private BookRepository bookRepository1;

    public String addOne(Map<String, Object> book){
         for(Map.Entry mm: book.entrySet()){
             System.out.println("Key:  "+mm.getKey()+ "  value:  "+mm.getValue());
         }
         Document document = new Document(book);
         return bookRepository.addOne(document);
     }

     public List<Object> getalls(){
         return bookRepository.getalls();
     }

     public String deletedByid(Object id){
           return bookRepository.deleteById(id);

     }

     public String updatedById(Map<String, Object> book, String id){
        Document doc = new Document(book);
        return bookRepository.updatedById(doc, id);
     }

    public Map<String, Object> getAllBooksPage(int pageNo, int pageSize, String[] fields, String sortBy) {
           Map<String, Object> response = new HashMap<String, Object>();
           response.put("data", bookRepository.getAllBooksBtPage(pageNo, pageSize, fields, sortBy));
           long count = bookRepository.countOfElement();
           response.put("No. of Elements", count);
           response.put("No. of Pages", Math.ceil(count / pageSize));
           return response;
    }

    public Map<String, Object> countpage() {
        Map<String, Object> response = new HashMap<String, Object>();
         String page =bookRepository.countPage();
         response.put("Total No. Of Page", page);
         return response;
    }

    public Integer gettotalbooks(){
        return bookRepository.findMaxPrice();
    }

    public Integer getminPrice(){
        return bookRepository.findMinPrice();
    }
}
