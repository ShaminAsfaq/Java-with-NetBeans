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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class SupplierUIController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private ListView<ProductDescription> productView;
    private ObservableList<ProductDescription> anyProduct;
    
    Statement statement;
    @FXML
    private TextField totalUnitField;
    @FXML
    private Label warningField;
    @FXML
    private Label warningField2;
    @FXML
    private Button addToCartField;
    @FXML
    private TableView<ProductDescription> allProductTableView;
    private ObservableList<ProductDescription> someProduct;
    @FXML
    private TableColumn<ProductDescription, String> itemColumn;
    @FXML
    private TableColumn<ProductDescription, Number> unitsLeftColumn;
    @FXML
    private Button finishOrderButton;
    @FXML
    private Label attentionLabel;
    @FXML
    private Label attentionLabel2;
    @FXML
    private TextField orderMoreField;
    @FXML
    private Button orderMoreButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        warningField.setText("Warning: everything is fine.");
        ConnectionHelper dbConnector= new ConnectionHelper();
        statement= dbConnector.connectDataBase();
            
            anyProduct = FXCollections.observableArrayList();
            productView.setItems(anyProduct);
            
            String stat = "select * from product;";
            ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(stat);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            someProduct= FXCollections.observableArrayList();
            allProductTableView.setItems(someProduct);
            
            itemColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            unitsLeftColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
            
        try {
            while(resultSet.next()){
                String name = resultSet.getString("productName");
                String catagory = resultSet.getString("productCategory");
                double unitPrice = resultSet.getDouble("unitPrice");
                double totalUnits = resultSet.getDouble("totalUnit");
                
                ProductDescription product = new ProductDescription(name, catagory, unitPrice, totalUnits);
                someProduct.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            String find= "select totalUnit from product";
            ResultSet rs = null;
        try {
            rs = statement.executeQuery(find);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        try {
            while(rs.next()){
                String units= rs.getString("totalUnit");
                
                if(Double.parseDouble(units) > 0 && Double.parseDouble(units) <= 10){
                    attentionLabel.setText("***One or more products are running low on stock. Order more.");   
                }
                
                if(Double.parseDouble(units) <= 0){
                    attentionLabel2.setText("Attention!! Some product(s) are out of stock. ORDER NOW !");
                    attentionLabel.setText("");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleAddAction(ActionEvent event) throws SQLException {        
        String name = nameField.getText();
        String category = categoryField.getText();
        double unitPrice = Double.parseDouble(unitPriceField.getText());
        double totalUnit= Double.parseDouble(totalUnitField.getText());
        
        String command= "select * from product where productName='" + name + "';";
        ResultSet rs= statement.executeQuery(command);
            if(rs.absolute(1)){
                String stat= "update product set totalUnit=totalUnit+" + totalUnit + ", unitPrice=" + unitPrice + " where productName='" + name + "';" ;
                statement.execute(stat);
                
                warningField.setText("Warning: Product already in the entry list.");
                warningField2.setText("Particulars are updated(if any)");
            }
            else{    
        ProductDescription product = new ProductDescription(name, category, unitPrice, totalUnit);
        anyProduct.add(product);
        
        String query = "insert into product values('" + product.getName() + "', '" + product.getCategory() +
                        "', " + product.getUnitPrice() + ", " + totalUnit + ");";
        statement.execute(query);
        
        
        warningField.setText("Warning: Product added.");
        warningField2.setText("");
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Product added !");
            alert.showAndWait();
        nameField.setText("");
        categoryField.setText("");
        unitPriceField.setText("");
        totalUnitField.setText("");
        
        
    }
    @FXML
    private void handleFinishOrderAction(ActionEvent event) throws SQLException {
        /////Shows the updated table of remaining products
        anyProduct = FXCollections.observableArrayList();
        productView.setItems(anyProduct);

        String xtract = "select * from product;";
        ResultSet Ret = statement.executeQuery(xtract);
            
            someProduct= FXCollections.observableArrayList();
            allProductTableView.setItems(someProduct);
            
            itemColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            unitsLeftColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
            
            while(Ret.next()){
                String aName = Ret.getString("productName");
                String aCatagory = Ret.getString("productCategory");
                double aUnitPrice = Ret.getDouble("unitPrice");
                double aTotalUnits = Ret.getDouble("totalUnit");
                
                ProductDescription product = new ProductDescription(aName, aCatagory, aUnitPrice, aTotalUnits);
                someProduct.add(product);
            }
        /////
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Order finished sucessfully !");
        alert.showAndWait();
        finishOrderButton.setDisable(true);
        orderMoreField.setDisable(true);
        orderMoreButton.setDisable(true);
        nameField.setDisable(true);
        categoryField.setDisable(true);
        unitPriceField.setDisable(true);
        totalUnitField.setDisable(true);
        addToCartField.setDisable(true);
        attentionLabel.setDisable(true);
        attentionLabel2.setDisable(true);
        
        warningField.setText("Order process finished successfully.");
        warningField2.setText("You can 'Sign Out' now.");
    }

    @FXML
    private void handleSignOutAction(ActionEvent event) {
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
    private void handleOrderAction(ActionEvent event) throws SQLException {
        String sample= "";
        double totalUnit= Double.parseDouble(orderMoreField.getText());
        
        if(totalUnit<=0.0){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Give a valid value.");
            alert.setTitle("Error!");
            alert.showAndWait();
            orderMoreField.setPromptText("Valid value needed!");
        }
        
        else{
        String anyName= allProductTableView.getSelectionModel().getSelectedItem().getName();
        
        String command= "select * from product where productName='" + anyName + "';";
        ResultSet rs= statement.executeQuery(command);
            if(rs.absolute(1)){
                String stat= "update product set totalUnit=totalUnit+" + totalUnit + " where productName='" + anyName + "';" ;
                statement.execute(stat);
                
                warningField.setText("Warning: Product already in the entry list.");
                warningField2.setText("Particulars are updated(if any)");
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Product added to the order list !\nClick 'Finish Order' to be sure.");
            alert.showAndWait();
            
            orderMoreField.setText("");
            }
    }
}
