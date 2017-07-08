package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionLeaderCardController {
    private Main main;

    @FXML private AnchorPane leaderPane;
    @FXML protected Text action;
    @FXML protected GridPane leader;
    @FXML private ToggleGroup leaderChoose;

    @FXML private void sendClicked(){
        int toSend = 0;
        Toggle chosen = leaderChoose.getSelectedToggle();
        for(int i = 0; i < leader.getChildren().size(); i++){
            if(chosen.equals(leader.getChildren().get(i))){
                toSend = i;
            }
        }

        main.getFromGuiToServer().put(Integer.toString(toSend));
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeLeader();
    }

    private void closeLeader(){
        BorderPane parent = (BorderPane) leaderPane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
