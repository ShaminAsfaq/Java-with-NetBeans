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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class HomeUIController extends CustomerUIController implements Initializable {


    private Statement statement;
    @FXML
    private Hyperlink userLogOutLink;
    @FXML
    private TableView<CustomerProductDescription> historyTableView;
    private ObservableList<CustomerProductDescription> historyEntry;
    @FXML
    private TableColumn<CustomerProductDescription, String> dateColumn;
    @FXML
    private TableColumn<CustomerProductDescription, String> productNameColumn;
    @FXML
    private TableColumn<CustomerProductDescription, Number> totalUnitsColumn;
    @FXML
    private TableColumn<CustomerProductDescription, Number> priceColumn;
    @FXML
    private Label totalCostLabel;
    
    
    private double netCost= 0;
    @FXML
    private Button returnButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label instructionLabel;
    @FXML
    private Hyperlink clickLink;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField reEnterPassword;
    @FXML
    private Hyperlink changePasswordLink;
    @FXML
    private Button doneButton;
    
    public void setUserNameLabel(String text){
        this.userNameLabel.setText(text);
    }
    
    String text;
    /**
     * Initializes the controller class.
     */
    
    private void gotoNext(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerUI.fxml"));
            Parent rootParent = (Parent)loader.load();
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            CustomerUIController dCont = loader.<CustomerUIController>getController();
            dCont.setCustomerUserNameLabel(text);
            Scene mainScene = new Scene(rootParent);
            mainStage.close();
            mainStage.setScene(mainScene);
            mainStage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        newPassword.setVisible(false);
        reEnterPassword.setVisible(false);
        doneButton.setVisible(false);
        historyTableView.setVisible(false);
        totalCostLabel.setVisible(false);
        returnButton.setVisible(false);
        instructionLabel.setVisible(false);
        returnButton.setDisable(true);
        
        ConnectionHelper dbConnector= new ConnectionHelper();
        statement= dbConnector.connectDataBase();
    }    

    @FXML
    private void goForShoppingLinkAction(ActionEvent event) throws IOException {
            text= userNameLabel.getText();
            gotoNext(event);
    }

    @FXML
    private void handleLogOutAction(ActionEvent event) {
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        } catch (IOException ex) {
            System.out.println("Error Occured.");
       
        }
        
        Scene scene = new Scene(root);
        
        Stage stage = SuperShop.getStage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleClickAction(ActionEvent event)  throws SQLException {
                        netCost= 0;
                        historyTableView.setVisible(true);
                        totalCostLabel.setVisible(true);
                        instructionLabel.setVisible(true);
                        clickLink.setDisable(true);
                    String stat = "select * from transaction where customerUserName='" + userNameLabel.getText() + "';";
                    ResultSet resultSet = null;

                    try {
                        resultSet = statement.executeQuery(stat);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    historyEntry= FXCollections.observableArrayList();
                    historyTableView.setItems(historyEntry);

                    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
                    totalUnitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
                    productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
                    priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));        

                        while(resultSet.next()){
                            String day = resultSet.getString("shoppingDate");
                            String name = resultSet.getString("customerProduct");
                            double totalPrice = resultSet.getDouble("totalPrice");
                            double totalUnits = resultSet.getDouble("totalUnits");

                            CustomerProductDescription product = new CustomerProductDescription(name, totalPrice, totalUnits, day);
                            historyEntry.add(product);
                        }
                        
                        //double netCost= 0;
                        String query= "select totalPrice from transaction where customerUserName='" + userNameLabel.getText() + "';";
                        ResultSet totalCost= statement.executeQuery(query);
                        while(totalCost.next()){
                            netCost+= Double.parseDouble(totalCost.getString("totalPrice"));
                        }
                        
                        totalCostLabel.setText("Your total purchase cost so far: " + netCost + " Taka.");
                        returnButton.setVisible(true);
                    }


    @FXML
    private void handleReturnAction(ActionEvent event) throws SQLException {
        CustomerProductDescription anyProduct= historyTableView.getSelectionModel().getSelectedItem();
        String shoppingDate= anyProduct.getDate();
        String productName= anyProduct.getName();
        double totalUnits= anyProduct.getTotalUnits();
        double totalCost= anyProduct.getTotalPrice();
        
        netCost-=totalCost;
        
        String follow= "delete from transaction where shoppingDate='" + shoppingDate +
                        "' and customerProduct='" + productName + "' and totalUnits=" + totalUnits + " and totalPrice=" + totalCost + ";";
        
        statement.execute(follow);
        
                    String stat = "select * from transaction where customerUserName='" + userNameLabel.getText() + "';";
                    ResultSet resultSet = null;

                    try {
                        resultSet = statement.executeQuery(stat);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
        
                    historyEntry= FXCollections.observableArrayList();
                    historyTableView.setItems(historyEntry);

                    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
                    totalUnitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
                    productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
                    priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));        

                        while(resultSet.next()){
                            String day = resultSet.getString("shoppingDate");
                            String name = resultSet.getString("customerProduct");
                            double totalPrice = resultSet.getDouble("totalPrice");
                            double units = resultSet.getDouble("totalUnits");

                            CustomerProductDescription product = new CustomerProductDescription(name, totalPrice, totalUnits, day);
                            historyEntry.add(product);
                        }
                    
        totalCostLabel.setText("Your total purchase cost so far: " + netCost + " Taka.(Excluding donation)");
        
        String query="update product set totalUnit=totalUnit+" + totalUnits + " where productName='" + productName + "';" ;
        statement.execute(query);
        
        handleClickAction(event);
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Product(s) return in due.\nWon't take more than 48 hours.");
        //alert.setContentText("Note: You can not have your donation return back.");
        alert.showAndWait();
    }

    @FXML
    private void handleMouseClickAction(MouseEvent event) {
        returnButton.setDisable(false);
    }

    @FXML
    private void handleDoneAction(ActionEvent event) throws SQLException {
            if(newPassword.getText().compareTo(reEnterPassword.getText())==0){
                String query="update userInfo set userPassword='" + reEnterPassword.getText() + "' where customerUserName='" + userNameLabel.getText() + "';" ;                
                statement.execute(query);
                
                newPassword.setVisible(false);
                reEnterPassword.setVisible(false);
                doneButton.setVisible(false);
                
                newPassword.setText("");
                reEnterPassword.setText("");
                doneButton.setText("");
                
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(HomeUIController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Scene scene = new Scene(root);

                Stage stage = SuperShop.getStage();
                stage.setScene(scene);
                stage.show();
                
                Alert alert= new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Password changed successfully!\nPlease try to log in again.");
                alert.setTitle("Log in");
                alert.showAndWait();
                
            }
            else{
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Password doesn't match.");
                alert.setTitle("Error!!");
                alert.setContentText("Type new passwords carefully.");
                alert.showAndWait();
            }
    }

    @FXML
    private void handleChangePassworAction(ActionEvent event) {
        newPassword.setVisible(true);
        reEnterPassword.setVisible(true);
        doneButton.setVisible(true);
    }
}