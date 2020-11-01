/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Electronic class:
 * Inherits from Product class, Parent of Computer and Phone Class
 */
package edu.ucdenver.company;

import java.time.LocalDate;

public class Electronic extends Product{
    private String serialNum;
    private int warrantyPeriod; //todo should we make this more specific? (it wouldn't hurt, how much code do we need to change?)

    public Electronic(String name, String id, String brand, String description, LocalDate incorporatedDate,
                      String serialNum, int warrantyPeriod) {
        super(name, id, brand, description, incorporatedDate);
        this.serialNum = serialNum;
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getSerialNum(){
        return this.serialNum;
    }
    public void setSerialNum(String serialNum){
        this.serialNum = serialNum;
    }

    //@Override
    public String productType() {
        return "Electronic";
    }
}
