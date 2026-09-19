package com.trantanh.navipos;

import java.net.URL;
import java.util.ResourceBundle;

import com.trantanh.navipos.dao.UserDao;
import com.trantanh.navipos.dao.impl.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import com.trantanh.navipos.config.ConfigManager;
import static com.trantanh.navipos.constants.NaviPOSConstants.CASH_DESK_ID;
import static com.trantanh.navipos.constants.NaviPOSConstants.SETTING_ID;
import static com.trantanh.navipos.constants.NaviPOSConstants.STORE_ID;
import static com.trantanh.navipos.constants.NaviPOSConstants.SALES_ID;

/**
 * FXML Controller class
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class MenuBarController implements Initializable, ControlledScreen {

    private ScreensController myController;
    @FXML
    private Button cashDeskButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button storeButton;
    @FXML
    private Button statisticsButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfigManager configManager = new ConfigManager();
        UserDao userDao = new UserDaoImpl();
        String role = userDao.getRole(configManager.readProperty());
        if (!role.equals("admin")) {
            statisticsButton.setVisible(false);
            storeButton.setVisible(false);
            settingButton.setVisible(false);
        }
    }

    @FXML
    private void goToCaskDesk(ActionEvent event) {
        myController.setScreen(CASH_DESK_ID);
    }

    @FXML
    private void goToStatistic() {
        myController.setScreen(SALES_ID);
    }

    @FXML
    private void goToStore() {
        myController.setScreen(STORE_ID);
    }

    @FXML
    private void goToSetting() {
        myController.setScreen(SETTING_ID);
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

}
