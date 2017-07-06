package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Created by trill on 03/07/2017.
 */
public class ActionExchangePrivilegeController {
    private Main main;

    @FXML private AnchorPane exchangePrivilege;
    @FXML protected Text number;

    @FXML private CheckBox woodStone;
    @FXML private CheckBox servant;
    @FXML private CheckBox coin;
    @FXML private CheckBox militaryPoint;
    @FXML private CheckBox faithPoint;

    @FXML private void sendClicked(){
        int quantity = Integer.valueOf(number.getText());
        String toSend = "";
        int count = 0;
        if(woodStone.isSelected()){
            toSend += "1";
            count++;
        }
        if(servant.isSelected()){
            toSend += "2";
            count++;
        }
        if(coin.isSelected()){
            toSend += "3";
            count++;
        }
        if(militaryPoint.isSelected()){
            toSend += "4";
            count++;
        }
        if(faithPoint.isSelected()){
            toSend += "5";
            count++;
        }
        if(count != quantity){
            return;
        }

        main.getFromGuiToServer().put(toSend);
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        closeExchangePrivilege();
    }

    private void closeExchangePrivilege(){
        BorderPane parent = (BorderPane) exchangePrivilege.getParent();
        parent.setDisable(true);
        parent.setVisible(false);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
