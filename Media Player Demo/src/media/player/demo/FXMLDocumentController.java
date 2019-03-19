/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media.player.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import static media.player.demo.MediaPlayerDemo.stage;

/**
 *
 * @author shamin
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private MediaView mediaView;
    @FXML
    private RadioButton muteButton;
    @FXML
    private MenuBar menuToolBar;
    @FXML
    private ToolBar controlToolBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button stopButton;
    @FXML
    private Button backwardButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Label volumeLabel;
    @FXML
    private Button playPauseButton;

    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean flag;
    private boolean counter;
    private boolean isSet;
    private final String operatingSystem = System.getProperty("os.name");
    private final FileChooser fileChooser = new FileChooser();
//    String text;

    void goForward() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.millis(5000)));
    }

    void goBackward() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.millis(5000)));
    }

    void increaseVolume() {

        volumeSlider.setValue(mediaPlayer.getVolume() * 100 + 1);

        String volume = volumeSlider.getValue() + "";

        if (volume.length() > 6) {
            volume = volume.substring(0, 5);
        }

        volumeLabel.setText("   " + volume + "%");
    }

    void decreaseVolume() {
        volumeSlider.setValue(mediaPlayer.getVolume() * 100 - 1);

        String volume = volumeSlider.getValue() + "";
        if (volume.length() > 6) {
            volume = volume.substring(0, 5);
        }

        volumeLabel.setText("   " + volume + "%");
    }

    void toggleMute() {
        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false);
            muteButton.setSelected(false);
            muteButton.setTextFill(Color.BLACK);

            Image image = new Image("icon/loud.png");
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            muteButton.setText("");
            muteButton.setGraphic(imageView);
        } else {
            mediaPlayer.setMute(true);
            muteButton.setSelected(true);
            muteButton.setTextFill(Color.RED);

            Image image = new Image("icon/mute.png");
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            muteButton.setText("");
            muteButton.setGraphic(imageView);
        }
    }

    void playPuase() {

        if (flag) {
            mediaPlayer.pause();
            System.out.println("Paused");
            flag = false;
        } else {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.millis(500)));
            System.out.println("Resumed");

            mediaPlayer.play();
            flag = true;
        }
        togglePlayPause();
    }

    void setTooltip(Node node, String string) {
        Tooltip.install(node, new Tooltip(string));
    }

    void togglePlayPause() {
        if (flag) {
            Image image = new Image("icon/pause 2.png");
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            playPauseButton.setText("");
            playPauseButton.setGraphic(imageView);
        } else {
            Image image = new Image("icon/play 2.png");
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            playPauseButton.setText("");
            playPauseButton.setGraphic(imageView);
        }
    }

    void setIcons() {
        Image image = new Image("icon/play 2.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        playPauseButton.setText("");
        playPauseButton.setGraphic(imageView);
        //----------------------------------------------------------------------
        /*
         image = new Image("icon/pause.png");
         imageView= new ImageView();
         imageView.setImage(image);
         imageView.setFitHeight(40);
         imageView.setFitWidth(40);
         playPauseButton.setText("");
         playPauseButton.setGraphic(imageView);
         */
        //----------------------------------------------------------------------
        image = new Image("icon/forward 2.png");
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        forwardButton.setText("");
        forwardButton.setGraphic(imageView);
        //----------------------------------------------------------------------
        image = new Image("icon/backward 2.png");
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        backwardButton.setText("");
        backwardButton.setGraphic(imageView);
        //----------------------------------------------------------------------
        image = new Image("icon/stop 3.png");
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        stopButton.setText("");
        stopButton.setGraphic(imageView);
        //----------------------------------------------------------------------
        image = new Image("icon/loud.png");
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        muteButton.setText("");
        muteButton.setGraphic(imageView);
    }

    void tryFullScreen() {

        if (MediaPlayerDemo.stage.fullScreenProperty().get()) {
            MediaPlayerDemo.stage.setFullScreen(false);
            controlToolBar.setOpacity(1.0);
            menuToolBar.setOpacity(1.0);
        } else {
            MediaPlayerDemo.stage.setFullScreen(true);
            controlToolBar.setOpacity(0.0);
            menuToolBar.setOpacity(0.0);
        }
    }

    void stopIfFinished() {
        mediaPlayer.currentTimeProperty().addListener((Observable observable) -> {

            if ((mediaPlayer.getStopTime().subtract(mediaPlayer.currentTimeProperty().get())).compareTo(Duration.seconds(0.1)) >= 1) {
                //                System.out.println(mediaPlayer.getMedia().getDuration().toMinutes());
//                System.out.println(mediaPlayer.currentTimeProperty().get() + " --> " + mediaPlayer.getStopTime());
                System.out.println("Currently playing");

            } else {
                System.out.println("Let you be doomed !");
                mediaPlayer.stop();
                flag = false;

                togglePlayPause();
            }
        });
    }

    void setDirectory() {
        if (operatingSystem.compareTo("Linux") == 0) {
            String home = System.getProperty("user.home");
            fileChooser.setInitialDirectory(new File(home + "/Desktop"));
            isSet = true;
//            System.out.println(fileChooser.getInitialDirectory());
        } else if (operatingSystem.compareTo("Windows") == 0) {
            String home = System.getProperty("user.home");
            fileChooser.setInitialDirectory(new File(home));
            isSet = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //----------------------------------------------------------------------
//        operatingSystem = System.getProperty("os.name");
//        System.out.println(operatingSystem);
        //----------------------------------------------------------------------
        setIcons();

        volumeLabel.setText("   50.0%");
        controlToolBar.setDisable(true);
        mediaView.setSmooth(true);
        menuToolBar.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
        backwardButton.setFocusTraversable(false);
        forwardButton.setFocusTraversable(false);
        muteButton.setFocusTraversable(false);
        volumeSlider.setFocusTraversable(false);

        volumeSlider.valueProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                String volume = volumeSlider.getValue() + "";

                if (volume.length() > 6) {
                    volume = volume.substring(0, 5);
                }

                volumeLabel.setText("   " + volume + "%");
            }
        });

        //  binding the screen size with chosen ratio------------------------------------------------------
        DoubleProperty width = mediaView.fitWidthProperty();                            //  |
        DoubleProperty height = mediaView.fitHeightProperty();                          //  |
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));          //  |
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));        //  |
        mediaView.setPreserveRatio(true);                                               //  |
        mediaView.setSmooth(true);                                                      //  |
        //-------------------------------------------------------------------------------------------------------------------
        /*
         String filePath = new File("/home/shamin/Desktop/sammi meri waar.mp4").getAbsolutePath();
         flag = true;

         media = new Media(new File(filePath).toURI().toString());
         mediaPlayer = new MediaPlayer(media);
         mediaView.setMediaPlayer(mediaPlayer);
         //                mediaPlayer.setAutoPlay(true);
         mediaPlayer.play();             //  any potential difference b/w .play() and .setAutoPlay() ?

         MediaPlayerDemo.stage.setTitle("sammi meri waar.mp4");
         volumeSlider.setValue(50);
         controlToolBar.setDisable(false);
         //        Tooltip.install(mediaView, new Tooltip("sammi meri waar.mp4"));
         //        setTooltip(mediaView, "sammi meri waar.mp4");
         togglePlayPause();
         */

        //----------------------------------------------------------------------
