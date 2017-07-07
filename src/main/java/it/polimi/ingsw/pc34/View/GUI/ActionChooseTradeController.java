package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionChooseTradeController {
    private Main main;

    @FXML private AnchorPane tradePane;
    @FXML protected ImageView tradeCard;
    @FXML protected Button second;
    
    @FXML private void firstClicked(){
        main.getFromGuiToServer().put("0");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeTrade();
    }

    @FXML private void secondClicked(){
        main.getFromGuiToServer().put("1");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeTrade();
    }

    @FXML private void noClicked(){
        main.getFromGuiToServer().put("-1");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeTrade();
    }

    private void closeTrade(){
        BorderPane parent = (BorderPane) tradePane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
