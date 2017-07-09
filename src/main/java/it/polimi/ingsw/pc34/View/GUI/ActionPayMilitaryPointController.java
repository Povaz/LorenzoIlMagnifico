package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
@SuppressWarnings("restriction")
public class ActionPayMilitaryPointController {
    private MainGUI mainGUI;

    @FXML private AnchorPane payMilitary;
    
    @FXML private void yesClicked(){
        mainGUI.getFromGuiToServer().put("yes");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closePayMilitary();
    }

    @FXML private void noClicked(){
        mainGUI.getFromGuiToServer().put("no");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closePayMilitary();
    }

    private void closePayMilitary(){
        BorderPane parent = (BorderPane) payMilitary.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
