package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class WaitingRoomController {
    private Main main;

    @FXML private Button logoutButton;
    @FXML private Text waitingMessageText;

    @FXML private void initialize(){
        // TODO inizializza
    }

    @FXML protected void logoutClick() throws Exception{
        main.getFromGuiToServer().put("/logout");
        String result = main.getFromServerToGui().get();

        main.showLogin();
    }

    public void setMessageText(String message){
        waitingMessageText.setText(message);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
