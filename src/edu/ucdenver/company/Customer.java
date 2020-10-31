/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Customer class:
 * Handles Customer and customer orders
 * Inherits from User class
 */
package edu.ucdenver.company;

import java.util.ArrayList;

public class Customer extends User {
    private String name;
    private String address;
    private ArrayList<Order> finalizedOrders;
    private Order openOrder; //will be added to orders list when finalized
    private boolean openOrderStatus;
    private final String ACCESS_LEVEL;

    public Customer(String displayName, String email, String password) {
        super(displayName, email, password);
        this.ACCESS_LEVEL = "customer";
        finalizedOrders = new ArrayList<>();
        openOrderStatus = false;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /** Creates an open order for the Customer
     * Changes order status to open/true
     * @param orderNum the integer the current order is assigned given by company
     */
    public void createOpenOrder(int orderNum) {
        this.openOrder = new Order(orderNum);
        openOrderStatus = true;
    }

    /** Finalizes the open order for the Customer
     * Adds order to list of finalized orders for Customer
     * Changes order status to closed/false*/
    public void finalizeOpenOrder() {
        this.openOrder.finalizeOrder();
        this.finalizedOrders.add(this.openOrder);
        this.openOrderStatus = false;
    }

    /**Cancels open order and initializes it to null*/
    public void cancelOrder(){
        this.openOrderStatus = false;
        this.openOrder = null;


    }

    public Order getOpenOrder() {
        return this.openOrder;
    }

    public boolean openOrderExists() {
        return this.openOrderStatus;
    }

    public ArrayList<Order> getFinalizedOrders() {
        return this.finalizedOrders;
    }

    @Override
    public String getAccessLevel() {
        return ACCESS_LEVEL;
    }
}
