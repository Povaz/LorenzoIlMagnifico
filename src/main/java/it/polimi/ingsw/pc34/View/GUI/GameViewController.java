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

    @FXML private Button bt;

    @FXML private Button zoomedCard;

    @FXML private Button territoryTowerCard0;
    @FXML private Button territoryTowerCard1;
    @FXML private Button territoryTowerCard2;
    @FXML private Button territoryTowerCard3;
    @FXML private Button buildingTowerCard0;
    @FXML private Button buildingTowerCard1;
    @FXML private Button buildingTowerCard2;
    @FXML private Button buildingTowerCard3;
    @FXML private Button characterTowerCard0;
    @FXML private Button characterTowerCard1;
    @FXML private Button characterTowerCard2;
    @FXML private Button characterTowerCard3;
    @FXML private Button ventureTowerCard0;
    @FXML private Button ventureTowerCard1;
    @FXML private Button ventureTowerCard2;
    @FXML private Button ventureTowerCard3;

    @FXML private Button vaticanReportCard1;
    @FXML private Button vaticanReportCard2;
    @FXML private Button vaticanReportCard3;

    @FXML private Button territorySpotCard0;
    @FXML private Button territorySpotCard1;
    @FXML private Button territorySpotCard2;
    @FXML private Button territorySpotCard3;
    @FXML private Button territorySpotCard4;
    @FXML private Button territorySpotCard5;
    @FXML private Button buildingSpotCard0;
    @FXML private Button buildingSpotCard1;
    @FXML private Button buildingSpotCard2;
    @FXML private Button buildingSpotCard3;
    @FXML private Button buildingSpotCard4;
    @FXML private Button buildingSpotCard5;
    @FXML private Button characterSpotCard0;
    @FXML private Button characterSpotCard1;
    @FXML private Button characterSpotCard2;
    @FXML private Button characterSpotCard3;
    @FXML private Button characterSpotCard4;
    @FXML private Button characterSpotCard5;
    @FXML private Button ventureSpotCard0;
    @FXML private Button ventureSpotCard1;
    @FXML private Button ventureSpotCard2;
    @FXML private Button ventureSpotCard3;
    @FXML private Button ventureSpotCard4;
    @FXML private Button ventureSpotCard5;

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
        Image image = new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/devcards_f_en_c_1.png", 85, 126, false, false);
        Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        buildingSpotCard0.setBackground(background);
        buildingSpotCard0.setDisable(false);
        buildingSpotCard0.setVisible(true);

        buildingSpotCard1.setBackground(background);
        buildingSpotCard1.setDisable(false);
        buildingSpotCard1.setVisible(true);

        buildingSpotCard2.setBackground(background);
        buildingSpotCard2.setDisable(false);
        buildingSpotCard2.setVisible(true);

        buildingSpotCard3.setBackground(background);
        buildingSpotCard3.setDisable(false);
        buildingSpotCard3.setVisible(true);

        buildingSpotCard4.setBackground(background);
        buildingSpotCard4.setDisable(false);
        buildingSpotCard4.setVisible(true);

        buildingSpotCard5.setBackground(background);
        buildingSpotCard5.setDisable(false);
        buildingSpotCard5.setVisible(true);

        territorySpotCard0.setBackground(background);
        territorySpotCard0.setDisable(false);
        territorySpotCard0.setVisible(true);

        territorySpotCard1.setBackground(background);
        territorySpotCard1.setDisable(false);
        territorySpotCard1.setVisible(true);

        territorySpotCard2.setBackground(background);
        territorySpotCard2.setDisable(false);
        territorySpotCard2.setVisible(true);

        territorySpotCard3.setBackground(background);
        territorySpotCard3.setDisable(false);
        territorySpotCard3.setVisible(true);

        territorySpotCard4.setBackground(background);
        territorySpotCard4.setDisable(false);
        territorySpotCard4.setVisible(true);

        territorySpotCard5.setBackground(background);
        territorySpotCard5.setDisable(false);
        territorySpotCard5.setVisible(true);

        characterSpotCard0.setBackground(background);
        characterSpotCard0.setDisable(false);
        characterSpotCard0.setVisible(true);

        characterSpotCard1.setBackground(background);
        characterSpotCard1.setDisable(false);
        characterSpotCard1.setVisible(true);

        characterSpotCard2.setBackground(background);
        characterSpotCard2.setDisable(false);
        characterSpotCard2.setVisible(true);

        characterSpotCard3.setBackground(background);
        characterSpotCard3.setDisable(false);
        characterSpotCard3.setVisible(true);

        characterSpotCard4.setBackground(background);
        characterSpotCard4.setDisable(false);
        characterSpotCard4.setVisible(true);

        characterSpotCard5.setBackground(background);
        characterSpotCard5.setDisable(false);
        characterSpotCard5.setVisible(true);

        ventureSpotCard0.setBackground(background);
        ventureSpotCard0.setDisable(false);
        ventureSpotCard0.setVisible(true);

        ventureSpotCard1.setBackground(background);
        ventureSpotCard1.setDisable(false);
        ventureSpotCard1.setVisible(true);

        ventureSpotCard2.setBackground(background);
        ventureSpotCard2.setDisable(false);
        ventureSpotCard2.setVisible(true);

        ventureSpotCard3.setBackground(background);
        ventureSpotCard3.setDisable(false);
        ventureSpotCard3.setVisible(true);

        ventureSpotCard4.setBackground(background);
        ventureSpotCard4.setDisable(false);
        ventureSpotCard4.setVisible(true);

        ventureSpotCard5.setBackground(background);
        ventureSpotCard5.setDisable(false);
        ventureSpotCard5.setVisible(true);

        territoryTowerCard0.setBackground(background);
        territoryTowerCard0.setDisable(false);
        territoryTowerCard0.setVisible(true);

        territoryTowerCard1.setBackground(background);
        territoryTowerCard1.setDisable(false);
        territoryTowerCard1.setVisible(true);

        territoryTowerCard2.setBackground(background);
        territoryTowerCard2.setDisable(false);
        territoryTowerCard2.setVisible(true);

        territoryTowerCard3.setBackground(background);
        territoryTowerCard3.setDisable(false);
        territoryTowerCard3.setVisible(true);

        buildingTowerCard0.setBackground(background);
        buildingTowerCard0.setDisable(false);
        buildingTowerCard0.setVisible(true);

        buildingTowerCard1.setBackground(background);
        buildingTowerCard1.setDisable(false);
        buildingTowerCard1.setVisible(true);

        buildingTowerCard2.setBackground(background);
        buildingTowerCard2.setDisable(false);
        buildingTowerCard2.setVisible(true);

        buildingTowerCard3.setBackground(background);
        buildingTowerCard3.setDisable(false);
        buildingTowerCard3.setVisible(true);

        characterTowerCard0.setBackground(background);
        characterTowerCard0.setDisable(false);
        characterTowerCard0.setVisible(true);

        characterTowerCard1.setBackground(background);
        characterTowerCard1.setDisable(false);
        characterTowerCard1.setVisible(true);

        characterTowerCard2.setBackground(background);
        characterTowerCard2.setDisable(false);
        characterTowerCard2.setVisible(true);

        characterTowerCard3.setBackground(background);
        characterTowerCard3.setDisable(false);
        characterTowerCard3.setVisible(true);

        ventureTowerCard0.setBackground(background);
        ventureTowerCard0.setDisable(false);
        ventureTowerCard0.setVisible(true);

        ventureTowerCard1.setBackground(background);
        ventureTowerCard1.setDisable(false);
        ventureTowerCard1.setVisible(true);

        ventureTowerCard2.setBackground(background);
        ventureTowerCard2.setDisable(false);
        ventureTowerCard2.setVisible(true);

        ventureTowerCard3.setBackground(background);
        ventureTowerCard3.setDisable(false);
        ventureTowerCard3.setVisible(true);

        vaticanReportCard1.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport1_1.png", 56, 109, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard1.setDisable(false);
        vaticanReportCard1.setVisible(true);

        vaticanReportCard2.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport2_1.png", 56, 103, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard2.setDisable(false);
        vaticanReportCard2.setVisible(true);

        vaticanReportCard3.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport3_1.png", 56, 109, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard3.setDisable(false);
        vaticanReportCard3.setVisible(true);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
