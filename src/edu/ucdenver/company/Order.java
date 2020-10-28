package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Order {
    private int orderNum;
    private String status; //open, finalize
    private LocalDate finalizedDate;
    private ArrayList<Product> products;

    public Order(int orderNum) {
        this.orderNum = orderNum;
        this.status = "open";
        finalizedDate = null;
        products = new ArrayList<>();
    }

    public void addProduct(Product p){
        products.add(p);
    }

    public void removeProduct(Product product){
        products.removeAll(Collections.singletonList(product));
    }

    public void finalizeOrder(){
        this.status = "finalized";
        this.finalizedDate = LocalDate.now();
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
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

    //TODO: Delete this when ready to finalize project. This is for testing purposes only
    public void setFinalizedDate(LocalDate ld){
        this.finalizedDate = ld;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order#%s Status: %s", getOrderNum(),getStatus()));
        Collections.sort(this.products, new ProductComparator());
        if(getStatus().equals("finalized")){
            sb.append(" on: " + getFinalizedDate());
        }
        return sb.toString();
    }
}//end Order
