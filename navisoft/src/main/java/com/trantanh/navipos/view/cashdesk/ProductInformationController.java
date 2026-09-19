package com.trantanh.navipos.view.cashdesk;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.LabelUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class ProductInformationController implements Initializable {

    @FXML
    private TextField barcodeTextField;

    @FXML
    private Label nameLabel;

    @FXML
    Label priceLabel;

    @FXML
    private Label informationLabel;

    private ProductService productService = ProductServiceImpl.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        barcodeTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Product product = productService.searchProduct(barcodeTextField.getText());
                if (product == null) {
                    LabelUtils.informationLabel(informationLabel, "Čárový kód: " + barcodeTextField.getText() + " nenalezen", Color.RED);
                    nameLabel.setText("");
                    priceLabel.setText("");
                } else {
                    priceLabel.setText(product.getPrice() + " kč");
                    nameLabel.setText(product.getName());
                }
            }
        });
    }
}
