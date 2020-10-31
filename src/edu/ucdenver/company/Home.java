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
        this.location = location;
    }

    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        this.location = location;
    }
}
