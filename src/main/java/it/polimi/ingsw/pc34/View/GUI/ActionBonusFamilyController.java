package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionBonusFamilyController {
    private Main main;

    @FXML private AnchorPane bonusPane;
    @FXML protected Button ghostFamily;
    
    @FXML private void passClicked(){
        main.getFromGuiToServer().put("0");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeBonus();
    }

    @FXML private void startDrag(MouseEvent event){
        main.getGameViewC().setDragButton((Button) event.getSource());
        main.getGameViewC().setDropShape(null);
        ((Button) event.getSource()).startFullDrag();
    }

    private void closeBonus(){
        BorderPane parent = (BorderPane) bonusPane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
