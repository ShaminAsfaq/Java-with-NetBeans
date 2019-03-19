/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgsuper.shop;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class RegistrationController implements Initializable {

    @FXML
    private TextField userNameRegisterField;
    @FXML
    private TextField userPasswordField;

    Statement statement;
    @FXML
    private Button registerButton;
    @FXML
    private TextField fullNameRegistrationField;

    String text;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Hyperlink termsAndConditionsLink;

    private void gotoNext(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeUI.fxml"));
        Parent rootParent = (Parent) loader.load();
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        HomeUIController dCont = loader.<HomeUIController>getController();
        dCont.setUserNameLabel(text);
        Scene mainScene = new Scene(rootParent);
        mainStage.close();
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ConnectionHelper dbConnector = new ConnectionHelper();
        statement = dbConnector.connectDataBase();
        registerButton.setDisable(true);
    }

    @FXML
    private void handleRegisterAction(ActionEvent event) throws IOException {
        String fullName = fullNameRegistrationField.getText();
        String userName = userNameRegisterField.getText();
        String password = userPasswordField.getText();
        double donation = 10;

        String sample = "";

        if (userName.compareTo(sample) == 0 || fullName.compareTo(sample) == 0 || password.compareTo(sample) == 0) {
            userNameRegisterField.setPromptText("FILL ME!!");
            fullNameRegistrationField.setPromptText("FILL ME!!");
            userPasswordField.setPromptText("FILL ME!!");
        } else {
            try {
                String query = "insert userInfo values('" + fullName + "', '" + userName + "', '" + password + "');";
                statement.execute(query);

                text = userNameRegisterField.getText();
                gotoNext(event);

            } catch (SQLException ex) {
                System.out.println("Something is wrong!");
            }
        }
    }

    @FXML
    private void handleLogInLinkAction(ActionEvent event) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);

        Stage stage = SuperShop.getStage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleCheckBoxAction(ActionEvent event) {
        if (checkBox.isSelected()) {
            registerButton.setDisable(false);
        } else {
            registerButton.setDisable(true);
        }
    }

    @FXML
    private void onTermsAndConditionsLinkAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("By clicking the checkbox, you agree that:\n\n"
                + "1. You are on your own in any kind of illegal transaction.\n"
                + "2. For any fake information, depending on the severity, we can discard your account.\n"
                + "3. You cannot return any product after 48 hours.\n"
                + "4. Company can change their policy at any time, and you agree with that.\n");
        alert.setTitle("Terms and Conditions");
        alert.setContentText("© The Nottingham Derby Online Cart.");
        alert.showAndWait();
    }
}
