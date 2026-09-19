package com.trantanh.navipos.view.cashdesk;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import com.trantanh.navipos.utils.DateUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class ReturnBillController implements Initializable {

    @FXML
    private Button sendButton;

    @FXML
    private TextField barcodeTextField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Number> indexColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    private ProductService productService;

    private final ObservableList<Product> data = FXCollections.observableArrayList();

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label tax15Label;

    @FXML
    private Label tax21Label;

    private CategoryService categoryService;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryService = new CategoryServiceImpl();
        productService = ProductServiceImpl.getInstance();
        productTable.getItems().addAll(data);
        productTable.setPlaceholder(new Label("Nejsou tu žádné položky"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, Number> column) -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(column.getValue()) + 1));
        returnBillController(barcodeTextField);
    }

    private void returnBillController(TextField barcodeTextField) {
        barcodeTextField.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (barcodeTextField.getText() != null && !barcodeTextField.getText().isEmpty()) {
                    Product product = productService.searchProduct(barcodeTextField.getText());
                    if (product != null) {
                        data.add(product);
                        productTable.getItems().clear();
                        productTable.getItems().addAll(data);
                        barcodeTextField.clear();
                        barcodeTextField.requestFocus();
                        totalPrice(totalPriceLabel, data);
                    } else {
                        AlertDialogUtils.getWarning("POZOR!", "Produkt číslo: " + barcodeTextField.getText(), "Nenalezen!");
                        barcodeTextField.clear();
                        barcodeTextField.requestFocus();
                    }
                }
            }
        });
    }

    private void totalPrice(Label totalPriceLabel, ObservableList<Product> data) {
        if (data.isEmpty()) {
            totalPriceLabel.setText("0.00");
        } else {
            double totalPrice = 0.00;
            totalPrice = data.stream().map((product) -> Double.parseDouble(product.getPrice())).reduce(totalPrice, (accumulator, _item) -> accumulator + _item);
            totalPriceLabel.setText(DateUtils.format(totalPrice));
        }
    }

    private void calculateTax(Product product) {
        int categoryId = productService.getCategoryId(product.getBarcode());
        String tax = categoryService.getTax(categoryId);
        getTaxForLabel(tax, product.getPrice());
    }

    private void getTaxForLabel(String tax, String price) {
    }

}
