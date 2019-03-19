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
public class Product {
    protected String name;
    protected String category;
    protected double unitPrice;

    public Product(String name, String category, double unitPrice) {
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return name;
    }

}
