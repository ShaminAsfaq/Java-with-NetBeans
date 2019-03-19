package image.tool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author shamin
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;

    //--------------------------------------------------------------------------
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser multipleFileChooser = new FileChooser();
    private File file;
    private File differFile;
    private BufferedImage inputImage = null;    //  grayscale tab --> right image
    private BufferedImage outputImage = null;   //  grayscale tab --> left image
    private BufferedImage inputImage2 = null;   //  Differ image tab --> left image
    private BufferedImage inputImage3 = null;   //  Differ image tab --> left image
    private BufferedImage finalImage = null; //  Differed image tab --> final image
    private boolean flag1 = false;  // Differ image tab --> leftPane doesn't have an image
    private boolean flag2 = false;  // Differ image tab --> rightPane doesn't have an image
    private boolean grayScale = false;  // grayscale image tab --> rightPane doesn't have an image
    private boolean frontImage = false;  // grayscale image tab --> rightPane doesn't have an image
    private boolean differedFlag = false;  // Differ image tab --> finalPane doesn't have an image
    private static String home;

    @FXML
    private AnchorPane leftPane1;
    @FXML
    private AnchorPane rightPane1;
    @FXML
    private AnchorPane finalPane;

    //--------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        home = System.getProperty("user.home");
        home += "/Desktop";
        fileChooser.setInitialDirectory(new File(home));
        multipleFileChooser.setInitialDirectory(new File(home));
    }

    @FXML
    private void handleSelectImageAction(ActionEvent event) {
        file = fileChooser.showOpenDialog(null);

        if (file != null) {

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                fileChooser.setInitialDirectory(new File(file.getParent()));
                inputImage = bufferedImage;

                if (bufferedImage != null) {
                    setImage(leftPane, bufferedImage);
//                    setImage(rightPane, inputImage);
                }

            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void handleSaveAsAction(ActionEvent event) {

        if (grayScale == true || frontImage == true) {
            saveImage(outputImage);
        } else {
            showAlert(AlertType.ERROR, "Please convert the image first in order to save");
        }
    }

    @FXML
    private void handleCloseAction(ActionEvent event) {
        exit();
    }

    @FXML
    private void handleRGBToGrayScaleAction(ActionEvent event) {

        if (inputImage != null) {
            outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
//        setImage(rightPane, bufferedImage);

            for (int c = 0; c < inputImage.getWidth(); c++) {
                for (int r = 0; r < inputImage.getHeight(); r++) {
                    int rgb = inputImage.getRGB(c, r);

//                System.err.println("c: " + c + " --> r: " + r);
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = (rgb >> 0) & 0xFF;

                    int intensity = (int) (red * 0.72 + green * 0.21 + blue * 0.07);
                    rgb = (intensity << 16) | (intensity << 8) | intensity;
                    outputImage.setRGB(c, r, rgb);
                }
            }

            setImage(rightPane, outputImage);
            grayScale = true;
        } else {
            showAlert(AlertType.ERROR, "Please select an image first");
        }
    }

    private void setImage(AnchorPane anchorPane, BufferedImage bufferedImage) {
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView imageView = new ImageView(image);
        anchorPane.getChildren().removeAll();
        anchorPane.getChildren().add(imageView);
    }

    private void showAlert(AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setContentText(text);
        alert.setHeaderText("Behold !");
        alert.showAndWait();
    }

    @FXML
    private void handleAboutAction(ActionEvent event) {
        showAlert(AlertType.INFORMATION, "This is the greatest image tool ever !");
    }

    @FXML
    private void handleSelectMultipleImageAction(ActionEvent event) {
        differFile = multipleFileChooser.showOpenDialog(null);
        BufferedImage bufferedImage = null;

        if (differFile != null) {
            try {
                bufferedImage = ImageIO.read(differFile);
                multipleFileChooser.setInitialDirectory(new File(differFile.getParent()));

                if (flag1 == true && flag2 == false) {
                    setImage(rightPane1, bufferedImage);
                    inputImage3 = bufferedImage;
//                    inputImage3 = makeGray(bufferedImage);
                    flag2 = true;   // Differ image --> rightPane now has an image
                    System.out.println("Right pane used !");
                }

                if (flag1 == false) {
                    setImage(leftPane1, bufferedImage);
                    inputImage2 = bufferedImage;
//                    inputImage2 = makeGray(bufferedImage);
                    flag1 = true;   // Differ image --> leftPane now has an image
                    System.out.println("Left pane used !");
                }

            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleDifferAction(ActionEvent event) {

        if (flag1 == false || flag2 == false) {
            showAlert(AlertType.ERROR, "Please select 2 images first");
        } else {

            System.out.println(inputImage2.getWidth() + " " + inputImage2.getHeight());
            BufferedImage bufferedImage = new BufferedImage(inputImage2.getWidth(), inputImage2.getHeight(), inputImage2.getType());

            System.out.println("Differing image..");

            for (int c = 0; c < inputImage2.getWidth(); c++) {
                for (int r = 0; r < inputImage2.getHeight(); r++) {

                    int rgb0 = inputImage2.getRGB(c, r) & 0xFF;
                    int rgb1 = inputImage3.getRGB(c, r) & 0xFF;

                    int rgb = Math.abs(rgb0 - rgb1);

                    rgb = (rgb << 16) | (rgb << 8) | rgb;

                    bufferedImage.setRGB(c, r, rgb);
                }
            }

            setImage(finalPane, bufferedImage);
            finalImage = bufferedImage;
            differedFlag = true;
        }
    }

    @FXML
    private void handleSaveDifferImageAsAction(ActionEvent event) {
        if (differedFlag == false) {
            showAlert(AlertType.ERROR, "Please convert the images");
        } else {
            saveImage(finalImage);
        }
        //----------------------------------------------------------------------
    }

    private void saveImage(BufferedImage image) {
        try {
            FileChooser tempFileChooser = new FileChooser();
            tempFileChooser.setInitialDirectory(new File(home));
            File tempFile = tempFileChooser.showSaveDialog(null);
            String format = tempFile.getName().substring(tempFile.getName().indexOf(".") + 1);

            ImageIO.write(image, format, tempFile);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage makeGray(BufferedImage bufferedImage) {
        BufferedImage tempImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());

        for (int c = 0; c < bufferedImage.getWidth(); c++) {
            for (int r = 0; r < bufferedImage.getHeight(); r++) {
                int rgb = bufferedImage.getRGB(c, r);

//                System.err.println("c: " + c + " --> r: " + r);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;

                int intensity = (int) (red * 0.72 + green * 0.21 + blue * 0.07);
                rgb = (intensity << 16) | (intensity << 8) | intensity;
                tempImage.setRGB(c, r, rgb);
            }
        }

        return tempImage;
    }

    @FXML
    private void handleResetGrayAction(ActionEvent event) {

        //  buggy..
        leftPane.getChildren().removeAll();
        rightPane.getChildren().removeAll();
    }

    @FXML
    private void handleMultipleResetAction(ActionEvent event) {

        //  buggy..
        leftPane1.getChildren().removeAll();
        rightPane1.getChildren().removeAll();
        finalPane.getChildren().removeAll();
    }

    @FXML
    private void handleFindTAction(ActionEvent event) {
        double T = 127;
        double temp = 0;
        double eps = 0.4;
//        int arr[] = new int[256];

        if (inputImage != null) {

            BufferedImage bufferedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
            BufferedImage image;
//            inputImage = getGrayScale(inputImage);
            inputImage = makeGrayBright(inputImage);

            while (true) {
                int arr[] = new int[256];
                for (int c = 0; c < inputImage.getWidth(); c++) {
                    for (int r = 0; r < inputImage.getHeight(); r++) {
                        int rgb = inputImage.getRGB(c, r);

//                        if (c == 0 && r == 0) {
//                            System.out.println("Temp 1: " + rgb);
//                        }
                        rgb = rgb & 0xFF;
//                        if (c == 0 && r == 0) {
//                            System.out.println("Temp 2: " + rgb);
//                        }
                        ++arr[rgb];

//                        bufferedImage.setRGB(c, r, rgb);
                    }
                }

                double m1 = 0, m2 = 0;
                double sum1 = 0, sum2 = 0;

                for (int i = 0; i <= 255; i++) {
//                System.out.println(i + " counts: " + arr[i]);

                    if (i < T) {
                        m1 += (i * arr[i]);
                        sum1 += arr[i];
                    } else {
                        m2 += (i * arr[i]);
                        sum2 += arr[i];
                    }
                }

                m1 /= sum1;
                m2 /= sum2;

                temp = 0.5 * (m1 + m2);

                if (Math.abs(T - temp) > eps) {
                    T = temp;
                    System.out.println("Changed T: " + T);
                } else {
                    System.out.println("Broken T: " + T);
                    System.out.println("Broken temp: " + temp);
                    System.out.println("Broken");
                    break;
                }
            }

            for (int c = 0; c < inputImage.getWidth(); c++) {
                for (int r = 0; r < inputImage.getHeight(); r++) {
                    int rgb = inputImage.getRGB(c, r);

//                    System.out.println("RGB: " + rgb);
                    int red = (rgb >> 16) & 0xFF;
                    if (red < T) {
                        red = 0;
                    } else {
                        red = 255;
                    }

                    int green = (rgb >> 8) & 0xFF;
                    if (green < T) {
                        green = 0;
                    } else {
                        green = 255;
                    }

                    int blue = (rgb >> 8) & 0xFF;
                    if (blue < T) {
                        blue = 0;
                    } else {
                        blue = 255;
                    }

                    int intensity = (int) (red * 0.72 + green * 0.21 + blue * 0.07);
                    rgb = (intensity << 16) | (intensity << 8) | intensity;
                    bufferedImage.setRGB(c, r, rgb);

                }
            }

            setImage(rightPane, bufferedImage);
            outputImage = bufferedImage;
            frontImage = true;
        }

    }

    BufferedImage getGrayScale(BufferedImage bufferedImage) {

        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
//        setImage(rightPane, bufferedImage);

        for (int c = 0; c < bufferedImage.getWidth(); c++) {
            for (int r = 0; r < bufferedImage.getHeight(); r++) {
                int rgb = bufferedImage.getRGB(c, r);

//                System.err.println("c: " + c + " --> r: " + r);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;

                int intensity = (int) (red * 0.72 + green * 0.21 + blue * 0.07);
                rgb = (intensity << 16) | (intensity << 8) | intensity;
                image.setRGB(c, r, rgb);
            }
        }
//        setImage(rightPane, image);
        return image;
    }

    BufferedImage makeGrayBright(BufferedImage bufferedImage) {
        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        double mul = 1.5;

        for (int c = 0; c < bufferedImage.getWidth(); c++) {
            for (int r = 0; r < bufferedImage.getHeight(); r++) {
                int rgb = bufferedImage.getRGB(c, r);

                int red = (rgb >> 16) & 0xFF;

//                    System.out.println("RED: " + red);
                red *= mul;
                if (red > 255) {
                    red = 255;
                } else if (red < 50) {
                    red += 100;
                }

                int green = (rgb >> 8) & 0xFF;

//                    System.out.println("GREEN: " + green);
                green *= mul;
                if (green > 255) {
                    green = 255;
                } else if (green < 50) {
                    green += 100;
                }

                int blue = (rgb >> 0) & 0xFF;
//                    System.out.println("BLUE: " + blue);
                blue *= mul;
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 50) {
                    blue += 100;
                }

                int intensity = (int) (red * 0.72 + green * 0.21 + blue * 0.07);
                rgb = (intensity << 16) | (intensity << 8) | intensity;

                rgb = (rgb << 16) | (rgb << 8) | rgb;

                image.setRGB(c, r, rgb);
            }
        }
//        setImage(leftPane1, image);
        return image;
    }
}
