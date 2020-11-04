/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Order class:
 * Handles orders adding/removing and listing products
 */
package edu.ucdenver.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Order implements Serializable {
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

    /** Add product to order
     * @param p Product type */
    public void addProduct(Product p){
        products.add(p);
    }

    /** Removes all instances of product in order
     * @param product Product Type*/
    public void removeProduct(Product product){
        products.removeIf(p -> p.equals(product));
    }

    /** Changes status of the the order and retrieves current date */
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

    //: This is for testing purposes only
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
