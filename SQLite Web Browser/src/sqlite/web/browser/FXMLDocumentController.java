/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite.web.browser;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author shamin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab addTab;
    
    private Statement statement;
    private SingleSelectionModel selectionModel;
    
    private HBox hBox;
    private Button go;
    private Button home;
    private Button reload;
    private Button forward;
    private Button backward;
    private Button bookmarks;
    private CheckBox checkBox;
    private TextField textField;
    
    private ResultSet resultSet;
    
    private void createNewTab(Tab tab) {

        /*
        Trying cookies        
         */
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        /*
        End of trying
         */
        BorderPane borderPane = new BorderPane();
        WebView webView = new WebView();
        
        hBox = new HBox();
        textField = new TextField();
        bookmarks = new Button(">>");
        backward = new Button("<-");
        forward = new Button("->");
        reload = new Button("R");
        home = new Button("H");
        go = new Button("Go");
        checkBox = new CheckBox();
        
        go.setOnAction(e -> handleGoAction(e));
        
        hBox.getChildren().addAll(bookmarks, backward, forward, reload, home, textField, go, checkBox);
        HBox.setHgrow(textField, Priority.ALWAYS);
        
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://www.google.com/");
        tab.setText(webEngine.getTitle() == null ? webEngine.getLocation() : "Untitled Tab");
        textField.setText(webEngine.getLocation());
        
        int width = (int) borderPane.getWidth();
        int height = (int) borderPane.getHeight();
        
        System.out.println(width + " , " + height);
        
        borderPane.setTop(hBox);
        borderPane.setCenter(webView);
        
        tab.setContent(borderPane);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        selectionModel = tabPane.getSelectionModel();
        selectionModel.selectLast();
        
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        createNewTab(tab);
        
        ConnectionHelper connection = new ConnectionHelper();
        try {
            statement = connection.getConnection().createStatement();
        } catch (SQLException ex) {
            System.out.println("Problem In Initialize");
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        checkBox.setOnAction(e -> handleCheckBoxClickedAction(e));
        bookmarks.setOnAction(e -> handleBookmarksClickedAction(e));
        
    }
    
    @FXML
    private void handleNewTabAction(Event event) {
        
        try {
            Tab tab = new Tab();
            tabPane.getTabs().add(tab);
            selectionModel.selectLast();
            
            createNewTab(tab);
        } catch (Exception ex) {
            System.out.println("Null Pointer Exception in handleNewTabAction()");
        }
    }
    
    private void handleCheckBoxClickedAction(ActionEvent e) {
        
        ConnectionHelper connection = new ConnectionHelper();
        try {
            statement = connection.getConnection().createStatement();
            
            String url = null;
            if (checkBox.isSelected()) {
                
                url = textField.getText();
                Date timer = new Date();
                String time = timer.toString();
                String name = null;
                
                TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setTitle(textField.getText());
                textInputDialog.setHeaderText("New Bookmarks");
                textInputDialog.setContentText("Name: ");
                Optional<String> result = textInputDialog.showAndWait();
                if (result.isPresent()) {
                    name = result.get();
                } else {
                    name = url;
                }
                
                String query = "insert into bookmarks values('" + url + "', '" + name + "', '" + time + "');";
                System.out.println(query);
                
                try {
                    statement.execute(query);
                    connection.closeConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                url = textField.getText();
                
                String query = "delete from bookmarks where url= '" + textField.getText() + "';";
                try {
                    statement.execute(query);
                    connection.closeConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println(url);
            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void handleBookmarksClickedAction(ActionEvent e) {
        
        ConnectionHelper connection = new ConnectionHelper();
        try {
            statement = connection.getConnection().createStatement();
            
            Tab tab = new Tab();
            tabPane.getTabs().add(tab);
            tab.setText("Bookmarks");
            selectionModel.selectLast();
            
            System.out.println("Here comes the rain again..");
            System.out.println("Falling on my head like a memory..");
            
            TableView<Bookmark> bookmarkTable = new TableView();
            ObservableList<Bookmark> list;
            TableColumn<Bookmark, String> urlColumn = new TableColumn("URL");
            TableColumn<Bookmark, String> nameColumn = new TableColumn("Name");
            TableColumn<Bookmark, String> timeColumn = new TableColumn("Time");
            
            list = FXCollections.observableArrayList();
            
            urlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUrl()));
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
            String query = "select * from Bookmarks;";
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                String url = resultSet.getString("URL");
                String name = resultSet.getString("Name");
                String time = resultSet.getString("Time");
                
                Bookmark bookmark = new Bookmark(url, name, time);
                list.add(bookmark);
            }
            
            bookmarkTable.getColumns().addAll(urlColumn, nameColumn, timeColumn);
            bookmarkTable.setItems(list);

            //  to remove extra last column of table view
            bookmarkTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tab.setContent(bookmarkTable);
            
            connection.closeConnection();
            
        } catch (SQLException ex) {
            System.out.println("Problem ta ekhane -> handleBookmarksClickedAction()");
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void handleGoAction(ActionEvent e) {
        
        BorderPane borderPane = (BorderPane) tabPane.getSelectionModel().getSelectedItem().getContent();
        ObservableList<Node> list = borderPane.getChildren();
        WebView webView = (WebView) list.get(1);
        HBox hBox = (HBox) list.get(0);
        
        TextField textField = (TextField) hBox.getChildren().get(5);
        
        String url = textField.getText();
        
        WebEngine webEngine = webView.getEngine();
        
        System.out.println("Found: " + url);
        
        webEngine.load(url);
        borderPane.setCenter(webView);
    }
    
}
