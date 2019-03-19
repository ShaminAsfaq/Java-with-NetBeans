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
public class LiveTransaction {
    private String time;
    private String fullName;
    private double bill;

    public LiveTransaction(String time, String fullName, double bill) {
        this.time = time;
        this.fullName = fullName;
        this.bill = bill;
    }

    public String getTime() {
        return time;
    }

    public String getFullName() {
        return fullName;
    }

    public double getBill() {
        return bill;
    }
}
