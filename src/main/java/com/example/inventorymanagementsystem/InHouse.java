package com.example.inventorymanagementsystem;

/**
 * Class InHouse.java
 */

/**
 * @author Joshua Call
 */

public class InHouse extends Part {


    private int machineId;

    public InHouse(int id, String name, double price, int stock,
                   int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId the machine id to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * @return the machine id
     */
    public int getMachineId(){
        return this.machineId;
    }

    @Override
    public String toString(){
        return super.toString() + ":" + this.machineId;
    }

}

