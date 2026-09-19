package com.trantanh.navipos.view.cashdesk;

import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import com.trantanh.navipos.utils.LabelUtils;
import com.trantanh.navipos.utils.ValidUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class AddProductDialogController implements Initializable {

    @FXML
    private ChoiceBox choicebox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField quantityTextField;

    private CategoryService categoryData;

    @FXML
    private Label barcodeLabel;

    @FXML
    private Label informationLabel;

    private ProductService productService;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productService = ProductServiceImpl.getInstance();
        informationLabel.setText("");
        categoryData = CategoryServiceImpl.getInstance();
        nameTextField.requestFocus();
        choicebox.setItems(categoryData.getCategoryName());
        choicebox.getSelectionModel().selectFirst();
        quantityTextField.setText("10");
        wayTextField();
    }

    public void wayTextField() {
        nameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                priceTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                priceTextField.requestFocus();
            }
        });

        priceTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                quantityTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                nameTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                quantityTextField.requestFocus();
            }
        });

        quantityTextField.setOnKeyPressed(event -> {

            if (event.getCode().equals(KeyCode.ENTER)) {
                choicebox.requestFocus();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                priceTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                choicebox.requestFocus();
            }
        });

        choicebox.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addProduct();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                quantityTextField.requestFocus();
            }
        });
    }

    @FXML
    private void addProduct() {
        String categoryName = (String) choicebox.getSelectionModel().getSelectedItem();
        if (ValidUtils.validInput(nameTextField.getText(), priceTextField.getText())) {
            if (productService.validatorBarcodeProduct(barcodeLabel.getText())) {
                AlertDialogUtils.getWarning("Čárový kód", "POZOR!", "Zadal jste stejný čárový kód, prosím zvolte jiný");
            } else {
                productService.addProduct(nameTextField.getText(), priceTextField.getText(), barcodeLabel.getText(), quantityTextField.getText(), categoryName);
                LabelUtils.informationLabel(informationLabel, "Zboží byl úspěšně přidan", Color.GREEN);
                closeWindow();
            }
        }

    }

    private void closeWindow(){
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            Stage stageCLose = (Stage) quantityTextField.getScene().getWindow();
            stageCLose.close();
        });
        new Thread(sleeper).start();
    }
    public void setBarcode(String barcode) {
        barcodeLabel.setText(barcode);
    }

    public void nameFocusTextField() {
        nameTextField.requestFocus();
    }

}
