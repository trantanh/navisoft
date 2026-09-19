package com.trantanh.navipos.view.store;

import com.trantanh.database.Database;
import com.trantanh.navipos.ControlledScreen;
import com.trantanh.navipos.ScreensController;
import com.trantanh.navipos.constants.DataScreen;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.dao.impl.DataDaoImpl;
import com.trantanh.navipos.model.Data;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import com.trantanh.navipos.utils.LabelUtils;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class StoreController implements Initializable, ControlledScreen {

    private DataDaoImpl dataDaoImpl;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField barcodeTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField filterTextField;

    @FXML
    private ChoiceBox choicebox;

    @FXML
    private BorderPane bp;

    private TextField filterField;

    private ProductService productService;

    private CategoryService categoryData;

    private DecimalFormat df;

    private final ObservableList<Product> data = FXCollections.observableArrayList();

    private ScreensController myController;

    @FXML
    private Label flashMessage;

    @FXML
    private Label productInformation;

    @FXML
    private Label billInformation;

    String[] fonts = new String[]{};

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productService = ProductServiceImpl.getInstance();
        categoryData = CategoryServiceImpl.getInstance();
        productService.getProductList().forEach((p) -> {
            data.add(p);
        });
        choicebox.setItems(categoryData.getCategoryName());
        choicebox.getSelectionModel().selectFirst();
        barcodeTextField.setText(String.valueOf(barcodeTextField.getText()));
        goToNextTextField();
    }

    public void enterAddProduct() {
        if (quantityTextField.getText().equals("")) {
            quantityTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    addProduct();
                }
                if (event.getCode().equals(KeyCode.UP)) {
                    priceTextField.requestFocus();
                }

                if (event.getCode().equals(KeyCode.DOWN)) {
                    choicebox.requestFocus();
                }
            });
        }
    }

    @FXML
    public void addProduct() {
        String name = (String) choicebox.getSelectionModel().getSelectedItem();
        if (isInputValid(name)) {
            if (productService.validatorBarcodeProduct(barcodeTextField.getText())) {
                AlertDialogUtils.getWarning("Čárový kód", "POZOR!", "Zadal jste stejný čárový kód, prosím zvolte jiný");
            } else {
                double value = Double.parseDouble(priceTextField.getText());
                df = new DecimalFormat("##0.00");
                productService.addProduct(nameTextField.getText(), df.format(value), barcodeTextField.getText(), quantityTextField.getText(), name);
                Product product = productService.searchProduct(barcodeTextField.getText());
                data.add(product);
                LabelUtils.informationLabel(flashMessage, "Úspěšně jste přidal zboží", Color.GREEN);
                nameTextField.clear();
                priceTextField.clear();
                barcodeTextField.clear();
                quantityTextField.clear();
                barcodeTextField.requestFocus();
                quantityTextField.setText("200");
            }
        }
    }

    private void goToNextTextField() {

        barcodeTextField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {

            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (!productService.validatorBarcodeProduct(barcodeTextField.getText())) {
                        nameTextField.requestFocus();
                    } else {
                        flashMessage.setTextFill(Color.RED);
                        flashMessage.setText("Čárový kód : " + barcodeTextField.getText() + "\n"
                                + "je obsazeno " + "zvolte jiné");
                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    Thread.sleep(4000);

                                } catch (InterruptedException e) {
                                }
                                return null;
                            }
                        };
                        barcodeTextField.clear();
                        sleeper.setOnSucceeded(event1 -> flashMessage.setText(""));
                        new Thread(sleeper).start();
                    }
                }
                if (event.getCode().equals(KeyCode.DOWN)) {
                    nameTextField.requestFocus();
                }
            }
        });

        nameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                priceTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                barcodeTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                priceTextField.requestFocus();
            }
        });

        priceTextField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {

            @Override
            public void handle(javafx.scene.input.KeyEvent event) {

                if (event.getCode().equals(KeyCode.ENTER)) {
                    quantityTextField.requestFocus();
                }
                if (event.getCode().equals(KeyCode.UP)) {
                    nameTextField.requestFocus();
                }
                if (event.getCode().equals(KeyCode.DOWN)) {
                    quantityTextField.requestFocus();
                }
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void goToCategory() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_CATEGORY, "Kategorie");
    }

    @FXML
    public void updateProduct() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_EDIT_PRODUCT, "Upravit zboží");
    }

    @FXML
    public void goToProduct() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_TABLE_PRODUCTS, "Seznam zboží");
    }

    @FXML
    public void goToBillList() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_BILLS, "Seznam účtenky");
    }

    @FXML
    public void goToVendorList() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_VENDOR, "Seznam dodavatelů");
    }

    @FXML
    public void goToTools() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_TOOLS, "Seznam slev");
    }

    @FXML
    public void goToPriceTags() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_PRICE_TAGS, "Cenovky");
    }

    private boolean isInputValid(String name) {
        String errorMessage = "";
        if (barcodeTextField.getText() == null || barcodeTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím čárový kód \n";
        }
        if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím název produktu \n";
        }

        if (priceTextField.getText() == null || priceTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím cenu produktu \n";
        } else {
            Double.parseDouble(priceTextField.getText());
            errorMessage += "Cena produktu musí být čísla \n";
        }

        if (quantityTextField.getText() == null || quantityTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím počet ks produktu \n";
        } else {
            try {
                Integer.parseInt(quantityTextField.getText());
            } catch (Exception e) {

                errorMessage += "Počet ks produktu musí být čísla \n";
            }
        }

        if (name == null || name.length() == 0) {
            errorMessage += "Vyberte prosim kategorii \n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            AlertDialogUtils.getWarning("Neplatná data", "Prosím vyplňte ve správném tvaru", errorMessage);
            return false;
        }
    }

    @FXML
    public void importData() {
        dataDaoImpl = new DataDaoImpl();
        Data databaseData = dataDaoImpl.getData();
        Database.export(databaseData.getPathFile(), databaseData.getPathMySQl());
    }
}
