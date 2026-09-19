package com.trantanh.eet.table;

import com.trantanh.navipos.config.SpringContext;

import com.trantanh.navipos.service.BillService;
import com.trantanh.navipos.service.impl.BillServiceImpl;
import com.trantanh.navipos.view.cashdesk.BillTodayController;
import com.trantanh.navipos.view.store.BillDetailController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class OfflineEET {

    private TableView<Eet> eetTable;

    private TableColumn<Eet, Number> indexColumn;

    private TableColumn<Eet, String> eetNumberColumn;

    private TableColumn<Eet, String> eetDateColumn;

    private TableColumn<Eet, String> eetTimeColumn;

    private TableColumn<Eet, String> sendColumn;

    final ObservableList<Eet> data = FXCollections.observableArrayList();

    public OfflineEET(TableView<Eet> eetTable, TableColumn<Eet, Number> indexColumn, TableColumn<Eet, String> eetNumberColumn, TableColumn<Eet, String> eetDateColumn, TableColumn<Eet, String> eetTimeColumn, TableColumn<Eet, String> sendColumn) {
        this.eetTable = eetTable;
        this.indexColumn = indexColumn;
        this.eetNumberColumn = eetNumberColumn;
        this.eetDateColumn = eetDateColumn;
        this.eetTimeColumn = eetTimeColumn;
        this.sendColumn = sendColumn;
        init();
    }

    private void init() {
        
        BillService billService = BillServiceImpl.getInstance();
        data.addAll(billService.getCurrentBillOfflineList());
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Eet, Number> column) -> new ReadOnlyObjectWrapper<>(eetTable.getItems().indexOf(column.getValue()) + 1));
        eetTimeColumn.setCellValueFactory((TableColumn.CellDataFeatures<Eet, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getTime()));
        eetNumberColumn.setCellValueFactory((TableColumn.CellDataFeatures<Eet, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getNumberBill()));
        eetDateColumn.setCellValueFactory((TableColumn.CellDataFeatures<Eet, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getDate()));
        sendColumn.setCellValueFactory((TableColumn.CellDataFeatures<Eet, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getStatus()));
        eetTable.setItems(data);
        
        eetTable.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    String id_bill = eetNumberColumn.getCellData(eetTable.getSelectionModel().getSelectedIndex());

                    Stage stage = new Stage(StageStyle.UTILITY);
                    FXMLLoader fxmlLoader = SpringContext.fxmlLoader(getClass().getResource("/views.store/BillDetail.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    BillDetailController controller = fxmlLoader.<BillDetailController>getController();
                    controller.setBillNumber(billService.getId(id_bill));
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
    }

}
