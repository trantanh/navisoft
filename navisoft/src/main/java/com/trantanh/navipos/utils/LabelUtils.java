package com.trantanh.navipos.utils;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 07.12.2019
 */
public class LabelUtils {

    private LabelUtils() {
    }

    public static void informationLabel(Label label, String text, Color color) {
        label.setTextFill(color);
        label.setText(text);
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
}
