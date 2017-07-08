package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionNumberServantController {
    private MainGUI mainGUI;

    @FXML private AnchorPane numberServant;
    @FXML protected Slider servant;

    @FXML private void sendClicked(){
        int number = (int) servant.getValue();
        mainGUI.getFromGuiToServer().put(Integer.toString(number));
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeNumberServant();
    }

    private void closeNumberServant(){
        BorderPane parent = (BorderPane) numberServant.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
