package it.polimi.ingsw.pc34.View.GUI;

import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class WaitingRoomController {
    private Main main;

    @FXML private Button logoutButton;
    @FXML private Button gameButton;
    @FXML private Text waitingMessageText;

    public void initializeThread(){
        (new Thread(){
            @Override
                public void run(){
                    String result = main.getOpenWindow().get();
                    if(result.equals("/game")){
                        logoutButton.setVisible(false);
                        logoutButton.setDisable(true);
                        gameButton.setDisable(false);
                        gameButton.setVisible(true);
                    }
                }
        }).start();
    }

    @FXML protected void logoutClick() throws Exception{
        String result = main.getFromServerToGui().get();

        main.showLogin();
    }

    @FXML private void gameClick(){
        main.showGame();
    }

    public void setMessageText(String message){
        waitingMessageText.setText(message);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
