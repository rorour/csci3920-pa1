/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Home class:
 * Inherits from Product class
 */
package edu.ucdenver.company;

import java.time.LocalDate;

public class Home extends Product{
    private String location;

    public Home(String name, String id, String brand, String description, LocalDate incorporatedDate, String location) {
        super(name, id, brand, description, incorporatedDate);
        setLocation(location);
    }

    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        location = location.toLowerCase();
        StringBuilder sb = new StringBuilder();
        sb.append(location.substring(0,1).toUpperCase());
        sb.append(location.substring(1));
        this.location = sb.toString();
    }

    @Override
    public String productType() {
        return "Home";
    }
}
