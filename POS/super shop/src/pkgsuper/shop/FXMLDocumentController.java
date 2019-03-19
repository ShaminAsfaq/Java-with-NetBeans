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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author TheJoker
 */
public class FXMLDocumentController extends HomeUIController implements Initializable {
    @FXML
    private Label todayField;
    @FXML
    private Hyperlink registerLink;
    @FXML
    protected TextField logInUserNameField;
    @FXML
    private TextField logInPasswordField;
    
    Statement statement;
    @FXML
    private TextField authorityIdField;
    @FXML
    private TextField authorityPassField;
    @FXML
    private MenuItem aboutMeMenu;
    @FXML
    private MenuItem contactMeMenu;
    
    String text;
    
    private void gotoNext(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeUI.fxml"));
            Parent rootParent = (Parent)loader.load();
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            HomeUIController dCont = loader.<HomeUIController>getController();
            dCont.setUserNameLabel(text);
            Scene mainScene = new Scene(rootParent);
            mainStage.close();
            mainStage.setScene(mainScene);
            mainStage.show();
    }
     
    private void gotoNextPage(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminUI.fxml"));
            Parent rootParent = (Parent)loader.load();
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            AdminUIController dCont = loader.<AdminUIController>getController();
            dCont.setAdminLabel(text);
            Scene mainScene = new Scene(rootParent);
            mainStage.close();
            mainStage.setScene(mainScene);
            mainStage.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        ConnectionHelper dbConnector= new ConnectionHelper();
        statement= dbConnector.connectDataBase();
        
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date anyDay = new Date();
        String time = dateFormat.format(anyDay.getTime());
        todayField.setText("Time: " + time);
    }
    
    @FXML
    private void handleExitAction(ActionEvent event) {
        exit();
    }

    @FXML
    private void handleRegisterLinkAction(ActionEvent event) {
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error!");
        }
        
        Scene scene = new Scene(root);
        
        Stage stage = SuperShop.getStage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogInAction(ActionEvent event) throws SQLException, IOException {
        
        String userName= logInUserNameField.getText();
        String password= logInPasswordField.getText();
        String sample= "";
        
        if(userName.compareTo(sample)==0){
                    logInUserNameField.setPromptText("Field Empty !");
        }
        
        if(password.compareTo(sample)==0){
                    logInPasswordField.setPromptText("Field Empty !");
        }
        
        else{
            
        String queue= "select * from userInfo where userPassword='" + password + "' and customerUserName='" + userName + "';";
        //statement.execute(queue);
        
        
        ResultSet rs= statement.executeQuery(queue);
        if(rs.absolute(1)){
            text= logInUserNameField.getText();
            gotoNext(event);
        }
        
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Log in page");
            alert.setHeaderText("Log in failed !");
            alert.setContentText("Please check your password again");
            alert.showAndWait();
        }
        
        logInUserNameField.setText("");
        logInPasswordField.setText("");
        }
    }

    @FXML
    private void handleAuthorityLogInAction(ActionEvent event) throws IOException, SQLException {
        /////
        String logInName= authorityIdField.getText();
        String logInPass= authorityPassField.getText();
        String salesId= "sales";
        String salesPass= "man";
        String adminId= "admin";
        String adminPass= "myadmin";
        /////
        
        /////
         if(logInName.compareTo(adminId)==0){
             if(logInPass.compareTo(adminPass)==0){
            text= authorityIdField.getText();
            gotoNextPage(event);
        }
          else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Log in page");
            alert.setHeaderText("Log in failed !");
            alert.setContentText("Please check your id & password again");
            alert.showAndWait();
        }
        }
        else{
             String stat= "select * from salesMan where id='" + logInName + "' and password='" + logInPass + "';";
             ResultSet rs= statement.executeQuery(stat);
             
             if(rs.absolute(1)){
                    Parent root = null;
                    try {
                            root = FXMLLoader.load(getClass().getResource("SupplierUI.fxml"));
                        } catch (IOException ex) {
                    System.out.println("Error Occured.");       
                        }
        
                    Scene scene = new Scene(root);

                    Stage stage = SuperShop.getStage();
                    stage.setScene(scene);
                    stage.show();
             }
             else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Log in page");
                alert.setHeaderText("Log in failed !");
                alert.setContentText("Please check your information again");
                alert.showAndWait();
             }
         }
            
        authorityIdField.setText("");
        authorityPassField.setText("");
        /////
    }

    @FXML
    private void handleAboutMeAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Developer");
        alert.setHeaderText("Developed by:\nশামিন আশফাক,\nঢাকা ক্যান্টনমেন্ট, ঢাকা।");
        alert.setContentText("© The Nottingham Derby Online Cart.");
        alert.showAndWait();
    }

    @FXML
    private void handleContactMeAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Contact Me");
        alert.setHeaderText("Shamin Asfaq\nE-mail: shamin.asfaq@gmail.com\nFacebook: facebook.com/sms.kumar2\n+88 017-323-345-76");
        alert.setContentText("© The Nottingham Derby Online Cart.");
        alert.showAndWait();
    }
}
