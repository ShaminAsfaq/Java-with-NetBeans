/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql.to.pkgdo.list;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import javafx.scene.control.Alert;

/**
 *
 * @author shamin
 */
public class ConnectionHelper {

    Connection connection = null;

    public ConnectionHelper() {
    }

    public Connection getConnection() {
        Statement statement = null;

        String DB_URL = "jdbc:mysql://localhost/toDoListdb";
        String DB_USER = "root";
        String DB_PASS = "shaminasfaq";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException ex) {
            //Logger.getLogger(ConnectionHelper.class.getName()).log(Level.SEVERE, null, ex);
            
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection failed with database!");
            alert.showAndWait();
            exit();
        }
        return connection;
    }

    public void closeDatabase() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
