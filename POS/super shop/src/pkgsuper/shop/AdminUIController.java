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
import java.util.Locale;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class AdminUIController implements Initializable {
    @FXML
    private TableColumn<ProductDescription, String> nameColumn;
    @FXML
    private TableColumn<ProductDescription, String> categoryColumn;
    @FXML
    private TableColumn<ProductDescription, Number> unitPriceColumn;
    @FXML
    private TableColumn<ProductDescription, Number> totalUnitsColumn;
    @FXML
    private TableView<ProductDescription> adminTableView;
    private ObservableList<ProductDescription> insertTable;    
    @FXML
    private TableView<Customer> customerListView;
    private ObservableList<Customer> anyCustomer;
    @FXML
    private TableView<CustomerProductDescription> customerHistoryTableView;
    private ObservableList<CustomerProductDescription> SomeCustomer;
    @FXML
    private TableColumn<CustomerProductDescription, String> dateColumn;
    @FXML
    private TableColumn<CustomerProductDescription, String> productNameColumn;
    @FXML
    private TableColumn<CustomerProductDescription, Number> unitsColumn;
    @FXML
    private Label adminLabel;
    @FXML
    private TableColumn<Customer, String> customerListColumn;
    @FXML
    private Label billLabel;
    @FXML
    private Label totalSaleLabel;
    
    Statement statement;
    @FXML
    private TableView<LiveTransaction> liveTransactionTableView;
    private ObservableList<LiveTransaction> transactionEntry;
    @FXML
    private TableColumn<LiveTransaction, String> liveTimeColumn;
    @FXML
    private TableColumn<LiveTransaction, String> liveCustomerNameColumn;
    @FXML
    private TableColumn<LiveTransaction, Number> liveTotalBillColumn;
    @FXML
    private TextField newPassUserNameField;
    @FXML
    private TextField newPassPasswordField;
    @FXML
    private TableView<SalesMan> salesManTableView;
    private ObservableList<SalesMan> insertSalesMan;
    @FXML
    private TableColumn<SalesMan, String> idColumn;
    @FXML
    private TableColumn<SalesMan, String> passwordColumn;
    @FXML
    private Button removeSalesManButton;
    @FXML
    private Button removeProductButton;
    @FXML
    private Button resetPriceButton;
    @FXML
    private TextField resetUnitPrice;
    
    public void setAdminLabel(String text){
        this.adminLabel.setText(text.toUpperCase());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ConnectionHelper dbConnector= new ConnectionHelper();
        statement= dbConnector.connectDataBase();
        removeSalesManButton.setDisable(true);
        removeProductButton.setDisable(true);
        resetPriceButton.setDisable(true);
        resetUnitPrice.setDisable(true);
        
        
        ///
        
        
        String see= "select * from salesMan";
        ResultSet result = null;
        try {
            result = statement.executeQuery(see);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertSalesMan= FXCollections.observableArrayList();
        salesManTableView.setItems(insertSalesMan);
        
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        
        
        try {
            while(result.next()){
                String id= result.getString("id");
                String password= result.getString("password");
                
                SalesMan salesMan= new SalesMan(id, password);
                insertSalesMan.add(salesMan);
            }
        } catch (SQLException ex) {
            System.out.println("BOOM!!!");
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ///
        ///
        String stat = "select * from product;";
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(stat);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertTable= FXCollections.observableArrayList();
        adminTableView.setItems(insertTable);
        
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        totalUnitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()));        
        
        try {
            while(resultSet.next()){
                String name = resultSet.getString("productName");
                String catagory = resultSet.getString("productCategory");
                double unitPrice = resultSet.getDouble("unitPrice");
                double totalUnits = resultSet.getDouble("totalUnit");
                
                ProductDescription product = new ProductDescription(name, catagory, unitPrice, totalUnits);
                insertTable.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ///
        
        ///
        String stitch= "select * from liveTransaction";
        ResultSet anyResult = null;
        try {
            anyResult = statement.executeQuery(stitch);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        transactionEntry= FXCollections.observableArrayList();
        liveTransactionTableView.setItems(transactionEntry);
        
        liveTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        liveCustomerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        liveTotalBillColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBill()));
        
        
        try {
            while(anyResult.next()){
                String time= anyResult.getString("time");
                String customerName= anyResult.getString("customerName");
                double totalBill= anyResult.getDouble("totalBill");
                
                LiveTransaction transaction= new LiveTransaction(time, customerName, totalBill);
                transactionEntry.add(transaction);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ///
        
        
        ///
        anyCustomer= FXCollections.observableArrayList();
        customerListView.setItems(anyCustomer);
        
        customerListColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        
        String customerList= "select * from userInfo";
        try {
            ResultSet customerSet= statement.executeQuery(customerList);
            
            while(customerSet.next()){
                String customerFullName= customerSet.getString("customerName");
                String customerUserName= customerSet.getString("customerUserName");
                
                Customer c= new Customer(customerFullName, customerUserName);
                anyCustomer.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ///
        
        
        double netCost= 0;
        String query= "select totalPrice from transaction";
        ResultSet anySet= null;
        try {
            anySet= statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while(anySet.next()){         
                netCost+= Double.parseDouble(anySet.getString("totalPrice"));
                            
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        totalSaleLabel.setText("Total Sale so far: " + netCost + " Taka.");
    }
        

    @FXML
    private void handleRemoveFromStoreAction(ActionEvent event) throws SQLException {
        String anyProduct= adminTableView.getSelectionModel().getSelectedItem().getName();
        
        String follow= "delete from product where productName='" + anyProduct + "';";
        statement.execute(follow);

        String stat = "select * from product";
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(stat);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        insertTable= FXCollections.observableArrayList();
        adminTableView.setItems(insertTable);
        
        insertTable= FXCollections.observableArrayList();
        adminTableView.setItems(insertTable);
        
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        totalUnitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()));        
        
        try {
            while(resultSet.next()){
                String name = resultSet.getString("productName");
                String catagory = resultSet.getString("productCategory");
                double unitPrice = resultSet.getDouble("unitPrice");
                double totalUnits = resultSet.getDouble("totalUnit");
                
                ProductDescription product = new ProductDescription(name, catagory, unitPrice, totalUnits);
                insertTable.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void handleClickAction(MouseEvent event) throws SQLException {
        
        String selected= customerListView.getSelectionModel().getSelectedItem().getUserName();
        String speech= "select * from transaction where customerUserName='" + selected + "';";
        ResultSet selectedCustomer= null;
        
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        unitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
        
        SomeCustomer= FXCollections.observableArrayList();
        customerHistoryTableView.setItems(SomeCustomer);
        
        
        try {
            selectedCustomer= statement.executeQuery(speech);
            while(selectedCustomer.next()){
                String day= selectedCustomer.getString("shoppingDate");
                String productName= selectedCustomer.getString("customerProduct");
                double totalUnits= selectedCustomer.getDouble("totalUnits");
                double totalPrice= selectedCustomer.getDouble("totalPrice");
                
                CustomerProductDescription product= new CustomerProductDescription(productName, totalPrice, totalUnits, day);
                SomeCustomer.add(product);
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        double netCost= 0;
        String query= "select totalPrice from transaction where customerUserName='" + selected + "';";
                        ResultSet totalCost= statement.executeQuery(query);
                        while(totalCost.next()){
                            netCost+= Double.parseDouble(totalCost.getString("totalPrice"));
                        }
                        
        billLabel.setText("Total transaction of this customer: " + netCost + " Taka.");
    }

    @FXML
    private void handleSignOutAction(ActionEvent event) {
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
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
    private void handleResetPriceAction(ActionEvent event) throws SQLException {
        String something= resetUnitPrice.getText();
        String product= adminTableView.getSelectionModel().getSelectedItem().getName();
        String command= "update product set unitPrice=" + something + " where productName= '" + product + "';";
        statement.execute(command);
        
        String stat = "select * from product;";
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(stat);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertTable= FXCollections.observableArrayList();
        adminTableView.setItems(insertTable);
        
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        totalUnitsColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalUnits()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()));        
        
        try {
            while(resultSet.next()){
                String name = resultSet.getString("productName");
                String catagory = resultSet.getString("productCategory");
                double unitPrice = resultSet.getDouble("unitPrice");
                double totalUnits = resultSet.getDouble("totalUnit");
                
                ProductDescription addableProduct = new ProductDescription(name, catagory, unitPrice, totalUnits);
                insertTable.add(addableProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        resetUnitPrice.setText("");
    }

    @FXML
    private void handleAddPassAction(ActionEvent event) throws SQLException {
        String user= newPassUserNameField.getText();
        String pass= newPassPasswordField.getText();
        
        String command= "insert into salesMan values('" + user + "', '" + pass + "');";
        statement.execute(command);
        
        newPassUserNameField.setText("");
        newPassPasswordField.setText("");
        
        String see= "select * from salesMan";
        ResultSet result = null;
        try {
            result = statement.executeQuery(see);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertSalesMan= FXCollections.observableArrayList();
        salesManTableView.setItems(insertSalesMan);
        
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        
        
        try {
            while(result.next()){
                String id= result.getString("id");
                String password= result.getString("password");
                
                SalesMan salesMan= new SalesMan(id, password);
                insertSalesMan.add(salesMan);
            }
        } catch (SQLException ex) {
            System.out.println("BOOM!!!");
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRemoveSalesManButtonAction(ActionEvent event) throws SQLException {
        String selectedId= salesManTableView.getSelectionModel().getSelectedItem().getId();
        String selectedPassword= salesManTableView.getSelectionModel().getSelectedItem().getPassword();
        String follow= "delete from salesMan where id='" + selectedId + "' and password='" + selectedPassword + "';";
        statement.execute(follow);
        
        String see= "select * from salesMan";
        ResultSet result = null;
        try {
            result = statement.executeQuery(see);
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertSalesMan= FXCollections.observableArrayList();
        salesManTableView.setItems(insertSalesMan);
        
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        
        
        try {
            while(result.next()){
                String id= result.getString("id");
                String password= result.getString("password");
                
                SalesMan salesMan= new SalesMan(id, password);
                insertSalesMan.add(salesMan);
            }
        } catch (SQLException ex) {
            System.out.println("BOOM!!!");
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleSalesClickAction(MouseEvent event) {
        removeSalesManButton.setDisable(false);
    }

    @FXML
    private void handleProductClickAction(MouseEvent event) {
        removeProductButton.setDisable(false);
        resetPriceButton.setDisable(false);
        resetUnitPrice.setDisable(false);
    }
}
