package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
@SuppressWarnings("restriction")
public class ActionChooseColoredFamilyController {
    private MainGUI mainGUI;

    @FXML private AnchorPane coloredFamilyPane;
    @FXML private Button black;
    @FXML private Button white;
    @FXML private Button orange;
    
    @FXML private void blackClicked(){
        mainGUI.getFromGuiToServer().put("1");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeColoredFamily();
    }

    @FXML private void whiteClicked(){
        mainGUI.getFromGuiToServer().put("2");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeColoredFamily();
    }

    @FXML private void orangeClicked(){
        mainGUI.getFromGuiToServer().put("3");
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeColoredFamily();
    }

    private void closeColoredFamily(){
        BorderPane parent = (BorderPane) coloredFamilyPane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
