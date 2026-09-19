package com.trantanh.navisoft.restaurant.app;

import com.trantanh.navisoft.restaurant.configraution.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author Ivan Tran, tran.tuan.anh@starkysclub.com
 * 21.09.2018
 */
@SpringBootApplication
@Import(AppConfig.class)
public class RestaurauntApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        springContext = SpringApplication.run(RestaurauntApp.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/RestaurantView.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("NAVISOFT Restaurant");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(RestaurauntApp.class, args);
    }
}
