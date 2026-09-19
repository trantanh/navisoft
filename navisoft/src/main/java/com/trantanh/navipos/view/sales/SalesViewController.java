package com.trantanh.navipos.view.sales;

import com.trantanh.navipos.model.Statistics;
import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.SalesService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import com.trantanh.navipos.service.impl.SalesServiceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class SalesViewController implements Initializable {

    @FXML
    private TableView<Statistics> statisticsTable;

    @FXML
    private TableColumn<Statistics, Number> indexColumn;

    @FXML
    private TableColumn<Statistics, String> dateColumn;

    @FXML
    private TableColumn<Statistics, String> totalPriceColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox categoryComboBox;

    private CategoryService categoryData;

    private SalesService statisticsData;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryData = CategoryServiceImpl.getInstance();
        statisticsData = SalesServiceImpl.getInstance();
        categoryComboBox.setItems(categoryData.getCategoryName());
        categoryComboBox.getSelectionModel().selectFirst();
        statisticsTable.setPlaceholder(new Label("Nejsou tu žádné data"));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<Statistics, Number> column) -> new ReadOnlyObjectWrapper<>(statisticsTable.getItems().indexOf(column.getValue()) + 1));
        dateColumn.setCellValueFactory((TableColumn.CellDataFeatures<Statistics, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getCreated()));
        totalPriceColumn.setCellValueFactory((TableColumn.CellDataFeatures<Statistics, String> param) -> new ReadOnlyObjectWrapper(param.getValue().getTotal_price()));
        statisticsTable.getSortOrder().add(dateColumn);
        categoryComboBox();
    }

    @FXML
    public void categoryComboBox() {
        String name = (String) categoryComboBox.getSelectionModel().getSelectedItem();
        int getId;
        getId = categoryData.getId(name);
        statisticsTable.getItems().clear();
        statisticsTable.getItems().addAll(statisticsData.getSales(getId));
    }

}
