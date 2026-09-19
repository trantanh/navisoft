package com.trantanh.navipos.view.sales;

import com.trantanh.navipos.ControlledScreen;
import com.trantanh.navipos.ScreensController;
import com.trantanh.navipos.constants.DataScreen;
import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.service.PrinterForm;
import com.trantanh.navipos.service.SalesService;
import com.trantanh.navipos.service.impl.Printer;
import com.trantanh.navipos.service.impl.PrinterFormImpl;
import com.trantanh.navipos.service.impl.SalesServiceImpl;
import com.trantanh.navipos.utils.AlertDialogUtils;
import com.trantanh.navipos.utils.DateUtils;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class SalesController implements Initializable, ControlledScreen {

    private final int START_YEAR = 2020;
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private ScreensController myController;

    @FXML
    private Label todayLabel;

    @FXML
    private Label todayPriceLabel;

    @FXML
    private Label datePriceLabel;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalPriceByCardLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label dan1Label;
    @FXML
    private Label dan2Label;
    @FXML
    private Label zakl_dan1Label;
    @FXML
    private Label zakl_dan2Label;
    @FXML
    private Label dan1ChooseDayLabel;
    @FXML
    private Label dan2ChooseDayLabel;
    @FXML
    private Label zakl_dan1ChooseDayLabel;
    @FXML
    private Label zakl_dan2ChooseDayLabel;
    @FXML
    private Label totalPriceChooseDayLabel;
    @FXML
    private Label chooseDayLabel;


    ObservableList<String> months
            = FXCollections.observableArrayList(
            "Leden",
            "Únor", "Březen", "Duben", "Květen",
            "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"
    );
    ObservableList<Integer> year = FXCollections.observableArrayList();

    @FXML
    private ComboBox monthComboBox;

    @FXML
    private ComboBox yearComboBox;

    @FXML
    private Label monthPriceLabel;

    private ScreenCreatorUtils screenCreator;

    private SalesService salesService;

    private PrinterForm printer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (int i = 0; i < 100; i++) {
            year.add(START_YEAR + i);
        }
        monthComboBox.setItems(months);
        yearComboBox.setItems(year);
        monthComboBox.getSelectionModel().select(Calendar.getInstance().get(Calendar.MONTH));
        yearComboBox.getSelectionModel().selectFirst();
        salesService = SalesServiceImpl.getInstance();
        todayPriceLabel.setText(salesService.getCurrentTotalPrice());
        todayLabel.setText(DateUtils.formatDate(new Date()));
        setFormatDatePicker();
        printer = new PrinterFormImpl();
    }

    @FXML
    public void getMonthPrice() {
        monthTotalPrice();
    }

    private void monthTotalPrice() {
        int month = monthComboBox.getSelectionModel().getSelectedIndex() + 1;
        int year = (Integer.valueOf(yearComboBox.getSelectionModel().getSelectedItem().toString()));
        SalesDetail salesDetail = salesService.getMonthTotalPrice(month, year);
        infoDetail(salesDetail.getTotalPrice(), salesDetail.getDan1(), salesDetail.getDan2(), salesDetail.getZakl_dan1(), salesDetail.getZakl_dan2(), month + " " + year, salesDetail.getPayByCard());
        monthPriceLabel.setText(salesDetail.getTotalPrice() + " CZK");
    }

    private void infoDetail(String totalPrice, String dan1, String dan2, String zakl_dan1,String zakl_dan2, String date, String payByCard){
        dan1Label.setText(dan1 + " CZK");
        dan2Label.setText(dan2 + " CZK");
        zakl_dan1Label.setText(zakl_dan1 + " CZK");
        zakl_dan2Label.setText(zakl_dan2 + " CZK");
        todayPriceLabel.setText(totalPrice + " CZK");
        chooseDayLabel.setText(date);
        totalPriceByCardLabel.setText(payByCard + " CZK");
    }

    public void setFormatDatePicker() {
        fromDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

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
        });
        toDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

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
        });
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    SalesDetail salesDetail = salesService.getCurrentData(java.sql.Date.valueOf(date));
                    dan1ChooseDayLabel.setText(salesDetail.getDan1() + " CZK");
                    dan2ChooseDayLabel.setText(salesDetail.getDan2() + " CZK");
                    zakl_dan1ChooseDayLabel.setText(salesDetail.getZakl_dan1() + " CZK");
                    zakl_dan2ChooseDayLabel.setText(salesDetail.getZakl_dan2() + " CZK");
                    totalPriceChooseDayLabel.setText(salesDetail.getTotalPrice() + " CZK");
                    chooseDayLabel.setText(dateFormatter.format(date));
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
        });
    }

    @FXML
    public void getTotalPrice() {
        totalPriceLabel.setText("0.00 CZK");
        if (fromDatePicker.getValue().toString() != null) {
            String fromDate = fromDatePicker.getValue().toString();
            String toDate = toDatePicker.getValue().toString();
            double total_price = salesService.getTotalPrice(fromDate, toDate);
            if (total_price != 0) {
                totalPriceLabel.setText(total_price + "0 CZK");
            } else {
                AlertDialogUtils.getWarning("POZOR", "Není tu nic", "Od " + fromDate + " do " + toDate + " Nic tu není! ");
            }
        } else {
            AlertDialogUtils.getWarning("POZOR", "Zapomněl jste vyplnit datum", "Prosím vyplňte datum ");
        }
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    public void updateCurrentPrice() {
        SalesDetail salesDetail = salesService.getTodaySale();
        dan1Label.setText(salesDetail.getDan1() + " Kc");
        dan2Label.setText(salesDetail.getDan2() + " Kc");
        zakl_dan1Label.setText(salesDetail.getZakl_dan1() + " Kc");
        zakl_dan2Label.setText(salesDetail.getZakl_dan2() + " Kc");
        totalPriceByCardLabel.setText(salesDetail.getPayByCard() + " Kc");
        todayPriceLabel.setText(salesDetail.getTotalPrice() + " Kc");
    }

    @FXML
    public void goToStatistics() {
        ScreenCreatorUtils.getScreen(DataScreen.GO_TO_SALES_VIEW, "Tržby");
    }

    @FXML
    public void printToday() {
        printer.printDaySales(dan1Label.getText(), zakl_dan1Label.getText(), dan2Label.getText(), zakl_dan2Label.getText(), todayPriceLabel.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT).format(LocalDate.now()), totalPriceByCardLabel.getText());

    }

    @FXML
    public void printSalesDay() {
        printer.printDaySales(dan1ChooseDayLabel.getText(), zakl_dan1ChooseDayLabel.getText(), dan2ChooseDayLabel.getText(), zakl_dan2ChooseDayLabel.getText(), totalPriceChooseDayLabel.getText(), chooseDayLabel.getText(), totalPriceByCardLabel.getText());

    }

    @FXML
    public void printingPrice() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printer(totalPriceLabel.getText()));
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
