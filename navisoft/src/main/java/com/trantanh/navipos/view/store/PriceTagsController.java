package com.trantanh.navipos.view.store;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.PrinterService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.PrinterServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import com.trantanh.navipos.utils.DateUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PriceTagsController implements Initializable {

    private final Logger logger = org.apache.log4j.Logger.getLogger(PriceTagsController.class.getName());
    @FXML
    private TextField barcodeTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField unitTextField;
    @FXML
    private CheckBox barcodeCheckBox;
    private ProductService productService;
    private PrinterService printerService;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productService = ProductServiceImpl.getInstance();
        initBarcode();
    }

    private void initBarcode() {
        barcodeTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (productService.validatorBarcodeProduct(barcodeTextField.getText())) {
                    barcodeTextField.setVisible(false);
                    priceTextField.requestFocus();
                    Product product = productService.searchProduct(barcodeTextField.getText());
                    nameTextField.setText(product.getName());
                    priceTextField.setText(product.getPrice());
                    unitTextField.setText(product.getUnit());
                } else {
                    AlertDialogUtils.getWarning("Čárový kod nenalezen", "POZOR!", "Čárový kod nenalezen");
                    barcodeTextField.requestFocus();
                }
            }

        });
    }

    @FXML
    public void printPriceTags() {
        if (barcodeTextField.getText().equals("") || priceTextField.getText().equals("") || unitTextField.getText().equals("")) {
            AlertDialogUtils.getWarning("Informace", "Prazdné pole", "Pole jsou prazdné");
            barcodeTextField.requestFocus();
        }
        String name = nameTextField.getText();
        String price = priceTextField.getText();
        String unit = unitTextField.getText();
        String unitCount = getUnit(unitTextField.getText(), priceTextField.getText());
        printerService = new PrinterServiceImpl();

        if(barcodeCheckBox.isSelected()){
            printerService.printPriceTagWithBarcode(name, price + "KČ", unit, unitCount + " KČ", barcodeTextField.getText());
        }else {
            printerService.printPriceTag(name, price + "KČ", unit, unitCount + " KČ");
        }
        productService.updateUnit(unitTextField.getText(), barcodeTextField.getText());
        barcodeTextField.setVisible(true);
        barcodeTextField.clear();
        unitTextField.clear();
        nameTextField.clear();
        priceTextField.clear();
        barcodeTextField.requestFocus();
    }


    private String getUnit(String value, String price) {
        String unit = value.replaceAll("\\d", "");
        String number = value.replaceAll("\\D+", "");
        double priceOfProduct = Double.valueOf(price);
        double unitOfProduct = Double.valueOf(number);
        String result;
        switch (unit.toLowerCase()) {
            case "l":
                result = "1l = " + DateUtils.format(countUnit(priceOfProduct, unitOfProduct, 1));
                break;
            case "ml":
                result = "100ml = " + DateUtils.format(countUnit(priceOfProduct, unitOfProduct, 100));
                break;
            case "kg":
                result = "1kg =" + DateUtils.format(countUnit(priceOfProduct, unitOfProduct, 100));
                break;
            case "g":
                result = "100g =" + DateUtils.format(countUnit(priceOfProduct, unitOfProduct, 100));
                break;
            case "ks":
                result = "1ks = " + DateUtils.format(countUnit(priceOfProduct, unitOfProduct, 1));
            default:
                result = "bez jednotek";
                break;
        }
        return result;
    }

    private double countUnit(double priceOfProduct, double unitOfProduct, double number) {
        return priceOfProduct / (unitOfProduct / number);
    }
}
