package com.trantanh.navipos.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * @author tran tuan anh, tran.t.anh@email.cz
 */
public class SoundUtils {

    private SoundUtils() {
    }

    public static void getSound(String path) {
        File file = new File(path);
        if(file.exists()) {
            Media sound = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    public static void main(String[] args) {
        String sound = SoundUtils.class.getResource("/hang.mp3").getPath();
        getSound(sound);
    }
}
