package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionPayMilitaryPointController {
    private Main main;

    @FXML private AnchorPane payMilitary;

    @FXML protected Text militaryNeeded;
    @FXML protected Text militaryPrice;
    @FXML protected Text coin;
    @FXML protected Text wood;
    @FXML protected Text stone;
    @FXML protected Text servant;


    @FXML private void yesClicked(){
        main.getFromGuiToServer().put("yes");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closePayMilitary();
    }

    @FXML private void noClicked(){
        main.getFromGuiToServer().put("no");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closePayMilitary();
    }

    private void closePayMilitary(){
        BorderPane parent = (BorderPane) payMilitary.getParent();
        parent.setCenter(null);
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
