package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
@SuppressWarnings("restriction")
public class ActionSupportVaticanController {
    private MainGUI mainGUI;

    @FXML private AnchorPane supportVatican;

    @FXML private void yesClicked(){
        mainGUI.getFromGuiToServer().put("yes");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeSupportVatican();
    }

    @FXML private void noClicked(){
        mainGUI.getFromGuiToServer().put("no");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeSupportVatican();
    }

    private void closeSupportVatican(){
        BorderPane parent = (BorderPane) supportVatican.getParent();
        parent.setCenter(null);
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