//        volumeLabel.setVisible(false);
//        System.out.println(System.getProperty("user.home"));
        //----------------------------------------------------------------------
    }

    @FXML
    private void handlePausePlayAction(ActionEvent event) {
        playPuase();
        System.out.println(mediaPlayer.currentTimeProperty().get() + " --> " + mediaPlayer.getStopTime());

    }

    @FXML
    private void handleStopAction(ActionEvent event) {
        mediaPlayer.stop();
        mediaPlayer.seek(mediaPlayer.getStartTime());
//        togglePlayPause();
        flag = false;
    }

    @FXML
    private void handleBackwardAction(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.millis(5000)));
    }

    @FXML
    private void handleForwardAction(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.millis(5000)));
    }

    @FXML
    private void handleSelectFileAction(ActionEvent event) {
        if (!isSet) {
//            if (operatingSystem.compareTo("Linux") == 0) {
//                String home = System.getProperty("user.home");
//                fileChooser.setInitialDirectory(new File(home));
//                isSet = true;
//            } else if (operatingSystem.compareTo("Windows") == 0) {
//                String home = System.getProperty("user.home");
//                fileChooser.setInitialDirectory(new File(home));
//                isSet = true;
//            }
            setDirectory();
//            isSet = true;
        }

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            //------------------------------------------------------------------
            System.out.println(file.getParent());

            fileChooser.setInitialDirectory(new File(file.getParent()));
            System.out.println(fileChooser.getInitialDirectory());

            //------------------------------------------------------------------
            if (!counter) {
                flag = true;
                counter = true;
                controlToolBar.setDisable(false);

                String filePath = file.getAbsolutePath();

//                if (filePath.contains(".mp3") || filePath.contains(".mp4") || filePath.contains(".avi") || filePath.contains(".wav")
//                        || filePath.contains(".AAC") || filePath.contains(".FLV")) {
                media = new Media(new File(filePath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();

                //--------------------------------------------------------------
                stopIfFinished();

                mediaPlayer.setVolume(0.5);

                //--------------------------------------------------------------
                MediaPlayerDemo.stage.setTitle(file.getName());

                volumeSlider.setValue(mediaPlayer.getVolume() * 50);
                System.out.println(volumeSlider.getValue() * 100);

//                setTooltip(mediaView, file.getName());
                togglePlayPause();
//                }
            } else {
                handleStopAction(event);
                counter = false;
                flag = true;

                String filePath = file.getAbsolutePath();
                media = new Media(new File(filePath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();

                //--------------------------------------------------------------
                stopIfFinished();

                //--------------------------------------------------------------
                MediaPlayerDemo.stage.setTitle(file.getName());

                volumeSlider.setValue(mediaPlayer.getVolume() * 50);

//                setTooltip(mediaView, file.getName());
                togglePlayPause();
            }
        }
    }

    @FXML
    private void handleCloseAction(ActionEvent event) {
        exit();
    }

    @FXML
    private void handleMuteAction(ActionEvent event) {
        toggleMute();
    }

    @FXML
    private void handleAboutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Media Player Beta - 1.0");
        alert.setHeaderText("Taste the new !");
        alert.showAndWait();
    }

    @FXML
    private void handleMouseClickedAction(MouseEvent event) {

        if (event.getButton().compareTo(MouseButton.PRIMARY) == 0) {
            playPuase();
        } else if (event.getButton().compareTo(MouseButton.SECONDARY) == 0) {

            //------------------------------------------------------------------
            Stage stage = new Stage();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("titledPane.fxml"));
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //------------------------------------------------------------------
            /*
             //---------------Trying to add a context menu on right mouse (secondary) clicked; not even close..
             ContextMenu contextMenu = new ContextMenu();
             MenuItem sub = new MenuItem("open sub..");

             borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent event) {
             contextMenu.show(borderPane, event.getSceneX(), event.getSceneY());
             }
             });
             //---------------Tried to add a context menu on right mouse (secondary) clicked; not even close..
             */
            System.out.println("Where is the context menu ?");
        }
    }

    @FXML
    private void handleKeyPressedAction(KeyEvent event) {
        String input = event.getCode().toString();
//        System.out.println(event.getCode());

        switch (input) {
            case "RIGHT":
                goForward();
                break;

            case "LEFT":
                goBackward();
                break;
            case "UP":
                increaseVolume();
                break;
            case "DOWN":
                decreaseVolume();
                break;
            case "M":
                toggleMute();
                break;
            case "ENTER":
                tryFullScreen();
                break;
        }
    }

    @FXML
    private void handleMouseScrollAction(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            increaseVolume();
        } else {
            decreaseVolume();
        }
    }

    private void playURL() {

//        webView.getEngine().load(urlField.getText());
//        media = new Media(urlField.getText());
//        mediaPlayer = new MediaPlayer(media);
//        mediaView.setMediaPlayer(mediaPlayer);
//        mediaPlayer.play();
//
//        //--------------------------------------------------------------
//        stopIfFinished();
//        //--------------------------------------------------------------
//        //--------------------------------------------------------------
    }

    @FXML
    private void handleURLAction(ActionEvent event) {
        {
            Stage stage = new Stage();
            Parent root;

            try {
                root = FXMLLoader.load(getClass().getResource("WebView.fxml"));

                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setResizable(false);

                //                    playURL(event);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
