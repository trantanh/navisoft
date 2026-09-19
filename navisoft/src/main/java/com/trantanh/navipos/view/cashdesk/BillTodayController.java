package com.trantanh.navipos.view.cashdesk;

import com.trantanh.eet.table.Eet;
import com.trantanh.eet.table.OfflineEET;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.service.BillService;
import com.trantanh.navipos.service.impl.BillServiceImpl;
import com.trantanh.navipos.view.store.BillDetailController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class BillTodayController implements Initializable {

    @FXML
    private TableView<Bill> billTable;

    @FXML
    private TableColumn<Bill, Number> billIndexColumn;

    @FXML
    private TableColumn<Bill, String> billNumberColumn;

    @FXML
    private TableColumn<Bill, String> billDateColumn;

    @FXML
    private TableColumn<Bill, String> billTimeColumn;

    @FXML
    private TextField billFilterTextField;

    private BillService billData;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Eet> eetTable;

    @FXML
    private TableColumn<Eet, Number> IndexColumn;

    @FXML
    private TableColumn<Eet, String> eetNumberColumn;

    @FXML
    private TableColumn<Eet, String> eetDateColumn;

    @FXML
    private TableColumn<Eet, String> eetTimeColumn;

    @FXML
    private TableColumn<Eet, String> sendColumn;

    private final ObservableList<Bill> dataBill = FXCollections.observableArrayList();

    private OfflineEET offlineEET;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBill();
    }

    private void initBill() {
        billData = BillServiceImpl.getInstance();
        dataBill.addAll(billData.getCurrentBills());
        billTable.setItems(dataBill);
        billTable.setPlaceholder(new Label("Nejsou tu žádné položky"));
        billIndexColumn.setSortable(false);

        billIndexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Bill, Number> column) -> new ReadOnlyObjectWrapper<>(billTable.getItems().indexOf(column.getValue()) + 1));
        billNumberColumn.setCellValueFactory((TableColumn.CellDataFeatures<Bill, String> param) -> new ReadOnlyObjectWrapper(
                param.getValue().getNumberBill()
        ));

        billTimeColumn.setCellValueFactory((TableColumn.CellDataFeatures<Bill, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getTime()));

        billDateColumn.setCellValueFactory((TableColumn.CellDataFeatures<Bill, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getDate()));

        billTable.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    String id_bill = billNumberColumn.getCellData(billTable.getSelectionModel().getSelectedIndex());
                    Stage stage = new Stage(StageStyle.UTILITY);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views.store/BillDetail.fxml"));
                    Parent root = fxmlLoader.load();
                    BillDetailController controller = fxmlLoader.<BillDetailController>getController();
                    controller.setBillNumber(billData.getId(id_bill));
                    Scene scene = new Scene(root);
                    stage.setTitle("Účtenka: ");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.sizeToScene();
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(BillTodayController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        billTimeColumn.setSortType(TableColumn.SortType.DESCENDING);
        billTable.getSortOrder().add(billTimeColumn);
        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, (evt) -> {
            if (evt.getCode() == KeyCode.ESCAPE) {
                Stage stage = (Stage) billTable.getScene().getWindow();
                stage.close();
            }
        });
        offlineEET = new OfflineEET(eetTable, IndexColumn, eetNumberColumn, eetDateColumn, eetTimeColumn, sendColumn);
    }

    public void filterBill() {
        FilteredList<Bill> filteredData = new FilteredList<>(billData.getBillList(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        billFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bill -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (bill.getNumberBill().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (bill.getNumberBill().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Bill> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(billTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        billTable.setItems(sortedData);

    }

    @FXML
    public void pressF1() {

    }
}
