/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Computer class:
 * Inherits from Electronic class
 */
package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;

public class Computer extends Electronic{
    private ArrayList<String> specs;
    public Computer(String name, String id, String brand, String description, LocalDate incorporatedDate,
                    String serialNum, int warrantyPeriod)
    {
        super(name, id, brand, description, incorporatedDate, serialNum, warrantyPeriod);
    }
    public ArrayList<String> getSpecs(){
        return specs;
    }
    public void addSpec(String spec){
        specs.add(spec);
    }
    public void removeSpec(String spec){
        specs.removeIf(s -> s.equals(spec));
    }
}
