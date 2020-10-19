package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private String orderNum;
    private String status; //open, finalize
    private LocalDate finalizedDate;
    private ArrayList<Product> products;

    public Order(String orderNum) {
        this.orderNum = orderNum;
        this.status = "open";
        finalizedDate = null;
        products = new ArrayList<>();
    }

    public void addProduct(Product product){
        if(status == "open"){
            //add product, duplicates are ok
        }
        else{ //send error message
        }
    }

    public void removeProduct(Product product){
        if(this.status == "open"){
            for(Product p : products){
                if(p.equals(product)){
                    products.remove(product);
                }
            }
        } else {
            //send error message
        }

    } //end removeProduct()

    public void finalizeOrder(){
        this.status = "finalized";
        this.finalizedDate = LocalDate.now();
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFinalizedDate() {
        return finalizedDate;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}//end Order
