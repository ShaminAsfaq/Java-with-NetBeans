/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgsuper.shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author TheJoker
 */
public class SuperShop extends Application {
    
    private static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Welcome to 'The Nottingham Derby Online Cart'!");
        alert.setTitle("Welcome!");
        alert.setContentText("© The Nottingham Derby Online Cart.");
        alert.showAndWait();
        
        this.stage= stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
        public static Stage getStage() {
            return stage;
    }
        
}
