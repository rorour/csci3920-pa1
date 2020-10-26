package edu.ucdenver.company;

import java.util.ArrayList;

/**
 * TODO: revisit to see if correct implementation based on feedback
 */

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

    public void createOpenOrder(int orderNum) {
        this.openOrder = new Order(orderNum);
        openOrderStatus = true;
    }

    public void finalizeOpenOrder() {
        this.openOrder.finalizeOrder();
        this.finalizedOrders.add(this.openOrder);
        this.openOrderStatus = false;
    }
    public void cancelOrder(){
        this.openOrderStatus = false;
        this.openOrder = null;


    }

    public ArrayList<Order> listOrders() {
        return this.finalizedOrders;
    }

    public Order getOpenOrder() {
        return this.openOrder;
    }

    public boolean openOrderExists() {
        return this.openOrderStatus;
    }

    public ArrayList<Order> getFinalizedOrders() {
        return finalizedOrders;
    }

    @Override
    public String getAccessLevel() {
        return ACCESS_LEVEL;
    }
}
