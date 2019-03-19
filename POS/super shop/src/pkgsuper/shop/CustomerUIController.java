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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shamin
 */
public class CustomerUIController implements Initializable {

    @FXML
    private TextField totalUnitField;
    @FXML
    private TableView<Product> availableProductTableView;
    private ObservableList<Product> availableProducts;
    @FXML
    private TableView<Product> cartTableView;
    private ObservableList<Product> cartProducts;
    
    Statement statement;

    @FXML
    private TableColumn<Product, String> nameColumn1;
    @FXML
    private TableColumn<Product, Number> unitPriceColumn1;
    @FXML
    private TableColumn<Product, String> nameColumn2;
    @FXML
    private TableColumn<Product, Number> unitPriceColumn2;
    @FXML
    private Label todayDate;
    TextField nameField;
    @FXML
    private Label billLabel;
    @FXML
    private Button addToCartButton;
    @FXML
    private Button shopNowButton;
    @FXML
    private Label userNameLabel;

    String text;

    public void setCustomerUserNameLabel(String text) {
        this.userNameLabel.setText(text);
    }

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

    private double bill;
    private String check = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ConnectionHelper dbConnector = new ConnectionHelper();
        statement = dbConnector.connectDataBase();
        
        totalUnitField.setDisable(true);
        addToCartButton.setDisable(true);
        shopNowButton.setDisable(true);
        billLabel.setVisible(false);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMMMM','yyyy");
        Date cal = new Date();
        //Calendar cal = Calendar.getInstance();

        String day = dateFormat.format(cal.getTime());
        todayDate.setText("" + day);

        availableProducts = FXCollections.observableArrayList();
        availableProductTableView.setItems(availableProducts);

        cartProducts = FXCollections.observableArrayList();
        cartTableView.setItems(cartProducts);

        nameColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        unitPriceColumn1.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()));

        nameColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        unitPriceColumn2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getUnitPrice()));

        String stat = "select * from product;";
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(stat);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (resultSet.next()) {
                String name = null;
                try {
                    name = resultSet.getString("productName");
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerUIController.class.getName()).log(Level.SEVERE, null, ex);
                }
                String catagory = resultSet.getString("productCategory");
                double unitPrice = resultSet.getDouble("unitPrice");

                Product product = new Product(name, catagory, unitPrice);
                availableProducts.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAddToCartAction(ActionEvent event) throws SQLException {

        billLabel.setText("");
        billLabel.setVisible(false);
        shopNowButton.setDisable(false);

        double total = Double.parseDouble(totalUnitField.getText());

        String tempName = availableProductTableView.getSelectionModel().getSelectedItem().getName();

        String find = "select totalUnit from product where productName='" + tempName + "';";

        ResultSet rs = statement.executeQuery(find);

        if (rs.next()) {
            double units = rs.getDouble("totalUnit");
            if (units <= 0) {
                Alert info = new Alert(Alert.AlertType.WARNING);
                info.setHeaderText("Product out of stock !");
                info.setContentText("This product is temporarily uavailable.");
                info.setTitle("We are sorry :(");
                info.showAndWait();
            } else if (total > units && total > 0) {
                Alert info = new Alert(Alert.AlertType.WARNING);
                info.setHeaderText("Not enough product in the stock !");
                info.setContentText("Only " + units + " units left in our store.");
                info.setTitle("We are sorry :(");
                info.showAndWait();
            } else if (total <= 0) {
                Alert info = new Alert(Alert.AlertType.WARNING);
                info.setHeaderText("Invalid amount of unit.");
                info.setContentText("Please give a valid unit.");
                info.setTitle("Error!");
                info.showAndWait();
            } else {
                String query = "update product set totalUnit=totalUnit-" + total + " where productName='" + tempName + "';";
                try {
                    statement.execute(query);
                } catch (SQLException ex) {
                    System.out.println("Something wrong Happened!");
                }

                double tempUnitPrice = availableProductTableView.getSelectionModel().getSelectedItem().getUnitPrice();
                double tempTotalPrice = tempUnitPrice * total;

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                Date cal = new Date();
                //Calendar cal = Calendar.getInstance();
                String day = dateFormat.format(cal.getTime());
                System.out.println("" + day);

                String stat = "insert into transaction values('" + userNameLabel.getText() + "', '" + day + "', '"
                        + tempName + "', " + total + ", " + tempTotalPrice + ");";

                    statement.execute(stat);
                    bill += tempTotalPrice;
                    cartProducts.add(availableProductTableView.getSelectionModel().getSelectedItem());
                    cartTableView.setItems(cartProducts);
            }
            totalUnitField.setText("");
        }
    }

    @FXML
    private void handleShopNowAction(ActionEvent event) throws SQLException {

        availableProductTableView.setDisable(true);
        addToCartButton.setDisable(true);
        shopNowButton.setDisable(true);
        totalUnitField.setDisable(true);

        billLabel.setVisible(true);
        billLabel.setText("Total Bill: " + bill + " Taka.");

        String customerName = null;
        DateFormat dateFormat = new SimpleDateFormat("dd MMMMMM','yyyy hh:mm a");
        Date cal = new Date();
        //Calendar cal = Calendar.getInstance();
        String day = dateFormat.format(cal.getTime());

        String stat = "select customerName from userInfo where customerUserName='" + userNameLabel.getText() + "';";
        ResultSet rs = statement.executeQuery(stat);
        if (rs.absolute(1)) {
            String name = rs.getString("customerName");
            customerName = name;
        }

        String query = "insert into liveTransaction values('" + day + "', '" + customerName + "'," + bill + ");";
        statement.execute(query);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bill Details");
        alert.setHeaderText("Your total bill: " + bill + " Taka only");
        alert.setContentText("Shopping Successful!");
        alert.showAndWait();
    }

    @FXML
    private void handleGoBackAction(ActionEvent event) throws IOException {
        text = userNameLabel.getText();
        gotoNext(event);
    }

    @FXML
    private void handleKeyTypeAction(KeyEvent event) {
        addToCartButton.setDisable(false);
    }

    @FXML
    private void handleMouseClickAction(MouseEvent event) {
        totalUnitField.setDisable(false);
    }
}
