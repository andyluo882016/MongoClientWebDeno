package com.mongoclient.demo.repository;


import com.mongodb.BasicDBObject;
import com.mongodb.ReadConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BasicBSONCallback;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import com.mongodb.MongoClient;
import java.util.*;
@Repository
public class BookRepository {

     MongoClient myclient;

     MongoClient getMymongoClient(){
          if(myclient ==null) {
               myclient = new MongoClient("localhost", 27017);
          }
          return myclient;
     }
     public List<Object> getalls(){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> mcollection = dbs.getCollection("book");
          //riteConcern.MAJORITY)
           // mcollection.withReadConcern(ReadConcern.LOCAL).find();
               FindIterable<Document> iterable=  mcollection.withReadConcern(ReadConcern.LOCAL).find();
                       //mcollection.find();
               List<Object> bookResponses = new ArrayList<>();
               for(Document doc : iterable){
                    bookResponses.add(doc);
               }
               return bookResponses;

     }

     public Integer findMaxPrice(){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> collection = dbs.getCollection("book");

          BasicDBObject max = new BasicDBObject("$max", "$price");

          BasicDBObject grp = new BasicDBObject();
          grp.append("_id", null);
          grp.append("max", max);

          BasicDBObject group = new BasicDBObject("$group", grp);
          System.out.println("**-->>  "+group.toJson());

          List<BasicDBObject> pipeline = new ArrayList<BasicDBObject>();
          pipeline.add(group);
          AggregateIterable<Document> ans = collection.aggregate(pipeline);

          String mm= ans.first().get("max").toString();
          System.out.println("---->>>>  Price"+mm);
          Integer mx = Integer.valueOf(mm);
          //return collection.countDocuments();

          return mx;
     }

     public Integer findMinPrice(){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> collection = dbs.getCollection("book");

          BasicDBObject min = new BasicDBObject("$min", "$price");

          BasicDBObject grp = new BasicDBObject();
          grp.append("_id", "price");
          grp.append("min", min);

          BasicDBObject group = new BasicDBObject("$group", grp);
          System.out.println("**-->>  "+group.toJson());

          List<BasicDBObject> pipeline = new ArrayList<BasicDBObject>();
          pipeline.add(group);
          AggregateIterable<Document> ans = collection.aggregate(pipeline);

          String mm= ans.first().get("min").toString();
          System.out.println("---->>>>  Price"+mm);
          Integer mx = Integer.valueOf(mm);
          //return collection.countDocuments();

          return mx;
     }

     public String addOne(Document doc){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> mcollection = dbs.getCollection("book");
          //BasicDBObject filter = new BasicDBObject("INT", '');

          try{
               mcollection.insertOne(doc);
               return "Succsseful inset one document";
          }catch(Exception en) {
               System.out.println("####>>>>>>>  "+en.getMessage());
          }
          return "Unable to insert the document";
     }

     public String updatedById(Document doc, String id){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> mcollection = dbs.getCollection("book");
          BasicDBObject filter = new BasicDBObject("_id", id);
          BasicDBObject update = new BasicDBObject("$set", doc);
          mcollection.updateOne(filter, update);
          return null;
     }

     public String deleteById(Object id){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> mcollection = dbs.getCollection("book");
          BasicDBObject filter = new BasicDBObject("_id", id);
          try{
               mcollection.deleteOne(filter);
               return "Delted Successfully";
          }catch(Exception en){
               System.out.println(en.getMessage());
          }
          return "Delted UnSuccessflly";
     }

     public List<Object> getAllBooksBtPage(int pageNo, int pageSize, String[] fields, String sortBy){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> collection = dbs.getCollection("book");

           BasicDBObject projection = new BasicDBObject("_id", 0);
           for(String field : fields){
                projection.append(field, 1);
           }

           BasicDBObject sort = new BasicDBObject(sortBy, 1);
           FindIterable<Document> findIterable = collection.find().projection(projection).sort(sort)
                   .skip(pageNo * pageSize).limit(pageSize);

           List<Object> booksResponse = new ArrayList<Object>();

           for(Document doc : findIterable) {
                booksResponse.add(doc);
           }
           return booksResponse;
     }

     public long countOfElement() {
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> collection = dbs.getCollection("book");

          return collection.countDocuments();
     }

     public String countPage(){
          MongoClient mymongoclient = getMymongoClient();
          MongoDatabase dbs = mymongoclient.getDatabase("bookstore");
          MongoCollection<Document> collection = dbs.getCollection("book");

          BasicDBObject sum = new BasicDBObject("$sum", "$price");

          BasicDBObject grp = new BasicDBObject();
          grp.append("_id", null);
          grp.append("count", sum);

          BasicDBObject group = new BasicDBObject("$group", grp);
          System.out.println("**-->>  "+group.toJson());

          List<BasicDBObject> pipeline = new ArrayList<BasicDBObject>();
          pipeline.add(group);
          AggregateIterable<Document> ans = collection.aggregate(pipeline);
          return ans.first().get("count").toString();
     }
}
