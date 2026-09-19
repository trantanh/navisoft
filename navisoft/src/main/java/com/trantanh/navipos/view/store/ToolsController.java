package com.trantanh.navipos.view.store;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class ToolsController implements Initializable {

    @FXML
    private TextField barcodeTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private Label nameLabel;

    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Number> indexColumn;
    @FXML
    private TableColumn<Product, String> fromDateColumn;
    @FXML
    private TableColumn<Product, String> toDateColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    private final String pattern = "dd.MM.yyyy";

    private final ObservableList<Product> data = FXCollections.observableArrayList();

    private ProductService productService;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productService = ProductServiceImpl.getInstance();
        nameLabel.setText("");
        data.addAll(productService.getProductListSale());
        productTable.setItems(data);
        productTable.setPlaceholder(new Label("Nejsou tu žádné podložky"));
        productTable.setEditable(true);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        fromDateColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        toDateColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, Number> column) -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(column.getValue()) + 1));
        goToNextField();

        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        fromDatePicker.setConverter(converter);
        toDatePicker.setConverter(converter);
        fromDatePicker.setValue(LocalDate.now());

    }

    public void goToNextField() {
        barcodeTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Product product = productService.searchProduct(barcodeTextField.getText());
                if (product != null) {
                    nameLabel.setText(product.getName());
                    priceTextField.requestFocus();
                } else {
                    AlertDialogUtils.getWarning("POZOR", "Čárový kód nenalezen!", "Zadejte jiný prosím");
                    barcodeTextField.clear();
                }
            }
        });

        priceTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                fromDatePicker.requestFocus();
            }

            if (event.getCode().equals(KeyCode.UP)) {
                barcodeTextField.requestFocus();
            }

            if (event.getCode().equals(KeyCode.DOWN)) {
                fromDatePicker.requestFocus();
            }

        });

        fromDatePicker.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                toDatePicker.requestFocus();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                priceTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                toDatePicker.requestFocus();
            }
        });
        toDatePicker.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addSaleProduct();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                fromDatePicker.requestFocus();
            }
        });

    }

    @FXML
    public void addSaleProduct() {
        int product_id = productService.getId(barcodeTextField.getText());
        if (isInputValid()) {
            productService.addDiscount(priceTextField.getText(), fromDatePicker.getValue().toString(), toDatePicker.getValue().toString(), product_id);
            AlertDialogUtils.getInformation("Informace", "Úspěšně", "Úspěšně jste přidal slevu");
            barcodeTextField.clear();
            priceTextField.clear();
            nameLabel.setText("");
            fromDatePicker.setValue(LocalDate.now());
            toDatePicker.setValue(toDatePicker.getValue());
            barcodeTextField.requestFocus();
        } else {
            AlertDialogUtils.getWarning("Chyba", "POZOR chyba", "Zadejte znovu");
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (fromDatePicker.getValue() == null || toDatePicker.getValue().compareTo(fromDatePicker.getValue()) < 0) {
            errorMessage += "Datum je prazdný nebo je špatně formártovaná \n";
        }
        if (toDatePicker.getValue() == null) {
            errorMessage += "Datum je prazdný nebo je špatně formártovaná \n";
        }
        try {
            Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Cena musí být čísla\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            AlertDialogUtils.getError("Neplatná pole!", "Prosim vyplňte správně", errorMessage);
            return false;
        }
    }

    public void requestFocusBarcode() {
        barcodeTextField.requestFocus();
    }
}
