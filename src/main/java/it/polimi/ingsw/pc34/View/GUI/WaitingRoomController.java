package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class WaitingRoomController {
    private Main main;

    @FXML private Button logoutButton;
    @FXML private Button gameButton;
    @FXML private Text waitingMessageText;

    public void initializeThread(){
        (new Thread(() -> {
            String result;
            do {
                result = main.getOpenWindow().get();
                final String toLambda = result;

                Platform.runLater(() -> {
                    if(toLambda.equals("/login")){
                        main.showLogin();
                    }
                    else if(toLambda.equals("/game")){
                        main.showGame();
                    }
                });
            } while(result.equals("/login") || result.equals("/game"));
        })).start();
    }

    @FXML protected void logoutClick() throws Exception{
        main.getFromGuiToServer().put("/logout");
        String result = main.getFromServerToGui().get();
    }

    public void setMessageText(String message){
        waitingMessageText.setText(message);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
