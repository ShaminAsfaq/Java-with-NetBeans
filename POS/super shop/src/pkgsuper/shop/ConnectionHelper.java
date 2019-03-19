/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgsuper.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shamin
 */
public class ConnectionHelper {
    Connection connection = null;
    Statement statement= null;
    
    public ConnectionHelper() {
    }
    
    public Statement connectDataBase(){
        String DB_URL = "jdbc:mysql://localhost/super_shopdb";
        String DB_USER = "root";
        String DB_PASS = "shaminasfaq";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SupplierUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }
    
    public void closeDateBase() throws SQLException{
        if(connection!=null){
            connection.close();
        }
    }
}
