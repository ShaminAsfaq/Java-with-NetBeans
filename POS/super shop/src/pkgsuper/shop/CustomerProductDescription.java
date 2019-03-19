/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgsuper.shop;

/**
 *
 * @author shamin
 */
public class CustomerProductDescription {
    private String name;
    private double totalPrice;
    private double totalUnits;
    private String date;

    public CustomerProductDescription(String name, double totalPrice, double totalUnits, String date) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.totalUnits = totalUnits;
        this.date = date;
    }

    
    

    public double getTotalUnits() {
        return totalUnits;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "CustomerProductDescription{" + "name=" + name + ", totalPrice=" + totalPrice + ", totalUnits=" + totalUnits + ", date=" + date + '}';
    }
}
