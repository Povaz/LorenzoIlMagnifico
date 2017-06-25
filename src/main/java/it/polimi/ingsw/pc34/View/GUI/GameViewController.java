package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class GameViewController {
    private Main main;

    @FXML private ImageView territorySpot0;
    @FXML private AnchorPane territoryTowerCard3;
    @FXML private Button bt;

    @FXML private void initialize(){
    }

    @FXML private void zoomCard(){

    }

    @FXML private void bTP(){
        upgrade("upgrade");
    }

    @FXML public void upgrade(String string){
        if(string.equals("")){
            territorySpot0.setVisible(false);
        }
        else{
            territorySpot0.setImage(new Image("it/polimi/ingsw/pc34/View/GUI/pngFiles/devcards_f_en_c_1.png"));
            territorySpot0.setVisible(true);
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
