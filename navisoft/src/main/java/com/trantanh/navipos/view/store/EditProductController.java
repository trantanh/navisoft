package com.trantanh.navipos.view.store;

import com.trantanh.eet.impl.EETClient;
import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.constants.DataScreen;
import com.trantanh.navipos.dao.impl.BillDaoImpl;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.BillService;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.BillServiceImpl;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.LabelUtils;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class EditProductController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private ChoiceBox categoryChoiceBox;

    @FXML
    private Label barcodeLabel;

    @FXML
    private TextField barcodeTextField;

    private ProductService productService = ProductServiceImpl.getInstance();

    private CategoryService categoryData;

    private DecimalFormat df;

    @FXML
    private TextField priceEetTextField;

    private Alert alert;

    private EETClient eet;

    private ConfigManager configManager;

    private BillService billData;

    public void initialize(URL url, ResourceBundle rb) {
        categoryData = CategoryServiceImpl.getInstance();
        billData = BillServiceImpl.getInstance();
        categoryChoiceBox.setItems(categoryData.getCategoryName());
        initBarcode();
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
                categoryChoiceBox.requestFocus();
            }

            if (event.getCode().equals(KeyCode.UP)) {
                priceTextField.requestFocus();
            }

            if (event.getCode().equals(KeyCode.DOWN)) {
                categoryChoiceBox.requestFocus();
            }

        });

        categoryChoiceBox.setOnKeyPressed(event -> {

            if (event.getCode().equals(KeyCode.ENTER)) {
                updateProduct();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                quantityTextField.requestFocus();
            }

        });
    }

    public void initBarcode() {
        barcodeTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (productService.validatorBarcodeProduct(barcodeTextField.getText())) {
                    barcodeTextField.setVisible(false);
                    barcodeLabel.setText("Upravit: " + barcodeTextField.getText());
                    priceTextField.requestFocus();
                    Product product = productService.searchProduct(barcodeTextField.getText());
                    nameTextField.setText(product.getName());
                    priceTextField.setText(product.getPrice());
                    quantityTextField.setText(product.getQuantity());
                    categoryChoiceBox.setValue(product.getCategory());
                } else {
                    LabelUtils.informationLabel(barcodeLabel, "Produkt nenalezen !", Color.RED);
                }
            }

        });
    }

    public void getProduct(String barcode) throws SQLException {
        Product product = productService.searchProduct(barcode);
        nameTextField.setText(product.getName());
        priceTextField.setText(product.getPrice());
        quantityTextField.setText(product.getPrice());
        categoryChoiceBox.getSelectionModel().select(product.getCategory());
        barcodeLabel.setText(barcode);

    }

    @FXML
    public void updateProduct() {
        df = new DecimalFormat("##0.00");
        String id = String.valueOf(productService.getId(barcodeTextField.getText()));
        String name = (String) categoryChoiceBox.getSelectionModel().getSelectedItem();
        double price = Double.valueOf(priceTextField.getText());
        String quantity = "0";
        if (!quantityTextField.getText().isEmpty()) {
            quantity = quantityTextField.getText();
        }
        productService.updateProduct(nameTextField.getText(), df.format(price), quantity, String.valueOf(categoryData.getId(name)), barcodeTextField.getText(), id);
        LabelUtils.informationLabel(barcodeLabel, "Produkt byl úspěšně změneň !", Color.GREEN);
        nameTextField.clear();
        priceTextField.clear();
        quantityTextField.clear();
        quantityTextField.clear();
        barcodeTextField.clear();
        barcodeTextField.setVisible(true);
        barcodeTextField.requestFocus();

    }

    @FXML
    public void returnEet() {

        if (priceEetTextField.getText().equals("")) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Informace");
            alert.setHeaderText(null);
            alert.setContentText("Položka je prázdná");
            alert.showAndWait();
        } else {
            if (isNumeric(priceEetTextField.getText())) {
                double value = Double.parseDouble(priceEetTextField.getText());

                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Informace");
                alert.setHeaderText(null);
                alert.setContentText("Opravdu chcet poslat do EET ?");

                ButtonType buttonTypeOk = new ButtonType("Ano");
                ButtonType buttonTypeCancel = new ButtonType("Ne");

                alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOk) {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Informace");
                    alert.setHeaderText(null);
                    alert.setContentText("Úspěšně jste poslal do EET");
                    alert.showAndWait();
                    int porad_cis = billData.getPoradCisel();
                    porad_cis++;
                    //   eet = new EETClient(-value, porad_cis);
//                    eet = new EETClient(-value, porad_cis, zakl_dan1, dan1, zakl_dan2, dan2), porad_cis).
                    eet.data();
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMddHHmmss");
                    Date date = new Date();
                    String bill_id = dateFormat.format(date);

                    configManager = new ConfigManager();
                    String namePerson = configManager.getLogin();
                    //        billData.createBill(bill_id, "0.00", String.valueOf(-value), namePerson, "0.00", eet.getFik(), eet.getBkp(), eet.getPkp(), porad_cis, "0.00", "0.00", "0.00", "0.00");
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Informace");
                alert.setHeaderText(null);
                alert.setContentText("Prosím zadejte čísla");
                alert.showAndWait();
                priceEetTextField.setText("");
            }
        }
    }

    /*Zjistit jestli je cisla nebo ne */
    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    @FXML
    public void returnBill() {
        ScreenCreatorUtils.getScreen(DataScreen.RETURN_BILL, "Vrátit zboží");
    }
}
