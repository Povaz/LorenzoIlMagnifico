package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionSupportVaticanController {
    private Main main;

    @FXML private AnchorPane supportVatican;

    @FXML private void yesClicked(){
        main.getFromGuiToServer().put("yes");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeSupportVatican();
    }

    @FXML private void noClicked(){
        main.getFromGuiToServer().put("no");
        if(!main.getFromServerToGui().get().equals("Yes")){
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

    public void setMain(Main main){
        this.main = main;
    }
}
