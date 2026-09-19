package com.trantanh.navipos.view.cashdesk;

import com.trantanh.navipos.config.SpringContext;

import com.trantanh.eet.v2.EetSubmissionService;

import com.trantanh.navipos.ControlledScreen;
import com.trantanh.navipos.ScreensController;
import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.constants.DataScreen;
import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.*;
import com.trantanh.navipos.service.impl.BillServiceImpl;
import com.trantanh.navipos.service.impl.PrinterFormImpl;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.service.impl.SalesServiceImpl;
import com.trantanh.navipos.utils.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.trantanh.navipos.constants.DataScreen.*;
import static com.trantanh.navipos.constants.NaviPOSConstants.PRINTER_OFF;
import static com.trantanh.navipos.constants.NaviPOSConstants.PRINTER_ON;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class CashDeskController implements Initializable, ControlledScreen {

    private final static int MAX_QUANTITY = 1000;
    private final static double MAX_PRICE_PRODUCT = 10000;
    private ScreensController myController;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> quantityColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TableColumn<Product, Number> indexColumn;

    @FXML
    private TextField barcodeTextField;

    @FXML
    private TextField payTextField;

    @FXML
    private TextField valueFVTextField;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label payLabel;

    @FXML
    private Label getMoneyLabel;

    @FXML
    private Label currencyLabel;

    @FXML
    private Label itemsLabel;

    @FXML
    private Label returnMoneyLabel;

    @FXML
    private Label flashMessage;

    @FXML
    private Button button_get_product_price;

    @FXML
    private Button button_open_cash;

    @FXML
    private Button button_storno;

    @FXML
    private Button button_edit_product;

    @FXML
    private Button button_return_product;

    @FXML
    private Button button_bill;

    @FXML
    private Button button_move_paper;

    @FXML
    private Button button_bill_in;

    @FXML
    private Button redundanceButton;

    @FXML
    private Button payCardButton;

    @FXML
    Button printerButton;

    private Alert alert;

    @FXML
    private AnchorPane anchorPane;

    private String quantityProduct = "1";

    private final ObservableList<Product> data = FXCollections.observableArrayList();

    private SalesDetail salesDetail;

    private static boolean REFUNDANCE = false;
    private static boolean PAY_BY_CARD = false;

    private EetSubmissionService eetSubmissionService;

    private ConfigManager configManager;

    private SalesService salesService;

    private BillService billService;

    private PrinterForm printerForm;

    private ProductService productService = ProductServiceImpl.getInstance();

    @FXML
    private Button productButton;
    @FXML
    private Button productButton2;
    @FXML
    private Button productButton3;
    @FXML
    private Button productButton4;
    @FXML
    private Button productButton5;
    @FXML
    private Button productButton6;
    @FXML
    private Button productButton7;
    @FXML
    private Button productButton8;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        billService = BillServiceImpl.getInstance();
        salesService = SalesServiceImpl.getInstance();
        eetSubmissionService = SpringContext.getBean(EetSubmissionService.class);
        payTextField.setVisible(false);
        configManager = new ConfigManager();
        valueFVTextField.setVisible(false);
        productTable.getItems().addAll(data);
        productTable.setPlaceholder(new Label("Nejsou tu žádné položky"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Product, Number> column) -> new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(column.getValue()) + 1));
        deleteProduct();
        barcodeInit();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> barcodeTextField.requestFocus());
        new Thread(sleeper).start();
        if (configManager.getPrinter().equals("1")) {
            printerButton.setText(PRINTER_ON);
        } else {
            printerButton.setText(PRINTER_OFF);
        }
        initFavoriteProducts();

    }

    private void initFavoriteProducts() {
        List<Button> popularProducts = new ArrayList<>(Arrays.asList(productButton, productButton2,
                productButton3, productButton4,
                productButton5, productButton6, productButton7, productButton8));
        List<Product> favoriteProducts = productService.favoriteProducts();
        for (int i = 0; i < favoriteProducts.size(); i++) {
            Button button = popularProducts.get(i);
            Product product = favoriteProducts.get(i);
            button.setText(product.getName());
            button.setOnAction(event -> {
                Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getBarcode(), product.getDph(), product.getCategory(), "1", product.getPriceWithoutTax(), product.getUnit());
                setProductWithPrice(newProduct);
            });
        }
    }

    @FXML
    private void showPriceProduct() {
        ScreenCreatorUtils.getScreen(INFO_PRODUCT, "Zjistit cenu zboží");
        barcodeTextField.requestFocus();
    }

    @FXML
    private void returnProduct() {
        ScreenCreatorUtils.getScreen(CASH_DESK_RETURN_PRODUCT, "Vrátit zboží");
        barcodeTextField.requestFocus();
    }

    private void barcodeInit() {
        barcodeTextField.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            shortcuts(event);
        });
    }

    private void shortcuts(javafx.scene.input.KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                barCodeLogicCount();
                break;
            case F1:
                cashDesk();
                break;
            case F2:
                openCashDriver();
                break;
            case F3:
                stornoProduct();
                break;
            case F4:
                updateProduct();
                break;
            case F5:
                bill();
                break;
            case DOWN:
                if (!productTable.getItems().isEmpty()) {
                    productTable.requestFocus();
                }
                break;
        }
    }

    private void barCodeLogicCount() {
        if (AlphaUtils.isAlpha(barcodeTextField.getText())) {
            AlertDialogUtils.getWarning("POZOR! ", "Pouze čísla", "Nemůžete zadat :" + barcodeTextField.getText());
            barcodeTextField.clear();
            barcodeTextField.requestFocus();
        } else {
            if (barcodeTextField.getText().contains("*")) {
                String quantity = "";
                String productPrice = "";
                String[] parts = barcodeTextField.getText().split("\\*");
                //part prvni cisla mnozstvi
                //part 2 druha cisla cena zbozi
                // napr. 1*2
                quantity = parts[0];
                productPrice = parts[1];
                double price = Double.parseDouble(productPrice);
                int quantityProduct = Integer.parseInt(quantity);
                if (quantityProduct > MAX_QUANTITY || price > MAX_PRICE_PRODUCT) {
                    AlertDialogUtils.getWarning("POZOR! ", "Prekroceni limit mnozstvi produktu nebo cena", "Nemůžete zadat :" + barcodeTextField.getText());
                    barcodeTextField.clear();
                    barcodeTextField.requestFocus();
                } else {
                    Product product = productService.searchProduct("123456789");
                    if (product == null) {
                        productService.addProduct("Zbozi", "0", "123456789", "0", "Potraviny");
                        product = productService.searchProduct("123456789");
                    }
                    Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getBarcode(), product.getDph(), product.getCategory(), "1", product.getPriceWithoutTax(), product.getUnit());
                    newProduct.setPrice(DateUtils.format(price));
                    newProduct.setQuantity(quantity);
                    data.add(newProduct);
                    productTable.getItems().clear();
                    productTable.getItems().addAll(data);
                    barcodeTextField.clear();
                    getItems();
                    getTotalPrice();
                    showInfoToCustomerDisplay(newProduct, totalPriceLabel.getText());
                }
                payTextField.setVisible(false);
            } else if (barcodeTextField.getText().equals("")) {
                if (productTable.getItems().isEmpty()) {
                    AlertDialogUtils.getWarning("POZOR!", "Nelze zaplatit", "Položky jsou prazdné");
                    barcodeTextField.requestFocus();
                } else {
                    String price = PriceUtils.priceMathRound(totalPriceLabel.getText());
                    totalPriceLabel.setText(price);
                    pay();
                }
                /*mnozstvi*/
            } else if (barcodeTextField.getText().length() < 4) {
                Integer.parseInt(barcodeTextField.getText());
                quantityProduct = barcodeTextField.getText();
                barcodeTextField.clear();
            } else {
                addProduct(barcodeTextField.getText(), quantityProduct);
                returnMoneyLabel.setText("0.00");
                payTextField.setVisible(false);
            }
        }

    }

    private void showInfoToCustomerDisplay(Product product, String totalPrice){
        String productName = product.getName();
        String productPriceText = product.getPrice() + "Kc";
        if (productName.length() > 10) {
            productName = productName.substring(0, 7)+"...";
        }
        PrintTextFile.printToVFDCustomerDisplay(productName + " " + productPriceText, "Celkem " +  totalPrice + "Kc");
    }

    private void pay() {
        if (productTable != null) {
            payTextField.setVisible(true);
            payTextField.requestFocus();
            payTextField.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (AlphaUtils.isAlpha(payTextField.getText())) {
                        AlertDialogUtils.getError("POZOR! ", "Pouze čísla", "Nemůžete zadat písmena :" + payTextField.getText());
                    }
                    if (payTextField.getText().equals("")) {
                        payTextField.setText("0.00");
                        saveBill();
                        quantityProduct = "1";
                        String payTextInfo = "Zaplaceno: "+ totalPriceLabel.getText() + "Kc";
                        PrintTextFile.printToVFDCustomerDisplay(payTextInfo, "Dekujeme za nakup");
                    } else {
                        double payPrice = Double.parseDouble(payTextField.getText());
                        double totalPrice = Double.parseDouble(totalPriceLabel.getText());
                        if (payPrice >= 10000) {
                            AlertDialogUtils.getError("POZOR! ", "Čísla platy", "Nemůže být tolik  :" + payTextField.getText());
                        } else {
                            if (payPrice > totalPrice || payPrice == totalPrice) {
                                double returnMoney = payPrice - totalPrice;
                                if (returnMoney == 0) {
                                    returnMoneyLabel.setText("0.00");
                                } else {
                                    returnMoneyLabel.setText(DateUtils.format(returnMoney));
                                }
                                saveBill();
                                quantityProduct = "1";
                                String payTextInfo = "Zaplaceno: " + DateUtils.format(payPrice)+ "Kc";
                                PrintTextFile.printToVFDCustomerDisplay(payTextInfo, "Vratit: " + DateUtils.format(returnMoney) + " Kc");
                            } else {
                                double returnMoney = totalPrice - payPrice;
                                totalPriceLabel.setText(DateUtils.format(returnMoney));
                                payTextField.setText("");
                            }
                        }
                    }
                }
                if (event.getCode().equals(KeyCode.UP)) {
                    payTextField.setVisible(false);
                    barcodeTextField.requestFocus();
                }
            });
        }
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void stornoProduct() {
        if (productTable.getItems().size() == 0) {
            AlertDialogUtils.getWarning("POZOR", "NELZE STORNOVAT", "Položky jsou prazdné");
            valueFVTextField.setVisible(false);
            barcodeTextField.clear();
            barcodeTextField.requestFocus();
        } else {
            newProduct();
        }
    }

    private void newProduct() {
        clearTableProducts();
        totalPriceLabel.setText("0.00");
    }

    private void clearProduct() {
        clearTableProducts();
    }

    private void clearTableProducts() {
        data.clear();
        productTable.getItems().clear();
        productTable.getItems().addAll(data);
        itemsLabel.setText("0");
        payTextField.setText("0.00");
        payTextField.setVisible(false);
        valueFVTextField.setVisible(false);
        barcodeTextField.clear();
        barcodeTextField.requestFocus();
    }

    private void addProduct(String barcode, String quantity) {
        barcode = barcode.replaceAll("\\s", "");
        Product product = productService.searchProduct(barcode);
        if (product == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Čárový kod nenalezen");
            alert.setHeaderText("POZOR!");
            alert.setContentText("Přejete si přidat do systému? ");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        // pridat parametr do jineho scenu
                        Stage stage = new Stage(StageStyle.UTILITY);
                        FXMLLoader fxmlLoader = SpringContext.fxmlLoader(getClass().getResource(ADD_PRODUCT));
                        Parent root = fxmlLoader.load();
                        root.getStylesheets().add(CashDeskController.class.getResource("/css/navipos.css").toString());
                        AddProductDialogController controller = fxmlLoader.getController();
                        controller.setBarcode(barcodeTextField.getText());
                        Scene scene = new Scene(root);
                        controller.nameFocusTextField();
                        stage.setTitle("Přidat zboží");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(scene);
                        stage.sizeToScene();
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(CashDeskController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    alert.close();
                }
            }
            barcodeTextField.clear();
        } else {
            Product newProduct = new Product(product.getId(), product.getName(), product.getPrice(), product.getBarcode(), product.getDph(), product.getCategory(), quantity, product.getPriceWithoutTax(), product.getUnit());
            setProductWithPrice(newProduct);
            showInfoToCustomerDisplay(newProduct, totalPriceLabel.getText());
        }
    }

    private void setProductWithPrice(Product product) {
        if (product.getPrice().equals("0.00")) {
            valueFVTextField.setVisible(true);
            valueFVTextField.requestFocus();
            valueFVTextField.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (valueFVTextField.getText().isEmpty() || AlphaUtils.isAlpha(valueFVTextField.getText())) {
                        valueFVTextField.setText("");
                    } else {
                        product.setQuantity(quantityProduct);
                        product.setPrice(valueFVTextField.getText());
                        data.add(product);
                        productTable.getItems().clear();
                        productTable.getItems().addAll(data);
                        barcodeTextField.setText("");
                        getItems();
                        getTotalPrice();
                        quantityProduct = "1";
                        valueFVTextField.setText("");
                        valueFVTextField.setVisible(false);
                        barcodeTextField.requestFocus();
                        showInfoToCustomerDisplay(product, totalPriceLabel.getText());
                    }
                }
            });
        } else {
            data.add(product);
            productTable.getItems().clear();
            productTable.getItems().addAll(data);
            barcodeTextField.setText("");
            getItems();
            getTotalPrice();
            quantityProduct = "1";
        }
    }

    private void getTotalPrice() {
        if (data.isEmpty()) {
            totalPriceLabel.setText("0.00");
        } else {
            double amount;
            double price;
            double total = 0.00;
            for (int i = 0; i < data.size(); i++) {
                amount = Double.parseDouble(quantityColumn.getCellData(i));
                price = Double.parseDouble(priceColumn.getCellData(i));
                total += amount * price;
            }
            totalPriceLabel.setText(DateUtils.format(total));
        }
    }

    private void getItems() {
        int i = 0;
        i = data.stream().map((p) -> Integer.valueOf(p.getQuantity())).reduce(i, Integer::sum);
        itemsLabel.setText(String.valueOf(i));
    }

    private void deleteProduct() {
        productTable.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                Product p = productTable.getSelectionModel().getSelectedItem();
                data.remove(p);
                productTable.getItems().remove(p);
                getTotalPrice();
                getItems();
                barcodeTextField.requestFocus();
            }
            if (event.getCode().equals(KeyCode.UP)) {
                int i = productTable.getSelectionModel().getSelectedIndex();
                if (i == 0) {
                    barcodeTextField.requestFocus();
                }
            }
        });
    }

    @FXML
    public void openCashDriver() {
        PrintTextFile.openCashDriwer();
        barcodeTextField.requestFocus();
    }

    private void saveBill() {
        String billNumber = DateUtils.getBillId();
        String namePerson = configManager.getLogin();
        String printer = configManager.getPrinter();
        int billId = 0;
        if (!data.isEmpty()) {
            if (configManager.getEet().equals("1")) {
                String fik = "";
                String bkp = "";
                String pkp = "";
                int porad_cis = billService.getPoradCisel();
                porad_cis++;
                Date dateTime = new Date();
                double price = PriceUtils.totalPrice(data);
                String priceFormat = DateUtils.format(price);
                String printerDateFormat = DateUtils.billDateFormat(dateTime);
                if (configManager.getTax().equals("1")) {
                    salesDetail = TaxUtils.taxProduct(data);
                    String zaklDan1 = salesDetail.getZakl_dan1();
                    String dan1 = salesDetail.getDan1();
                    String zaklDan2 = salesDetail.getZakl_dan2();
                    String dan2 = salesDetail.getDan2();
                    if (REFUNDANCE) {
                        zaklDan1 = TaxUtils.negativeTax(zaklDan1);
                        dan1 = TaxUtils.negativeTax(dan1);
                        zaklDan2 = TaxUtils.negativeTax(zaklDan2);
                        dan2 = TaxUtils.negativeTax(dan2);
                        price *= -1;
                        priceFormat = PriceUtils.negativePrice(priceFormat);
                    }
                    billId = saveBill(billNumber, dateTime, returnMoneyLabel.getText(), priceFormat, namePerson, payTextField.getText(), fik, bkp, pkp, porad_cis, zaklDan1, dan1, zaklDan2, dan2);
                    salesService.saveSale(billId, data);
                    salesService.addTodayPrice(priceFormat);
                    if (printer.equals("1")) {
                        PrinterForm printerForm = new PrinterFormImpl(billNumber, data, printerDateFormat, fik, bkp, pkp, priceFormat, payLabel.getText(), returnMoneyLabel.getText(), zaklDan2, dan2, zaklDan1, dan1);
                        printerForm.printBillWithTax();
                    }
                    informationAboutPay();
                } else {
                    billId = saveBill(billNumber, dateTime, returnMoneyLabel.getText(), priceFormat, namePerson, payTextField.getText(), fik, bkp, pkp, porad_cis, "0.00", "0.00", "0.00", "0.00");
                    salesService.saveSale(billId, data);
                    salesService.addTodayPrice(priceFormat);
                    if (printer.equals("1")) {
                        PrinterForm printerForm = new PrinterFormImpl(billNumber, data, printerDateFormat, fik, bkp, pkp, priceFormat, payLabel.getText(), returnMoneyLabel.getText(), "", "", "", "");
                        printerForm.printBillWithoutTax();
                    }
                    informationAboutPay();
                }
            } else {
                int porad_cis = billService.getPoradCisel();
                porad_cis++;
                Date dateTime = new Date();
                double price = PriceUtils.totalPrice(data);
                String priceFormat = DateUtils.format(price);
                String printerDateFormat = DateUtils.billDateFormat(dateTime);
                if (configManager.getTax().equals("1")) {
                    salesDetail = TaxUtils.taxProduct(data);
                    String zaklDan1 = salesDetail.getZakl_dan1();
                    String dan1 = salesDetail.getDan1();
                    String zaklDan2 = salesDetail.getZakl_dan2();
                    String dan2 = salesDetail.getDan2();
                    if (REFUNDANCE) {
                        zaklDan1 = TaxUtils.negativeTax(zaklDan1);
                        dan1 = TaxUtils.negativeTax(dan1);
                        zaklDan2 = TaxUtils.negativeTax(zaklDan2);
                        dan2 = TaxUtils.negativeTax(dan2);
                        price *= -1;
                        priceFormat = PriceUtils.negativePrice(priceFormat);
                    }
                    billId = saveBill(billNumber, dateTime, returnMoneyLabel.getText(), priceFormat, namePerson, payTextField.getText(), "", "", "", porad_cis, zaklDan1, dan1, zaklDan2, dan2);
                    salesService.saveSale(billId, data);
                    salesService.addTodayPrice(priceFormat);
                    if (printer.equals("1")) {
                        PrinterForm printerForm = new PrinterFormImpl(billNumber, data, printerDateFormat, "", "", "", priceFormat, payLabel.getText(), returnMoneyLabel.getText(), zaklDan2, dan2, zaklDan1, dan1);
                        printerForm.printBillWithTax();
                    }
                    informationAboutPay();
                } else {
                    billId = saveBill(billNumber, dateTime, returnMoneyLabel.getText(), priceFormat, namePerson, payTextField.getText(), "", "", "", porad_cis, "0.00", "0.00", "0.00", "0.00");
                    salesService.saveSale(billId, data);
                    salesService.addTodayPrice(priceFormat);
                    if (printer.equals("1")) {
                        PrinterForm printerForm = new PrinterFormImpl(billNumber, data, printerDateFormat, "", "", "", priceFormat, payLabel.getText(), returnMoneyLabel.getText(), "", "", "", "");
                        printerForm.printBillWithoutTax();
                    }
                    informationAboutPay();
                }
            }
        }

    }

    private void informationAboutPay() {
        REFUNDANCE = false;
        PAY_BY_CARD = false;
        redundanceButton.setStyle("");
        payCardButton.setStyle("");
        AlertDialogUtils.getInformationWithTime("Platba", "Probíha zprácování platby", "Prosím vyčkejte ....", 1000);
        clearProduct();
    }

    @FXML
    public void feedPaper() {
        PrintTextFile.feedPaper();
        barcodeTextField.requestFocus();
    }

    @FXML
    public void updateProduct() {
        ScreenCreatorUtils.getScreen(CASH_DESK_EDIT_PRODUCT, "Zboží");
        barcodeTextField.requestFocus();
    }

    @FXML
    public void bill() {
        ScreenCreatorUtils.getScreen(BILL_RESOURCE, "Dnešní účtenky");
        barcodeTextField.requestFocus();
    }

    @FXML
    public void cashDesk() {
        ScreenCreatorUtils.getScreen(CASH_DESK, "Rychla pokladna");
    }

    @FXML
    public void printerButton() {
        if (printerButton.getText().equals(PRINTER_ON)) {
            printerButton.setText(PRINTER_OFF);
            configManager.setPrinter("0");
        } else {
            printerButton.setText(PRINTER_ON);
            configManager.setPrinter("1");
        }
    }

    @FXML
    public void redundance() {
        if (!REFUNDANCE) {
            redundanceButton.setStyle("-fx-background-color:#ff0000");
            REFUNDANCE = true;
        } else {
            redundanceButton.setStyle("");
            REFUNDANCE = false;
        }
    }

    @FXML
    public void goToPriceTags() {
        ScreenCreatorUtils.getScreen(DataScreen.STORE_PRICE_TAGS, "Cenovky");
        barcodeTextField.requestFocus();
    }

    @FXML
    public void payCard() {
        if (!PAY_BY_CARD) {
            payCardButton.setStyle("-fx-background-color:green");
            PAY_BY_CARD = true;
        } else {
            payCardButton.setStyle("");
            PAY_BY_CARD = false;
        }
        barcodeTextField.requestFocus();
    }

    private int saveBill(String numberBill, Date date, String returnMoney, String totalPrice, String person, String acceptMoney, String FIK, String BKP, String PKP, int poradCis, String dan1, String zaklDan1, String dan2, String zaklDan2) {
        int billId = billService.createBill(numberBill, date, returnMoney, totalPrice, person, acceptMoney,
                FIK, BKP, PKP, poradCis, zaklDan1, dan1, zaklDan2, dan2, PAY_BY_CARD);
        if (billId > 0 && "1".equals(configManager.getEet())) {
            eetSubmissionService.enqueue(
                    billId,
                    Integer.toString(poradCis),
                    date.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().withNano(0),
                    new BigDecimal(totalPrice)
            ).ifPresentOrElse(
                    ignored -> { },
                    () -> flashMessage.setText("Účtenka byla uložena, ale chybí konfigurace EET")
            );
        }
        return billId;
    }
}
