package com.trantanh.navipos;

import com.trantanh.navipos.constants.NaviPOSConstants;
import com.trantanh.navipos.config.SpringContext;
import com.trantanh.navipos.utils.ScreenCreatorUtils;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Locale;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
@SpringBootApplication
public class CashDeskApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(CashDeskApp.class);

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(CashDeskApp.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(getParameters().getRaw().toArray(String[]::new));
        SpringContext.initialize(applicationContext);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting NaviPOS desktop application");
        Locale.setDefault(Locale.US);
        ScreenCreatorUtils.getScreen(NaviPOSConstants.LOGIN, NaviPOSConstants.TITLE);
    }

    @Override
    public void stop() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
