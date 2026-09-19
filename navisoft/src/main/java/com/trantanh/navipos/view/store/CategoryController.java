package com.trantanh.navipos.view.store;

import java.net.URL;

import com.trantanh.navipos.service.CategoryService;
import com.trantanh.navipos.service.impl.CategoryServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.trantanh.navipos.model.Category;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class CategoryController implements Initializable {

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> dphColumn;

    @FXML
    private TableColumn<Category, Number> indexColumn;

    @FXML
    private TableColumn<Category, String> numberColumn;

    @FXML
    private TableColumn<Category, String> createdColumn;

    private CategoryService categoryData;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryData = CategoryServiceImpl.getInstance();
        categoryTable.getItems().addAll(categoryData.getCategoryList());
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dphColumn.setCellValueFactory(new PropertyValueFactory<>("dph"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        dphColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyboardShort();
        editColumn();
    }

    public void keyboardShort() {
        categoryTable.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                int i = categoryTable.getSelectionModel().getSelectedIndex();
                categoryData.deleteCategory(nameColumn.getCellData(i));
                categoryTable.getItems().clear();
                categoryTable.getItems().addAll(categoryData.getCategoryList());
            }
        });
    }
    
    public void editColumn() {
        dphColumn.setOnEditCommit((TableColumn.CellEditEvent<Category, String> event) -> {
            int i = categoryTable.getSelectionModel().getSelectedIndex();
            categoryData.saveOrUpdate(nameColumn.getCellData(i), event.getNewValue());
            categoryTable.getItems().clear();
            categoryTable.getItems().addAll(categoryData.getCategoryList());
        });
    }

}
