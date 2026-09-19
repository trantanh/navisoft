package com.trantanh.navipos.utils;

import com.trantanh.navipos.config.SpringContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tran tuan anh, tran.t.anh@email.cz
 */
public final class ScreenCreatorUtils {

    public static void getScreen(String name, String title) {
        try {
            Stage stage = new Stage(StageStyle.UTILITY);
            FXMLLoader loader = SpringContext.fxmlLoader(ScreenCreatorUtils.class.getResource(name));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ScreenCreatorUtils.class.getResource("/css/navipos.css").toString());
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(ScreenCreatorUtils.class.getResourceAsStream("/icons/logo2.png")));
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ScreenCreatorUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
