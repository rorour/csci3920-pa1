package edu.ucdenver.company;

import java.util.ArrayList;

/**TODO: revisit to see if correct implementation based on feedback */

public class Customer extends User{
    private String name;
    private String address;
    private ArrayList<Order> finalizedOrders; //will only be of finalized orders
    private Order openOrder; //will be added to orders list when finalized
    private boolean openOrderPermission; //ensures only one open order is available at a time.
    private final String ACCESS_LEVEL;

    public Customer(String displayName, String email, String password) {
        super(displayName, email, password);
        this.ACCESS_LEVEL = "customer";
        finalizedOrders = new ArrayList<>();
        openOrderPermission = true;

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

    public void createOpenOrder(String orderNum){
        if(openOrderPermission){
            openOrder = new Order(orderNum);
        } else {
            //send message
        }
    }





    @Override
    public String getAccessLevel() {
        return ACCESS_LEVEL;
    }
}
