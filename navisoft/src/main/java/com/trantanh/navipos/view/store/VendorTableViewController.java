package com.trantanh.navipos.view.store;

import com.trantanh.navipos.dao.VendorDao;
import com.trantanh.navipos.dao.impl.VendorDaoImpl;
import com.trantanh.navipos.model.Vendor;
import com.trantanh.navipos.utils.AlertDialogUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class VendorTableViewController implements Initializable {

    @FXML
    private TableView<Vendor> vendorTable;

    @FXML
    private TableColumn<Vendor, Number> indexColumn2;

    @FXML
    private TableColumn<Vendor, String> nameColumn2;

    @FXML
    private TableColumn<Vendor, String> telephoneColumn2;

    @FXML
    private TextField nameTextField2;

    @FXML
    private TextField telephoneTextField2;

    @FXML
    private TextField filterTextField2;

    private final ObservableList<Vendor> dataVendor = FXCollections.observableArrayList();
    private VendorDao vendorDao;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initVendor();
    }

    public void initVendor() {
        vendorDao = new VendorDaoImpl();
        dataVendor.addAll(vendorDao.getVendorsList());
        vendorTable.setItems(dataVendor);
        vendorTable.setPlaceholder(new Label("Nejsou tu žádné podložky"));
        vendorTable.setEditable(true);
        indexColumn2.setSortable(false);
        indexColumn2.setCellValueFactory((TableColumn.CellDataFeatures<Vendor, Number> column) -> new ReadOnlyObjectWrapper<>(vendorTable.getItems().indexOf(column.getValue()) + 1));

        nameColumn2.setCellValueFactory((TableColumn.CellDataFeatures<Vendor, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getName()));
        telephoneColumn2.setCellValueFactory((TableColumn.CellDataFeatures<Vendor, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getTelephone()));
        nameColumn2.setCellFactory(TextFieldTableCell.forTableColumn());
        telephoneColumn2.setCellFactory(TextFieldTableCell.forTableColumn());
        editColumn2();
        filterVendor();
        vendorGoToNextField();
        deleteVendor();
    }

    public void vendorGoToNextField() {
        nameTextField2.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                telephoneTextField2.requestFocus();
            }
        });

        telephoneTextField2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addVendor();
            }
        });

    }

    public void editColumn2() {
        nameColumn2.setOnEditCommit((TableColumn.CellEditEvent<Vendor, String> event) -> {
            int i = vendorTable.getSelectionModel().getSelectedIndex();
            String name = nameColumn2.getCellData(i);
            vendorDao.updateName(name, event.getNewValue());
            AlertDialogUtils.getInformation("Informace", "Úspěšně", "Úspěšně jste změnil dodavetele");
        });

        telephoneColumn2.setOnEditCommit((TableColumn.CellEditEvent<Vendor, String> event) -> {
            int i = vendorTable.getSelectionModel().getSelectedIndex();
            String telephone = telephoneColumn2.getCellData(i);
            vendorDao.updateTelephone(telephone, event.getNewValue());
            AlertDialogUtils.getInformation("Informace", "Úspěšně", "Úspěšně jste změnil telefoni číslo");
        });

    }

    private void deleteVendor() {
        vendorTable.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                Vendor vendor = vendorTable.getSelectionModel().getSelectedItem();
                vendorDao.delete(vendor.getName());
                dataVendor.remove(vendor);
                AlertDialogUtils.getInformation("Informace", "Úspěšně", "Úspěšně jste smazal dodavetel:" + vendor.getName());
            }

        });
    }

    @FXML
    private void addVendor() {
        if (isInputValid()) {
            Vendor vendor = new Vendor();
            vendor.setName(nameTextField2.getText());
            vendor.setTelephone(telephoneTextField2.getText());
            vendor.setCreated(new Timestamp(System.currentTimeMillis()));
            vendorDao.add(vendor);
            dataVendor.add(vendor);
            AlertDialogUtils.getInformation("Informace", "Úspěšně", "Úspěšně jste přidal dodavetele");
            nameTextField2.clear();
            telephoneTextField2.clear();
        }

    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nameTextField2.getText() == null || nameTextField2.getText().length() == 0) {
            errorMessage += "Zadejte prosím jméno dodavetel \n";
        }

        if (telephoneTextField2.getText() == null || telephoneTextField2.getText().length() == 0) {
            errorMessage += "Zadejte prosím tel.číslo dodavetelů \n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            AlertDialogUtils.getWarning("Neplatná data", "Prosím vyplňte ve správném tvaru", errorMessage);
            return false;
        }

    }

    public void filterVendor() {
        FilteredList<Vendor> filteredData = new FilteredList<>(dataVendor, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        filterTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(vendor -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (vendor.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (vendor.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Vendor> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(vendorTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        vendorTable.setItems(sortedData);

    }
}
