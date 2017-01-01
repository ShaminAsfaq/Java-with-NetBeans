/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media.player.demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class WebViewController implements Initializable {

    @FXML
    public WebView webView;
    public static WebEngine webEngine;
    @FXML
    private TextField urlField;
    @FXML
    private Button returnButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void handleGoAction(ActionEvent event) {
        String url = urlField.getText();

        url = url.replace("watch?v=", "embed/");

        String con = "<iframe width=\"580\" height=\"340\" src=\"" + url + "\" frameborder=\"0\" allowfullscreen></iframe>";
        webEngine = webView.getEngine();
//        webEngine.load(urlField.getText());
        webEngine.loadContent(con);
//        webView.setPrefSize(650, 400);

    }

    @FXML
    private void handleReturnAction(ActionEvent event) {

        Stage anyStage = (Stage) returnButton.getScene().getWindow();
        anyStage.close();

        /*{
         Stage stage = new Stage();
         Parent root;

         try {
         root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

         Scene scene = new Scene(root);

         stage.setScene(scene);
         //                stage.setResizable(false);

         //                    playURL(event);
         stage.show();
         } catch (IOException ex) {
         Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
         }
         }*/
    }

}
