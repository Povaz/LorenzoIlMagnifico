package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionChooseDiscountController {
    private MainGUI mainGUI;

    @FXML private AnchorPane discountPane;
    @FXML protected GridPane discount;
    @FXML private ToggleGroup discountChoose;

    @FXML private void sendClicked(){
        int toSend = 0;
        Toggle chosen = discountChoose.getSelectedToggle();
        for(int i = 0; i < discount.getChildren().size(); i++){
            if(chosen.equals(discount.getChildren().get(i))){
                toSend = i;
            }
        }

        mainGUI.getFromGuiToServer().put(Integer.toString(toSend));
        if(!mainGUI.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeDiscount();
    }

    private void closeDiscount(){
        BorderPane parent = (BorderPane) discountPane.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
