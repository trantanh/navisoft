package com.trantanh.navipos.utils;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author tran tuan anh, tran.t.anh@email.cz
 */
public final class AlertDialogUtils {

    private AlertDialogUtils() {
    }

    public static void getWarning(String title, String headerText, String contentText) {
        alert(AlertType.WARNING, title, headerText, contentText);
    }

    public static void getError(String title, String headerText, String contentText) {
        alert(AlertType.ERROR, title, headerText, contentText);
    }

    public static void getInformation(String title, String headerText, String contentText) {
        alert(AlertType.INFORMATION, title, headerText, contentText);
    }

    public static void getInformationWithTime(String title, String headerText, String contentText, int time) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> alert.close());
        new Thread(sleeper).start();

    }

    public static void getConfirmation(String title, String headerText, String contentText) {
        alert(AlertType.CONFIRMATION, title, headerText, contentText);
    }

    private static Alert alert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert;
    }
}
