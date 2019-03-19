/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package open.camera;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author shamin
 */
public class FXMLDocumentController implements Initializable {

    public static boolean applicationShouldClose = false;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @FXML
    private RadioButton toggleButton;

    @FXML
    private ImageView imageView;
    public VideoCapture videoCapture;
    public ScheduledExecutorService scheduledExecutorService;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Slider threshold;
    @FXML
    private CheckBox cannyBox;

    int frameCount = 0;
    Image tempImage;
    Image currentImage;

    int streak = 0;
    int mask = 5000;

    int width = 1280;
    int height = 720;
    int[][] flag = new int[width + 5][height + 5];
    Image roznica = null;

    int min_c = 1000000;
    int min_r = 1000000;
    int max_c = -1;
    int max_r = -1;

    Vector<Integer> vc = new Vector<>();
    Vector<Integer> vr = new Vector<>();

    @FXML
    private ImageView tempView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        videoCapture = new VideoCapture();
        threshold.setDisable(true);

        threshold.valueProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                System.out.println("Current value: " + threshold.getValue() * 10);
            }
        });

    }

    @FXML
    private void handleToggleButtonAction(ActionEvent event) {
        if (toggleButton.isSelected()) {
            videoCapture.open(0);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(() -> imageView.setImage(grab()), 0, 50, TimeUnit.MILLISECONDS);
        } else {
            videoCapture.release();
            scheduledExecutorService.shutdown();
        }
    }

    private Image grab() {
        if (applicationShouldClose) {
            if (videoCapture.isOpened()) {
                videoCapture.release();
            }
            scheduledExecutorService.shutdown();
        }

        Image image = null;
        Mat frame = new Mat();

        if (videoCapture.isOpened()) {
            //---------------------------------------------------------------------------------

            videoCapture.read(frame);

            if (frame.empty() == false) {

                // for canny edge detection by opencv native implementation
                if (cannyBox.isSelected()) {
                    frame = doCanny(frame);
//                    System.out.println("Canny Done ! ");
                }
//                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

                MatOfByte buffer = new MatOfByte();
                Imgcodecs.imencode(".jpg", frame, buffer);
                image = new Image(new ByteArrayInputStream(buffer.toArray()));
            } else {
                frameCount = 0;
            }
            //---------------------------------------------------------------------------------

        }

        ++frameCount;

        if (frameCount == 1) {
            tempImage = image;
            System.out.println("Width: " + image.getWidth() + " Height: " + image.getHeight());
//            System.out.println("Frame count: ---------------------------------------------------------------------------------- " + frameCount);
//            tempView.setImage(tempImage);
            return image;
        } else {

            currentImage = differ(image);
            tempImage = image;
//            System.out.println("Frame count: ---------------------------------------------------------------------------------- " + frameCount);
//            tempView.setImage(image);

            //------------------------------------------------------------------
            BufferedImage ii = SwingFXUtils.fromFXImage(currentImage, null);
            String num = frameCount + ".";
            //------------------------------------------------------------------
            return currentImage;
//            return image;
        }
    }

    public Image differ(Image image) {

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage tempBufferedImage = SwingFXUtils.fromFXImage(tempImage, null);

        for (int c = 0; c < bufferedImage.getWidth(); c++) {
            for (int r = 0; r < bufferedImage.getHeight(); r++) {
                int r1 = tempBufferedImage.getRGB(c, r) & 0xFF;
                int r2 = bufferedImage.getRGB(c, r) & 0xFF;

                int rgb = Math.abs(r1 - r2);

//                System.out.println("RGB1 == " + r1 + " RGB2 == " + r2 + " and difference is: " + rgb + " [" + c + " * " + r + "]");
                if (rgb < 45) {
                    rgb = 0;
                } else {
                    rgb = 255;

                    vc.add(c);
                    vr.add(r);
                }

                rgb = rgb << 16 | rgb << 8 | rgb;
                bufferedImage.setRGB(c, r, rgb);
            }
        }

        Image anyImage = SwingFXUtils.toFXImage(bufferedImage, null);

        tempBufferedImage = SwingFXUtils.fromFXImage(image, null);

        for (int i = 0; i < vc.size(); i++) {

            min_c = vc.get(i);
            min_r = vr.get(i);
            max_c = -1;
            max_r = -1;

            getContour(bufferedImage, vc.get(i), vr.get(i));

            if (streak >= mask) {
                tempBufferedImage = draw(tempBufferedImage);
            }

        }

        anyImage = SwingFXUtils.toFXImage(tempBufferedImage, null);

        return anyImage;
    }

    public BufferedImage draw(BufferedImage image) {

        for (int c = min_c; c <= 2 * (max_c - min_c); c++) {
            image.setRGB(c, min_r, 0);

            if (c + 1 < image.getWidth()) {
                image.setRGB(c + 1, min_r, 0);
            }
            if (c + 2 < image.getWidth()) {
                image.setRGB(c + 2, min_r, 0);
            }
        }

        for (int c = min_c; c <= 2 * (max_c - min_c); c++) {
            image.setRGB(c, max_r, 0);

            if (c + 1 < image.getWidth()) {
                image.setRGB(c + 1, max_r, 0);
            }
            if (c + 2 < image.getWidth()) {
                image.setRGB(c + 2, max_r, 0);
            }
        }

        for (int r = min_c; r <= 2 * (max_c - min_c); r++) {
            image.setRGB(min_c, r, 0);

            if (r + 1 < image.getHeight()) {
                image.setRGB(min_c, r, 0);
            }
            if (r + 2 < image.getHeight()) {
                image.setRGB(min_c, r, 0);
            }
        }

        for (int r = min_c; r <= 2 * (max_c - min_c); r++) {
            image.setRGB(max_c, r, 0);

            if (r + 1 < image.getHeight()) {
                image.setRGB(max_c, r, 0);
            }
            if (r + 2 < image.getHeight()) {
                image.setRGB(max_c, r, 0);
            }
        }

        return image;
    }

    public void getContour(BufferedImage image, int c, int r) {

        if (c < 0) {
            return;
        }
        if (r < 0) {
            return;
        }
        if (c >= image.getWidth()) {
            return;
        }
        if (r >= image.getHeight()) {
            return;
        }

        if ((image.getRGB(c, r) & 0xFF) == 255) {
            image.setRGB(c, r, 6);

            if (c < min_c) {
                min_c = c;
            }
            if (r < min_r) {
                min_r = r;
            }
            if (c > max_c) {
                max_c = c;
            }
            if (r > max_r) {
                max_r = r;
            }

            ++streak;
        } else {
            return;
        }

        getContour(image, c + 1, r);
        getContour(image, c - 1, r);
        getContour(image, c, r + 1);
        getContour(image, c, r - 1);
    }

    public Mat doCanny(Mat frame) {

        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();
        Mat dest = new Mat();

        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
        System.out.println("Gray Done ! ");

        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));
        System.out.println("Canny Canny ! ");

        Imgproc.Canny(detectedEdges, detectedEdges, threshold.getValue(), threshold.getValue() * 2, 3, false);

        frame.copyTo(dest, detectedEdges);

        return dest;
    }

    public static void exit() {
        applicationShouldClose = true;
    }

    @FXML
    private void handleCannyBoxAction(ActionEvent event) {
        if (cannyBox.isSelected()) {
            threshold.setDisable(false);
        } else {
            threshold.setDisable(true);
        }
    }

}
