/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * CellPhone class:
 * Inherits from Electronic class
 */
package edu.ucdenver.company;

import java.time.LocalDate;

public class CellPhone extends Electronic{
    private String imei;
    private String os;

    public CellPhone(String name, String id, String brand, String description, LocalDate incorporatedDate,
                     String serialNum, int warrantyPeriod, String imei, String os) {
        super(name, id, brand, description, incorporatedDate, serialNum, warrantyPeriod);
        this.imei = imei;
        this.os = os;
    }

    public String getImei(){
        return imei;
    }
    public void setImei(String imei){
        this.imei = imei;
    }
    public String getOs(){
        return this.os;
    }
    public void setOs(String os){
        this.os = os;
    }
}
