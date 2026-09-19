package com.trantanh.navipos.view.store;

import com.trantanh.navipos.ScreensController;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.utils.LabelUtils;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class ProductTableViewController implements Initializable {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> barcodeColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TableColumn<Product, Number> indexColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> dphColumn;

    @FXML
    private TableColumn<Product, String> priceWithoutDphColumn;

    @FXML
    private TableColumn<Product, String> quantityColumn;

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

    @FXML
    private Label countProductLabel;

    @FXML
    private Label productInformation;

    @FXML
    private Label billInformation;

    @FXML
    private ComboBox categoryChoiceBox;

    private TextField filterField;

    private ProductService productService = ProductServiceImpl.getInstance();

    private CategoryService categoryData;

    private DecimalFormat df;

    private final ObservableList<Product> data = FXCollections.observableArrayList();

    private ScreensController myController;

    String[] fonts = new String[]{};

    private ScreenCreatorUtils screenCreator;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryData = CategoryServiceImpl.getInstance();
        categoryChoiceBox.setItems(categoryData.getCategoryName());
        for (Product p : productService.getProductList()) {
            data.add(p);
        }
        countProductLabel.setText(data.size() + " KS");
        productTable.setItems(data);
        productTable.setPlaceholder(new Label("Nejsou tu žádné podložky"));
        productTable.setEditable(true);
        filterProduct();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dphColumn.setCellValueFactory(new PropertyValueFactory<>("dph"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceWithoutDphColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> param) -> new ReadOnlyObjectWrapper<>(param.getValue().getPriceWithoutTax()));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, Number> column) -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(column.getValue()) + 1));

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        barcodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        filterProduct();
        editColumn();
        deleteProduct();
    }

    private void filterProduct() {
        FilteredList<Product> filteredData = new FilteredList<>(productService.getProductList(), p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Product> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        productTable.setItems(sortedData);

    }

    private void editColumn() {
        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> event) -> {
            int i = productTable.getSelectionModel().getSelectedIndex();
            String barcode = "" + barcodeColumn.getCellData(i);
            productService.updateProduct("name", event.getNewValue(), barcode);
            LabelUtils.informationLabel(productInformation, "Úspěšně jste změnil název zboží:" + event.getNewValue(), Color.GREEN);
        });
        priceColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> event) -> {
            int i = productTable.getSelectionModel().getSelectedIndex();
            String barcode = "" + barcodeColumn.getCellData(i);
            double value = Double.parseDouble(event.getNewValue());
            df = new DecimalFormat("##0.00");
            productService.updateProduct("price", df.format(value), barcode);
            LabelUtils.informationLabel(productInformation, "Úspěšně jste změnil cenu zboží:" + df.format(value), Color.GREEN);
        });

        quantityColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> event) -> {
            int i = productTable.getSelectionModel().getSelectedIndex();
            String barcode = "" + barcodeColumn.getCellData(i);
            productService.updateProduct("quantity", event.getNewValue(), barcode);
            LabelUtils.informationLabel(productInformation, "Úspěšně jste změnil množství zboží:" + event.getNewValue(), Color.GREEN);
        });

    }

    private void deleteProduct() {
        productTable.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                Product p = productTable.getSelectionModel().getSelectedItem();
                productService.deleteProduct(p.getBarcode());
                data.remove(p);
                LabelUtils.informationLabel(productInformation, "Úspěšně jste smazal product:" + p.getName(), Color.GREEN);
            }
        });
    }

    @FXML
    public void choiceProducts() {
        String name = (String) categoryChoiceBox.getSelectionModel().getSelectedItem();
        List<Product> productList = productService.findAllByCategory(name);
        data.clear();
        data.addAll(productList);
        productTable.setItems(data);
    }
}
