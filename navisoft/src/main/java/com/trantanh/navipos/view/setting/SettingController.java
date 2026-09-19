package com.trantanh.navipos.view.setting;

import com.trantanh.eet.impl.EetConnect;
import com.trantanh.navipos.ControlledScreen;
import com.trantanh.navipos.ScreensController;
import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.dao.DataDao;
import com.trantanh.navipos.dao.PersonDao;
import com.trantanh.navipos.dao.UserDao;
import com.trantanh.navipos.dao.impl.DataDaoImpl;
import com.trantanh.navipos.dao.impl.PersonDaoImpl;
import com.trantanh.navipos.dao.impl.UserDaoImpl;
import com.trantanh.navipos.model.Data;
import com.trantanh.navipos.model.Person;
import com.trantanh.navipos.model.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.trantanh.navipos.constants.NaviPOSConstants.PLAYGROUND;
import static com.trantanh.navipos.constants.NaviPOSConstants.PRINTER_OFF;
import static com.trantanh.navipos.constants.NaviPOSConstants.PRINTER_ON;
import static com.trantanh.navipos.constants.NaviPOSConstants.PRODUCTION;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class SettingController implements Initializable, ControlledScreen {

    private ScreensController myController;

    private DataDao dataDao;

    @FXML
    private TextField shopTextField;
    @FXML
    private TextField fistnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField postalcodeTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField icoTextField;

    @FXML
    private TextField dicTextField;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField passwordTextField2;

    @FXML
    private ChoiceBox roleChoiceBox;

    @FXML
    private Label flashMessage;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, Number> indexColumn;

    @FXML
    private Label informationLabel;

    private final ObservableList<User> data = FXCollections.observableArrayList();

    @FXML
    private Label personMessage;

    @FXML
    private Desktop desktop;

    @FXML
    private TextField dicEetTextField;
    @FXML
    private TextField storeTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button openButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Label validityLabel;

    @FXML
    private TextField cashTextField;

    @FXML
    private Button taxButton;

    public ConfigManager confing;

    @FXML
    private TextField porad_cisTextField;

    @FXML
    private Button eetButton;

    @FXML
    private Button printerButton;

    @FXML
    private Button typeButton;

    @FXML
    private Button dataButton;
    @FXML
    private Button mysqlButton;
    @FXML
    private Button fileButton;

    private FileChooser fileChooser;

    private PersonDao personDao;

    private UserDao userDao;

    private EetConnect eetConnect;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDao = new UserDaoImpl();
        dataDao = new DataDaoImpl();
        personDao = new PersonDaoImpl();
        userDao.findAll().stream().filter((u) -> (!u.getLogin().equals("admin"))).forEachOrdered((u) -> {
            data.add(u);
        });
        userTable.setItems(data);
        userTable.setPlaceholder(new Label("Nejsou tu žádné uživatele"));
        userTable.setEditable(true);
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory((TableColumn.CellDataFeatures<User, Number> column) -> new ReadOnlyObjectWrapper<>(userTable.getItems().indexOf(column.getValue())));
        roleChoiceBox.setItems(FXCollections.observableArrayList("uživatel", "admin"));
        roleChoiceBox.getSelectionModel().selectFirst();
        getUser();
        goToNextField();
        deleteUser();
        confing = new ConfigManager();

        try {
            eetConnect = new EetConnect(dicEetTextField, storeTextField, passwordField, openButton, saveButton, deleteButton, validityLabel, cashTextField, porad_cisTextField);
        } catch (SQLException ex) {
            Logger.getLogger(SettingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        config();
//        initData();
        dataOpenFile();
    }

    public void initData() {
        Data dataMySQL = dataDao.getData();
        mysqlButton.setText(dataMySQL.getPathMySQl());
        fileButton.setText(dataMySQL.getPathFile());
    }

    public void dataOpenFile() {
        mysqlButton.setOnAction(
                e -> {
                    fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(dicTextField.getScene().getWindow());
                    if (file != null) {
                        mysqlButton.setText(file.getAbsolutePath());
                    }
                });
        fileButton.setOnAction(
                e -> {
                    fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(dicTextField.getScene().getWindow());
                    if (file != null) {

                        fileButton.setText(file.getAbsolutePath());

                    }
                });

    }

    public void config() {
        if (confing.getTax().equals("1")) {
            taxButton.setText("Ano");
        } else {
            taxButton.setText("Ne");
        }
        if (confing.getSpaceEet().equals("1")) {
            typeButton.setText(PRODUCTION);
        } else {
            typeButton.setText(PLAYGROUND);
        }
        if (confing.getEet().equals("1")) {
            eetButton.setText("EET zapnuta");
            eetButton.setStyle("-fx-background-color:red");
        } else {
            eetButton.setText("EET vypnuta");
            eetButton.setStyle("");
        }
        if (confing.getPrinter().equals("1")) {
            printerButton.setText(PRINTER_ON);
        } else {
            printerButton.setText(PRINTER_OFF);
        }
    }

    private void deleteUser() {
        userTable.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                User u = userTable.getSelectionModel().getSelectedItem();
                if (!u.getLogin().equals("admin")) {
                    userDao.delete(u.getLogin());
                    data.remove(u);
                    informationLabel.setTextFill(Color.GREEN);
                    informationLabel.setText("Úspěšně jste smazal uživatel:" + u.getLogin());
                } else {
                    informationLabel.setTextFill(Color.RED);
                    informationLabel.setText("NELZE SMAZAT ADMIN :" + u.getLogin());
                }
            }
        });
    }

    public void goToNextField() {
        loginTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordTextField.requestFocus();
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordTextField2.requestFocus();
            }
        });

        passwordTextField2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addUser();
            }
        });
    }

    private void getUser() {
        Person person = personDao.getPerson();
        shopTextField.setText(person.getShop());
        fistnameTextField.setText(person.getFirstName());
        lastnameTextField.setText(person.getLastName());
        streetTextField.setText(person.getStreet());
        postalcodeTextField.setText(person.getPostalCode());
        cityTextField.setText(person.getCity());
        icoTextField.setText(person.getIco());
        dicTextField.setText(person.getDic());
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @FXML
    private void updatePerson() {
        Person person = personDao.getPerson();
        person.setShop(shopTextField.getText());
        person.setFirstName(fistnameTextField.getText());
        person.setLastName(lastnameTextField.getText());
        person.setStreet(streetTextField.getText());
        person.setPostalCode(postalcodeTextField.getText());
        person.setCity(cityTextField.getText());
        person.setIco(icoTextField.getText());
        person.setDic(dicTextField.getText());
        person.setCreated(new Timestamp(System.currentTimeMillis()));
        personDao.saveOrUpdate(person);
        informationLabel(personMessage, "Úspěšně jste změnil údaje ");
    }

    @FXML
    public void addUser() {
        if (isInputValid()) {
            if (passwordTextField.getText().equals(passwordTextField2.getText())) {
                if (!userDao.findUser(loginTextField.getText())) {
                    String role = (String) roleChoiceBox.getSelectionModel().getSelectedItem();
                    User user = new User();
                    user.setRole(role);
                    user.setPassword(passwordField.getText());
                    user.setLogin(loginTextField.getText());
                    userDao.add(user);
                    data.add(user);
                    flashMessage.setTextFill(Color.GREEN);
                    flashMessage.setText("Úspěšne jste přidal " + loginTextField.getText());
                    loginTextField.clear();
                    passwordTextField.clear();
                    passwordTextField2.clear();
                } else {
                    flashMessage.setTextFill(Color.RED);
                    flashMessage.setText("Uživatelské jméno je obsazeno, zvolte jiné");
                }
            } else {
                flashMessage.setTextFill(Color.RED);
                flashMessage.setText("Heslo se neshodují");
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (loginTextField.getText() == null || loginTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím login \n";
        }
        if (passwordTextField.getText() == null || passwordTextField.getText().length() == 0) {
            errorMessage += "Zadejte prosím heslo \n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            flashMessage.setTextFill(Color.RED);
            flashMessage.setText(errorMessage);
            return false;
        }
    }

    public void informationLabel(Label label, String information) {
        label.setTextFill(Color.GREEN);
        label.setText(information);
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> label.setText(""));
        new Thread(sleeper).start();
    }

    @FXML
    public void taxButton() {
        if (taxButton.getText().equals("Ano")) {
            taxButton.setText("Ne");
            confing.setTax("0");
        } else {
            taxButton.setText("Ano");
            confing.setTax("1");
        }
    }

    @FXML
    public void eetButton() {
        if (eetButton.getText().equals("EET vypnuta")) {
            eetButton.setText("EET zapnuta");
            eetButton.setStyle("-fx-background-color:red");
            confing.setEet("1");
        } else {
            eetButton.setText("EET vypnuta");
            eetButton.setStyle("");
            confing.setEet("0");
        }
    }

    @FXML
    public void printerButton() {
        if (printerButton.getText().equals("Tiskarna zapnuta")) {
            printerButton.setText("Tiskarna vypnuta");
            confing.setPrinter("0");
        } else {
            printerButton.setText("Tiskarna zapnuta");
            confing.setPrinter("1");
        }
    }

    @FXML
    public void typeButton() {
        if (typeButton.getText().equals(PRODUCTION)) {
            typeButton.setText(PLAYGROUND);
            confing.setSpaceEet("0");
        } else {
            typeButton.setText(PRODUCTION);
            confing.setSpaceEet("1");
        }
    }

    @FXML
    public void dataButton() {
        dataDao.addData(fileButton.getText(), mysqlButton.getText());
    }
}
