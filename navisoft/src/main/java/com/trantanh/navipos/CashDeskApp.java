package com.trantanh.navipos;

import com.trantanh.navipos.constants.NaviPOSConstants;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.util.Locale;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class CashDeskApp extends Application {

    private final Logger logger = org.apache.log4j.Logger.getLogger(CashDeskApp.class.getName());

    @Override
    public void start(Stage primaryStage) {
        logger.info("START NaviPOS 2024");
        Locale.setDefault(Locale.US);
        ScreenCreatorUtils.getScreen(NaviPOSConstants.LOGIN, NaviPOSConstants.TITLE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
