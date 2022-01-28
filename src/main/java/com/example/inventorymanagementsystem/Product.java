package com.example.inventorymanagementsystem;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Class Product.java
 */

/**
 * @author Joshua Call
 */

public class Product {
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return the price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     *
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @return the stock
     */
    public int getStock() {
        return this.stock;
    }

    /**
     *
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @return the min
     */
    public int getMin() {
        return this.min;
    }

    /**
     *
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return the max
     */
    public int getMax() {
        return this.max;
    }

    /**
     *
     * @param part the part to add to the product
     */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    /**
     *
     * @param selectedAssociatedPart the part to remove
     * @return the result of removing a part from the product
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     *
     * @return the list of parts of the product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }

    @Override
    public String toString(){
        return this.id + ":" + this.name + ":" + this.price + ":" + this.stock + ":" + this.min + ":" + this.max +":" + associatedParts.size();
    }
}
