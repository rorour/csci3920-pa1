package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;

public class Product {
    private String name;
    private String id;
    private String brand;
    private String description;
    private LocalDate incorporatedDate;
    private ArrayList<Category> categories;
    //todo figure out how to add: private Image image;

    public Product(String name, String id, String brand, String description,
                   LocalDate incorporatedDate){
        this.name = name;
        this.id = id;
        this.brand = brand;
        this.description = description;
        this.incorporatedDate = incorporatedDate;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public LocalDate getIncorporatedDate(){
        return this.incorporatedDate;
    }
    public void setIncorporatedDate(LocalDate date){
        this.incorporatedDate = date;
    }
    public void addCategory(Category c){
        categories.add(c);
    }
    @Override
    public String toString(){
        //todo implement
        return this.name + "to string";
    }

}
