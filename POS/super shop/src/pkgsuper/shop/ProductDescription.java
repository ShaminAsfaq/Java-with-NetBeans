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
public class ProductDescription extends Product{
    private double totalUnits;

    public ProductDescription(String name, String category, double unitPrice, double totalUnits) {
        super(name, category, unitPrice);
        this.totalUnits = totalUnits;
    }

    public double getTotalUnits() {
        return totalUnits;
    }

    @Override
    public String toString() {
        return name;
    }
}
