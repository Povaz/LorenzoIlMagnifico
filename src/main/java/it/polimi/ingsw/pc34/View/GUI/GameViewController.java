package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class GameViewController {
    private Main main;

    @FXML private Button zoomedCard;

    @FXML private ImageView territorySpot0;
    @FXML private AnchorPane territoryTowerCard3;
    @FXML private Button bt;
    @FXML private Button buildingSpotCard0;

    @FXML private void initialize(){
    	// add in .fxml per settare le dimensioni dell'immagine
    	// <Image url="@pngFiles/Board.png" requestedHeight="1046.0" requestedWidth="716.0" />
    }

    @FXML private void zoomCard(MouseEvent event){
        Button button = (Button) event.getSource();
        String imagePath = ((LocatedImage) button.getBackground().getImages().get(0).getImage()).getURL();

        List<BackgroundFill> paints = new ArrayList<>();
        paints.add(new BackgroundFill(new Color(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY));

        List<BackgroundImage> images = new ArrayList<>();
        images.add(new BackgroundImage(new LocatedImage(imagePath), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        zoomedCard.setBackground(new Background(paints, images));
        zoomedCard.setDisable(false);
        zoomedCard.setVisible(true);
    }

    @FXML private void zoomOut(){
        zoomedCard.setDisable(true);
        zoomedCard.setVisible(false);
    }

    @FXML private void bTP(){
        Image image = new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/devcards_f_en_c_1.png", 100, 100, false, false);
        Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        buildingSpotCard0.setBackground(background);
        buildingSpotCard0.setDisable(false);
        buildingSpotCard0.setVisible(true);
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
