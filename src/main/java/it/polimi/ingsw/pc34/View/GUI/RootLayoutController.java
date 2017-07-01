package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.event.EventType.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class RootLayoutController {
    private Main main;

    private MediaPlayer mediaPlayer;

    @FXML private void initialize(){
        try {
            List<MediaPlayer> music = new LinkedList<>();
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track3.mp3").toURI().toString())));

            playMusic(music);
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    @FXML private void setTrack1(){
        try {
            List<MediaPlayer> music = new LinkedList<>();
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track1_1.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track1_2.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track1_3.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track1_4.mp3").toURI().toString())));

            playMusic(music);
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    @FXML private void setTrack2(){
        try {
            List<MediaPlayer> music = new LinkedList<>();
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/track2.mp3").toURI().toString())));

            playMusic(music);
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    private void playMusic(List<MediaPlayer> music){
        if(music == null || music.size() <= 0){
            return;
        }
        stopMusic();
        mediaPlayer = music.get(0);
        mediaPlayer.play();
        for(int i = 0; i < music.size(); i++){
            final MediaPlayer nextPlayer = music.get((i + 1) % music.size());
            mediaPlayer.setOnEndOfMedia(() -> {
                stopMusic();
                mediaPlayer = nextPlayer;
                mediaPlayer.play();
            });
        }
    }

    @FXML private void setMute(){
        stopMusic();
    }

    private void stopMusic(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    @FXML private void keyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ESCAPE){
            setFullScreenOff();
        }
    }

    @FXML private void setFullScreenOn(){
        if(main.isCanBeFullScreen()){
            main.getPrimaryStage().setFullScreen(true);
        }
    }

    @FXML private void setFullScreenOff(){
        if(main.isCanBeFullScreen()){
            main.getPrimaryStage().setFullScreen(false);
            main.getPrimaryStage().setWidth(main.getWindowWidth());
            main.getPrimaryStage().setHeight(main.getWindowHeight());
            main.getPrimaryStage().setX(0);
            main.getPrimaryStage().setY(0);
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
