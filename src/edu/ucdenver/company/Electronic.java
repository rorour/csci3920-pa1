package edu.ucdenver.company;

import java.time.LocalDate;

public class Electronic extends Product{
    private String serialNum;
    private int warrantyPeriod; //todo should we make this more specific?

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
}
