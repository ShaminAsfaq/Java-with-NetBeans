/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql.to.pkgdo.list;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Platform.exit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 *
 * @author shamin
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Tab todaysTaskTab;
    @FXML
    private ListView<String> todaysTaskList;
    ObservableList<String> todaysTempList;
    @FXML
    private Tab addTaskTab;
    @FXML
    private DatePicker addTaskDatePicker;
    @FXML
    private CheckBox _12Check;
    @FXML
    private CheckBox _24Check;
    @FXML
    private ComboBox<List> hourComboBox;
    @FXML
    private ComboBox<List> minuteComboBox;
    @FXML
    private TextField whereField;
    @FXML
    private TextField headerField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ProgressBar progressionBar;

    ConnectionHelper conn = new ConnectionHelper();
    Connection connection = null;
    Statement statement = null;
    Time time = new Time();

    @FXML
    private Label statusLabel;

    private int flag;
    @FXML
    private ListView<String> allTaskList;
    ObservableList<String> tempList;
    @FXML
    private ListView<String> todaysTaskDescription;
    ObservableList<String> taskDescription;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label todaysDateLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private ListView<String> addedTaskDescriptionList;
    ObservableList<String> addedTaskDescription;
    @FXML
    private Label miniTimerLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //--------------------------------------------------------------------------------------------
        flag = 0;

        try {
            statement = conn.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //--------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String thisDate = dateFormat.format(date.getTime());
        todaysDateLabel.setText("Today's date: " + thisDate);
        //--------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------
        String query = "select * from task where date= '" + thisDate + "';";

        todaysTempList = FXCollections.observableArrayList();
        todaysTaskList.setItems(todaysTempList);

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String anyDate = resultSet.getString("date");
                String anyTime = resultSet.getString("time");
                String anyHeader = resultSet.getString("header");
                String anyDescription = resultSet.getString("description");
                String anyLocation = resultSet.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                todaysTempList.add(work.toString());
                //System.out.println(work);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //--------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------
        timerLabel.setDisable(true);

        Timeline timeLine = new Timeline();
        timeLine.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.0), event -> {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
            Date timer = new Date();
            String temp = timeFormat.format(timer.getTime());
            timerLabel.setText(" The Time is: " + temp);
            miniTimerLabel.setText(" The Time is: " + temp);
            //System.out.println(temp);

        });
        timeLine.getKeyFrames().add(keyFrame);
        timeLine.play();
        //--------------------------------------------------------------------------------------------
    }

    @FXML
    private void handleAddTaskLinkAction(ActionEvent event) {
        tabPane.getSelectionModel().select(addTaskTab);
    }

    @FXML
    private void handleExitLinkAction(ActionEvent event) {
        exit();
    }

    @FXML
    private void handleAddTaskAction(ActionEvent event) {

        if (flag == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Select a date!");
            alert.setContentText("Running without a destination?");
            alert.setTitle("Warning!");
            alert.showAndWait();
        } else {
            String date = addTaskDatePicker.getValue().toString();
            String hour = hourComboBox.getValue() + "";
            String minute = minuteComboBox.getValue() + "";
            String where = whereField.getText();
            String header = headerField.getText();
            String description = descriptionArea.getText();

            if (hour.contains("null")) {
                hour = "12 AM";
            }
            if (minute.contains("null")) {
                minute = "00";
            }
            if (header.length() == 0) {
                header = "(No header available)";
            }
            if (description.length() == 0) {
                description = "(No description available)";
            }
            if (header.length() == 0 && description.length() == 0) {
                header = "(Empty task)";
                description = "(Empty description)";
            }

            if (header.contains("'")) {
                header = header.replace("'", "\\'");
            }

            if (where.contains("'")) {
                where = where.replace("'", "\\'");
            }

            if (description.contains("'")) {
                description = description.replace("'", "\\'");
            }

            String tQuery = "select * from task where date= '" + date + "' and time= '" + hour + " " + minute + " minutes'; ";
            ResultSet tempSet;
            int freu = 0;

            try {
                tempSet = statement.executeQuery(tQuery);
                while (tempSet.next()) {
                    freu = 1;
                    break;
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (freu == 0) {

                String query = null;
                if (_12Check.isSelected()) {
                    query = "insert into task values('" + date + "','" + hour + " " + minute + " minutes', '"
                            + header + "', '" + description + "', 'at " + where + "');";
                } else if (_24Check.isSelected()) {
                    query = "insert into task values('" + date + "','" + hour + " " + minute + " hour', '"
                            + header + "', '" + description + "', 'at " + where + "');";
                } else {
                    query = "insert into task values('" + date + "','" + hour + " " + minute + " minutes', '"
                            + header + "', '" + description + "', 'at " + where + "');";
                }

                //System.out.println(query);
                try {
                    statement.execute(query);
                    for (double i = 0.1; i <= 10000; i += 0.025) {
                        progressionBar.setProgress(i);
                    }

                    statusLabel.setOpacity(1);
                    statusLabel.setText("Task added!");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Task added successfully!");
                    alert.showAndWait();

                    for (double i = 1.0; i >= 0; i -= 0.1) {
                        statusLabel.setOpacity(i);
                    }
                    progressionBar.setProgress(0.0);

                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error occured!");
                    alert.showAndWait();
                }

                ResultSet resultSet;
                query = "select * from task where date= '" + date + "';";
                System.out.println(query);

                tempList = FXCollections.observableArrayList();
                allTaskList.setItems(tempList);

                try {
                    resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        String anyDate = resultSet.getString("date");
                        String anyTime = resultSet.getString("time");
                        String anyHeader = resultSet.getString("header");
                        String anyDescription = resultSet.getString("description");
                        String anyLocation = resultSet.getString("location");

                        Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                        tempList.add(work.toString());
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    //System.out.println("Empty result");
                    //System.out.println(date);
                }

                _12Check.setSelected(false);
                _24Check.setSelected(false);
                whereField.setText("");
                headerField.setText("");
                descriptionArea.setText("");

                //-----------------------------------------------------------------------------------
                String thisDate = todaysDateLabel.getText();

                if (thisDate.contains("Today's date: ")) {
                    thisDate = thisDate.replace("Today's date: ", "");
                }

                String anyQuery = "select * from task where date= '" + thisDate + "';";

                todaysTempList = FXCollections.observableArrayList();
                todaysTaskList.setItems(todaysTempList);

                ResultSet result;
                try {
                    result = statement.executeQuery(query);

                    while (result.next()) {
                        String anyDate = result.getString("date");
                        String anyTime = result.getString("time");
                        String anyHeader = result.getString("header");
                        String anyDescription = result.getString("description");
                        String anyLocation = result.getString("location");

                        Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                        todaysTempList.add(work.toString());
                        //System.out.println(work);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

                //-----------------------------------------------------------------------------------
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("You can hange time by changing the minute value by 1/2 minutes.");
                alert.setHeaderText("Multiple tasks cannot be assigned at the same time.");
                alert.setTitle("Task overlaped!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void on12HourAction(ActionEvent event) {
        hourComboBox.getItems().clear();
        minuteComboBox.getItems().clear();

        if (_12Check.isSelected()) {
            if (_24Check.isSelected()) {
                _24Check.setSelected(false);
            }
            hourComboBox.setItems(time.set12Values());
            minuteComboBox.setItems(time.setMinutes());
        }
    }

    @FXML
    private void on24Hourction(ActionEvent event) {
        if (_24Check.isSelected()) {
            if (_12Check.isSelected()) {
                _12Check.setSelected(false);
            }
            hourComboBox.setItems(time.set24Values());
            minuteComboBox.setItems(time.setMinutes());
        }
    }

    @FXML
    private void handleTaskDatePickerAction(ActionEvent event) {
        flag = 1;
        String thisDate = addTaskDatePicker.getValue().toString();
        ResultSet resultSet;
        String query = "select * from task where date= '" + thisDate + "';";

        tempList = FXCollections.observableArrayList();
        allTaskList.setItems(tempList);

        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String header = resultSet.getString("header");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");

                Work work = new Work(date, time, header, description, location);

                tempList.add(work.toString());
            }
        } catch (SQLException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No tasks available");
            //System.out.println(thisDate);
        }

    }

    @FXML
    private void handleRemoveLink(ActionEvent event) {
        String thisDate = todaysDateLabel.getText();
        String thisTime = todaysTaskList.getSelectionModel().getSelectedItem();

        if (thisDate.contains("Today's date: ")) {
            thisDate = thisDate.replace("Today's date: ", "");
        }

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "delete from task where date= '" + thisDate + "' and time= '" + thisTime + "';";
        try {
            statement.execute(query);
            taskDescription.clear();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "select * from task where date= '" + thisDate + "';";

        todaysTempList = FXCollections.observableArrayList();
        todaysTaskList.setItems(todaysTempList);

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String header = resultSet.getString("header");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");

                Work work = new Work(date, time, header, description, location);

                todaysTempList.add(work.toString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //-----------------------------------------------------------------------------------------------
        if (flag == 1) {
            String anyDate = addTaskDatePicker.getValue().toString();
            ResultSet result;
            query = "select * from task where date= '" + anyDate + "';";

            tempList = FXCollections.observableArrayList();
            allTaskList.setItems(tempList);

            try {
                result = statement.executeQuery(query);
                while (result.next()) {
                    String date = result.getString("date");
                    String time = result.getString("time");
                    String header = result.getString("header");
                    String description = result.getString("description");
                    String location = result.getString("location");

                    Work work = new Work(date, time, header, description, location);

                    tempList.add(work.toString());
                }
            } catch (SQLException ex) {
                //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("No tasks available");
                //System.out.println(thisDate);
            }
        }

    }

    @FXML
    private void handleTodaysTaskListKeyReleasedAction(KeyEvent event) {
        String thisDate = todaysDateLabel.getText();
        String thisTime = todaysTaskList.getSelectionModel().getSelectedItem();

        if (thisDate.contains("Today's date: ")) {
            thisDate = thisDate.replace("Today's date: ", "");
        }

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "select * from task where date= '" + thisDate + "' and time= '" + thisTime + "';";

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String anyDate = resultSet.getString("date");
                String anyTime = resultSet.getString("time");
                String anyHeader = resultSet.getString("header");
                String anyDescription = resultSet.getString("description");
                String anyLocation = resultSet.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                taskDescription = FXCollections.observableArrayList();
                todaysTaskDescription.setItems(taskDescription);
                taskDescription.add(work.workDescription());

            }
        } catch (SQLException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nothing selected");
        }
    }

    @FXML
    private void handleTodaysTaskListClickAction(MouseEvent event) {
        String thisDate = todaysDateLabel.getText();
        String thisTime = todaysTaskList.getSelectionModel().getSelectedItem();

        if (thisDate.contains("Today's date: ")) {
            thisDate = thisDate.replace("Today's date: ", "");
        }

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "select * from task where date= '" + thisDate + "' and time= '" + thisTime + "';";
        //System.out.println(query);
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String anyDate = resultSet.getString("date");
                String anyTime = resultSet.getString("time");
                String anyHeader = resultSet.getString("header");
                String anyDescription = resultSet.getString("description");
                String anyLocation = resultSet.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                taskDescription = FXCollections.observableArrayList();
                todaysTaskDescription.setItems(taskDescription);
                taskDescription.add(work.workDescription());

            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRemoveAction(ActionEvent event) {
        String thisDate = addTaskDatePicker.getValue().toString();
        String thisTime = allTaskList.getSelectionModel().getSelectedItem();

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "delete from task where date= '" + thisDate + "' and time= '" + thisTime + "';";
        try {
            statement.execute(query);
            //taskDescription.clear();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "select * from task where date= '" + thisDate + "';";

        tempList = FXCollections.observableArrayList();
        allTaskList.setItems(tempList);

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String header = resultSet.getString("header");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");

                Work work = new Work(date, time, header, description, location);

                tempList.add(work.toString());
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //-------------------------------------------------------------------------------------------------------
        String tempDate = todaysDateLabel.getText();

        if (tempDate.contains("Today's date: ")) {
            tempDate = tempDate.replace("Today's date: ", "");
        }

        String anyQuery = "select * from task where date= '" + tempDate + "';";

        todaysTempList = FXCollections.observableArrayList();
        todaysTaskList.setItems(todaysTempList);

        ResultSet result;
        try {
            result = statement.executeQuery(query);

            while (result.next()) {
                String anyDate = result.getString("date");
                String anyTime = result.getString("time");
                String anyHeader = result.getString("header");
                String anyDescription = result.getString("description");
                String anyLocation = result.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                todaysTempList.add(work.toString());
                //System.out.println(work);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //-----------------------------------------------------------------------------------
    }

    @FXML
    private void handleAllTaskListMouseClickedAction(MouseEvent event) {
        String thisDate = addTaskDatePicker.getValue().toString();
        String thisTime = allTaskList.getSelectionModel().getSelectedItem();

        if (thisDate.contains("Today's date: ")) {
            thisDate = thisDate.replace("Today's date: ", "");
        }

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "select * from task where date= '" + thisDate + "' and time= '" + thisTime + "';";

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String anyDate = resultSet.getString("date");
                String anyTime = resultSet.getString("time");
                String anyHeader = resultSet.getString("header");
                String anyDescription = resultSet.getString("description");
                String anyLocation = resultSet.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                addedTaskDescription = FXCollections.observableArrayList();
                addedTaskDescriptionList.setItems(addedTaskDescription);
                addedTaskDescription.add(work.workDescription());

            }
        } catch (SQLException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nothing selected");
        }
    }

    @FXML
    private void handleAllTaskListKeyPressedAction(KeyEvent event) {
        String thisDate = addTaskDatePicker.getValue().toString();
        String thisTime = allTaskList.getSelectionModel().getSelectedItem();

        if (thisDate.contains("Today's date: ")) {
            thisDate = thisDate.replace("Today's date: ", "");
        }

        if (thisTime.contains("at ")) {
            thisTime = thisTime.replace("at ", "");
        }

        String query = "select * from task where date= '" + thisDate + "' and time= '" + thisTime + "';";

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String anyDate = resultSet.getString("date");
                String anyTime = resultSet.getString("time");
                String anyHeader = resultSet.getString("header");
                String anyDescription = resultSet.getString("description");
                String anyLocation = resultSet.getString("location");

                Work work = new Work(anyDate, anyTime, anyHeader, anyDescription, anyLocation);

                addedTaskDescription = FXCollections.observableArrayList();
                addedTaskDescriptionList.setItems(addedTaskDescription);
                addedTaskDescription.add(work.workDescription());

            }
        } catch (SQLException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nothing selected");
        }
    }

}
