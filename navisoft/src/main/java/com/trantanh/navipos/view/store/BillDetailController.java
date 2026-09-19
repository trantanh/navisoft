package com.trantanh.navipos.view.store;

import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.dao.impl.BillDaoImpl;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.PrinterForm;
import com.trantanh.navipos.service.impl.PrinterFormImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class BillDetailController implements Initializable {

    @FXML
    private Label numberBillLabel;

    @FXML
    private TableView<Product> billTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> barcodeColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TableColumn<Product, Number> indexColumn;

    @FXML
    private TableColumn<Product, String> quantityColummn;

    @FXML
    private Label fikLabel;

    @FXML
    private Label bkpLabel;

    private BillDaoImpl billData;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label returnMoneyLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label acceptMoneyLabel;

    @FXML
    private Label timeLabel;

    private String idBill;

    private String pkp;

    @FXML
    private Label porad_cisLabel;

    @FXML
    private Label dan1Label;

    @FXML
    private Label dan2Label;

    @FXML
    private Label zakl_dan1Label;

    @FXML
    private Label zakl_dan2Label;

    private ConfigManager configManager;

    private PrinterForm printerForm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configManager = new ConfigManager();
        billData = new BillDaoImpl();
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, Number> column) -> new ReadOnlyObjectWrapper<>(billTable.getItems().indexOf(column.getValue()) + 1));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColummn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void setBillNumber(int numberBill) {
        this.idBill = String.valueOf(numberBill);
        numberBillLabel.setText(String.valueOf(numberBill));
        billTable.getItems().addAll(billData.getBillProductList(numberBill));
        billData = new BillDaoImpl();
        Bill bill = billData.getBill(String.valueOf(numberBill));
        if (bill != null) {
            totalPriceLabel.setText(bill.getTotalPrice());
            returnMoneyLabel.setText(bill.getReturnPrice());
            nameLabel.setText(bill.getPersonName());
            dateLabel.setText(bill.getDate());
            timeLabel.setText(bill.getTime());
            acceptMoneyLabel.setText(bill.getAcceptMoney());
            fikLabel.setText(bill.getFik());
            bkpLabel.setText(bill.getBkp());
            porad_cisLabel.setText(bill.getPoradiCis());
            this.pkp = bill.getPkp();
            if (configManager.getTax().equals("1")) {
                dan1Label.setText(bill.getDan1());
                dan2Label.setText(bill.getDan2());
                zakl_dan1Label.setText(bill.getZakl_dan1());
                zakl_dan2Label.setText(bill.getZakl_dan2());
            } else {
                dan1Label.setText("0.00");
                dan2Label.setText("0.00");
                zakl_dan1Label.setText("0.00");
                zakl_dan2Label.setText("0.00");
            }
        } else {
            AlertDialogUtils.getWarning("hyba účteneky", "Účtenky je prazdný", "Vyberte něco jiného");
        }
    }

    @FXML
    public void printBill() {
        String date = dateLabel.getText() + " " + timeLabel.getText();
        printerForm = new PrinterFormImpl(idBill, billTable.getItems(), date, fikLabel.getText(), bkpLabel.getText(), pkp, totalPriceLabel.getText(), acceptMoneyLabel.getText(), returnMoneyLabel.getText(), zakl_dan2Label.getText(), dan2Label.getText(), zakl_dan1Label.getText(), dan1Label.getText());
        if (!billTable.getItems().isEmpty()) {
            if (configManager.getTax().equals("1")) {
                printerForm.printBillWithTax();
            } else {
                printerForm.printBillWithoutTax();
            }
        }
    }
}
