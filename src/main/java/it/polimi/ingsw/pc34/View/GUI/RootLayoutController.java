package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RootLayoutController {
    Logger LOGGER = Logger.getLogger(RootLayoutController.class.getName());

    private MainGUI mainGUI;

    private MediaPlayer mediaPlayer;

    @FXML private void initialize(){
        setTrack1();
    }

    protected void initializeListner(){
        mainGUI.getPrimaryStage().iconifiedProperty().addListener((obsVal, oldVal, newVal) -> {
            Stage stage = mainGUI.getPrimaryStage();
            if(stage.isFullScreen() || stage.getX() < 0 || stage.getY() < 0){
                setFullScreenOff();
            }
        });

        mainGUI.getPrimaryStage().setOnCloseRequest((event) -> {
            System.exit(0);
        });
    }

    @FXML private void setTrack1(){
        try {
            List<MediaPlayer> music = new LinkedList<>();
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/AwesomeMix_1.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/AwesomeMix_2.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/AwesomeMix_3.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/AwesomeMix_4.mp3").toURI().toString())));

            playMusic(music);
        } catch(URISyntaxException e){
            LOGGER.log(Level.WARNING, "URISyntaxException AwesomeMix", e);
        }
    }

    @FXML private void setTrack2(){
        try {
            List<MediaPlayer> music = new LinkedList<>();
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/DarkSouls_1.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/DarkSouls_2.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/DarkSouls_3.mp3").toURI().toString())));
            music.add(new MediaPlayer(new Media(RootLayoutController.class.getResource("mp3Files/DarkSouls_4.mp3").toURI().toString())));

            playMusic(music);
        } catch(URISyntaxException e){
            LOGGER.log(Level.WARNING, "URISyntaxException DarkSouls", e);
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
        KeyCode key = event.getCode();
        if(key == KeyCode.ESCAPE){
            setFullScreenOff();
        }
        else if(key == KeyCode.F){
            setFullScreenOn();
        }
        else if(key == KeyCode.M){
            setMute();
        }
        else if(key == KeyCode.DIGIT1){
            setTrack1();
        }
        else if(key == KeyCode.DIGIT2){
            setTrack2();
        }
        else if(key == KeyCode.D){
            resizeWindow();
        }
    }

    @FXML private void setFullScreenOn(){
        if(mainGUI.isCanBeFullScreen()){
            mainGUI.getPrimaryStage().setFullScreen(true);
        }
    }

    @FXML protected void setFullScreenOff(){
        if(mainGUI.isCanBeFullScreen()){
            mainGUI.getPrimaryStage().setFullScreen(false);
            resizeWindow();
        }
    }

    @FXML private void resizeWindow(){
        mainGUI.getPrimaryStage().setWidth(mainGUI.getWindowWidth());
        mainGUI.getPrimaryStage().setHeight(mainGUI.getWindowHeight());
        mainGUI.getPrimaryStage().setX(0);
        mainGUI.getPrimaryStage().setY(0);
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
