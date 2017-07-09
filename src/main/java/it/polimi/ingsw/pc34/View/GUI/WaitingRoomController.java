package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public class WaitingRoomController {
    private MainGUI mainGUI;

    @FXML private Button logoutButton;
    @FXML private Button gameButton;
    @FXML private Text waitingMessageText;

    public void initializeThread(){
        (new Thread(() -> {
            String result;
            do {
                result = mainGUI.getOpenWindow().get();
                final String toLambda = result;

                Platform.runLater(() -> {
                    if(toLambda.equals("/login")){
                        mainGUI.showLogin();
                    }
                    else if(toLambda.equals("/game")){
                        mainGUI.showGame();
                    }
                });
            } while(!(result.equals("/login") || result.equals("/game")));
        })).start();
    }

    @FXML protected void logoutClick() throws Exception{
        mainGUI.getFromGuiToServer().put("/logout");
        @SuppressWarnings("unused")
		String result = mainGUI.getFromServerToGui().get();
    }

    public void setMessageText(String message){
        waitingMessageText.setText(message);
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
