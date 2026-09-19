package com.trantanh.navipos.view.cashdesk;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class LoginController implements Initializable {

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
        if (!passwordTextField.getText().equals("1234")) {
            flashMessageLabel.setText("Zadané heslo je špatně! Zadejte ");
            flashMessageLabel.setTextFill(Color.rgb(210, 39, 30));
        } else {
            flashMessageLabel.setText("Zadal jste spravný heslo");
            flashMessageLabel.setTextFill(Color.rgb(21, 117, 84));
        }
        passwordTextField.clear();
    }

    public void setFocusTextField() {
        passwordTextField.requestFocus();
    }

}
