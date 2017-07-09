package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Created by trill on 03/07/2017.
 */
@SuppressWarnings("restriction")
public class ActionBonusFamilyController {
    private MainGUI mainGUI;

    @FXML private AnchorPane bonusPane;
    @FXML protected Button ghostFamily;
    @FXML protected Text typeValue;
    
    @FXML private void passClicked(){
        mainGUI.getFromGuiToServer().put("0");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeBonus();
    }

    @FXML private void startDrag(MouseEvent event){
        mainGUI.getGameViewC().setGhost(true);
        mainGUI.getGameViewC().setDragButton((Button) event.getSource());
        mainGUI.getGameViewC().setDropShape(null);
        ((Button) event.getSource()).startFullDrag();
    }

    private void closeBonus(){
        BorderPane parent = (BorderPane) bonusPane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
