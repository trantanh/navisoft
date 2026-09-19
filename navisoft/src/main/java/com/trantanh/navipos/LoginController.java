package com.trantanh.navipos;

import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.dao.impl.UserDaoImpl;
import com.trantanh.navipos.model.User;
import com.trantanh.navipos.utils.SHA1Provider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.trantanh.navipos.constants.NaviPOSConstants.*;

/**
 * FXML Controller class
 *
 * @author Tuan Anh
 */
public class LoginController implements Initializable {

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label flashMessage;

    @FXML
    private Button signInButton;

    @FXML
    private CheckBox checkbox;

    private UserDaoImpl userData;

    private ConfigManager configManager;

    @FXML
    private Label infoLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configManager = new ConfigManager();
        if (configManager.getCheckBox().equals("1")) {
            checkbox.selectedProperty().setValue(true);
            loginTextField.setText(configManager.getLogin());
            passwordField.setText(configManager.getPassword());
            Platform.runLater(() -> passwordField.requestFocus());
        } else {
            checkbox.selectedProperty().setValue(false);
        }
        switch (configManager.getLanguage()) {
            case "1":
                infoLabel.setText("CZ");
                break;
            case "2":
                infoLabel.setText("VN");
                break;
            case "3":
                infoLabel.setText("EN");
                break;
            default:
                infoLabel.setText("CZ");
                break;
        }
        goToNextField();
    }

    @FXML
    public void signIn() {
        SHA1Provider p = new SHA1Provider();
        userData = new UserDaoImpl();
        String hashpassword = p.computeHash(passwordField.getText());
        if (checkbox.selectedProperty().getValue()) {
            if (passwordField.getText().length() <= 15) {
                goToCaskDesk(hashpassword, userData);
                configManager.savePassword(loginTextField.getText(), hashpassword, "1");
            } else {
                goToCaskDesk(passwordField.getText(), userData);
                configManager.savePassword(loginTextField.getText(), passwordField.getText(), "1");
            }
        } else {
            configManager.savePassword("", "", "0");
            if (passwordField.getText().length() <= 30) {
                goToCaskDesk(hashpassword, userData);
            } else {
                goToCaskDesk(passwordField.getText(), userData);
            }
        }
    }

    private void goToCaskDesk(String password, UserDaoImpl userData) {
        for (User user : userData.findAll()) {
            if (loginTextField.getText().equals(user.getLogin()) && password.equals(user.getPassword())) {
                configManager.saveLogin(loginTextField.getText());
                Stage stage = new Stage();
                acceptToGoCashDesk(stage);
                Stage stageCLose = (Stage) loginTextField.getScene().getWindow();
                stageCLose.close();

            } else {
                flashMessage.setTextFill(Color.RED);
                flashMessage.setText("Zadal jste spatny heslo nebo getLogin");
            }

        }
    }

    private void goToNextField() {
        loginTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });
    }

    private void acceptToGoCashDesk(Stage primaryStage) {
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(MENU_BAR_ID, MENU_BAR_FILE);
        mainContainer.loadScreen(CASH_DESK_ID, CASH_DESK_FILE);
        mainContainer.loadScreen(STORE_ID, STORE_FILE);
        mainContainer.loadScreen(SETTING_ID, SETTING_FILE);
        mainContainer.loadScreen(SALES_ID, SALES_FILE);
        mainContainer.setScreen(CASH_DESK_ID);
        VBox bp = new VBox();
        bp.getChildren().add(mainContainer.getScreen(MENU_BAR_ID));
        bp.getChildren().addAll(mainContainer);
        Scene scene = new Scene(bp);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.F1)) {

            }
        });
        scene.getStylesheets().add(LoginController.class.getResource("/css/navipos.css").toExternalForm());
        primaryStage.getIcons().add(new Image(LoginController.class.getResourceAsStream("/icons/logo2.png")));
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }

    @FXML
    public void setCzech() {
        infoLabel.setText("CZ");
        configManager.setLanguage("1");
    }

    @FXML
    public void setVietnamese() {
        infoLabel.setText("VN");
        configManager.setLanguage("2");
    }

    @FXML
    public void setEnglish() {
        infoLabel.setText("EN");
        configManager.setLanguage("3");
    }

}
