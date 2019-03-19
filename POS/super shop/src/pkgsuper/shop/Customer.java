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
public class Customer {
    private String fullName;
    private String userName;

    public Customer(String fullName, String userName) {
        this.fullName = fullName;
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return fullName;
    }
    
    
    
    
}
