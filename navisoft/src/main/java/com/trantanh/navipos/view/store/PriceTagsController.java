package com.trantanh.navipos.view.store;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.PrinterService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.PrinterServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PriceTagsController implements Initializable {

    private static final Pattern UNIT_PATTERN = Pattern.compile("^\\s*(\\d+(?:[.,]\\d+)?)\\s*([\\p{L}]+)\\s*$");

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
        String barcode = barcodeTextField.getText().trim();
        String name = nameTextField.getText().trim();
        String price = priceTextField.getText().trim();
        String unit = unitTextField.getText().trim();
        if (barcode.isEmpty() || name.isEmpty() || price.isEmpty() || unit.isEmpty()) {
            AlertDialogUtils.getWarning("Informace", "Prázdné pole", "Vyplňte čárový kód, název, cenu a jednotku.");
            barcodeTextField.requestFocus();
            return;
        }

        String unitCount;
        try {
            BigDecimal numericPrice = new BigDecimal(price.replace(',', '.'));
            if (numericPrice.signum() < 0) {
                throw new NumberFormatException("Cena musí být nezáporné číslo");
            }
            unitCount = getUnit(unit, price.replace(',', '.'));
        } catch (NumberFormatException exception) {
            AlertDialogUtils.getWarning("Neplatná data", "Zkontrolujte cenu a jednotku", "Cena nebo množství v jednotce nemá platný číselný formát.");
            priceTextField.requestFocus();
            return;
        }

        printerService = new PrinterServiceImpl();
        boolean printed = barcodeCheckBox.isSelected()
                ? printerService.printPriceTagWithBarcode(name, price + "KČ", unit, unitCount + " KČ", barcode)
                : printerService.printPriceTag(name, price + "KČ", unit, unitCount + " KČ");
        if (!printed) {
            AlertDialogUtils.getError("Tisk cenovky selhal", "Tiskárna není dostupná", "Zkontrolujte výchozí tiskárnu a zkuste tisk znovu.");
            return;
        }

        productService.updateUnit(unit, barcode);
        barcodeTextField.setVisible(true);
        barcodeTextField.clear();
        unitTextField.clear();
        nameTextField.clear();
        priceTextField.clear();
        barcodeTextField.requestFocus();
    }


    private String getUnit(String value, String price) {
        Matcher matcher = UNIT_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return "bez jednotek";
        }

        BigDecimal unitOfProduct = new BigDecimal(matcher.group(1).replace(',', '.'));
        if (unitOfProduct.signum() <= 0) {
            throw new NumberFormatException("Množství jednotky musí být větší než nula");
        }
        String unit = matcher.group(2);
        BigDecimal priceOfProduct = new BigDecimal(price);
        String result;
        switch (unit.toLowerCase()) {
            case "l":
                result = "1l = " + countUnit(priceOfProduct, unitOfProduct, 1);
                break;
            case "ml":
                result = "100ml = " + countUnit(priceOfProduct, unitOfProduct, 100);
                break;
            case "kg":
                result = "1kg =" + countUnit(priceOfProduct, unitOfProduct, 100);
                break;
            case "g":
                result = "100g =" + countUnit(priceOfProduct, unitOfProduct, 100);
                break;
            case "ks":
                result = "1ks = " + countUnit(priceOfProduct, unitOfProduct, 1);
                break;
            default:
                result = "bez jednotek";
                break;
        }
        return result;
    }

    private String countUnit(BigDecimal priceOfProduct, BigDecimal unitOfProduct, int number) {
        return priceOfProduct.multiply(BigDecimal.valueOf(number))
                .divide(unitOfProduct, 2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}
