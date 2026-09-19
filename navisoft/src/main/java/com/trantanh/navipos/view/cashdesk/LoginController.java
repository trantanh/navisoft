package com.trantanh.navipos.view.cashdesk;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class LoginController implements Initializable {

    private final String cashDeskPin;

    public LoginController(@Value("${navisoft.cash-desk.pin:}") String cashDeskPin) {
        this.cashDeskPin = cashDeskPin;
    }

    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label flashMessageLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordTextField.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                openCashDriver();
            }
        });
    }

    @FXML
    public void openCashDriver() {
        if (cashDeskPin.isBlank()) {
            flashMessageLabel.setText("Pokladní PIN není nakonfigurován");
            flashMessageLabel.setTextFill(Color.rgb(210, 39, 30));
        } else if (!passwordTextField.getText().equals(cashDeskPin)) {
            flashMessageLabel.setText("Zadaný PIN je nesprávný");
            flashMessageLabel.setTextFill(Color.rgb(210, 39, 30));
        } else {
            flashMessageLabel.setText("PIN je správný");
            flashMessageLabel.setTextFill(Color.rgb(21, 117, 84));
        }
        passwordTextField.clear();
    }

    public void setFocusTextField() {
        passwordTextField.requestFocus();
    }

}
