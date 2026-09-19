package com.trantanh.navipos;

import com.trantanh.navipos.utils.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class KeyFormController implements Initializable {

    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private PasswordField textField;

    @FXML
    private PasswordField textField2;

    @FXML
    private Label informationLabel;

    @FXML
    private Label infoCounterLabel;

    @FXML
    private TextField macAddressTextField;

    private String key = "";
    private String key2 = "";
    private static int counter = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        informationLabel.setText("");
        infoCounterLabel.setText("");

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        dateLabel.setText(dateFormat.format(date));
        timeLabel.setText(timeFormat.format(date));
        setMacAddress();
    }

    @FXML
    public void acceptButton() throws IOException {
        if (textField.getText().equals(key)) {
            if (textField2.getText().length() == 36) {
                LabelUtils.informationLabel(informationLabel, "Heslo je spravne", Color.GREEN);
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Login.fxml"));
                Scene scene = new Scene(root);
                stage.setTitle("Login");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.sizeToScene();
                stage.show();
                System.exit(0);

            } else {
                LabelUtils.informationLabel(informationLabel, "Heslo je spatne!", Color.RED);
            }
        } else {
            LabelUtils.informationLabel(informationLabel, "Heslo je spatne!", Color.RED);
        }

        counter++;
        infoCounterLabel.setText("Hacknout system :" + counter + "krát");

        if (counter == 4) {

            for (int i = 0; i < 100; i++) {
                openBrowser();
            }
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("POZOR!!");
            alert.setHeaderText("Tuan Anh SOFT");
            alert.setContentText("Snážíš hacknout systém Tuan Anh SOFT ? Vzdyť je to FREE :D ");
            alert.showAndWait();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }
        }
    }

    public void openBrowser() {
        String url = "http://www.google.com";

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setMacAddress() {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());

            Random r = new Random();
            char a = (char) (r.nextInt(26) + 'A');

            macAddressTextField.setText(a + a + "-" + sb.toString() + "-" + a + a);
            System.out.println(MD5(sb.toString()));
            key = MD5(sb.toString());
            key2 = MD5("TA" + sb.toString()) + "TA";
        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e) {

            e.printStackTrace();

        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
