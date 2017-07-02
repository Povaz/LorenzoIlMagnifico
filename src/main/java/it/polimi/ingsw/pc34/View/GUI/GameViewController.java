package it.polimi.ingsw.pc34.View.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class GameViewController {
    private Main main;
    private String currentPlayerShown;
    private List<PersonalBoardView> players;
    private ObservableList<PersonalBoardView> observablePlayers;
    private BoardView board;

    // drag and drop attributes
    private Button dragButton = null;
    private Button dropButton = null;

    @FXML private Button bt;

    @FXML private Button zoomedCard;

    @FXML private Button player0;
    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;
    @FXML private Button player4;

    @FXML private GridPane territoryTower;
    @FXML private GridPane characterTower;
    @FXML private GridPane buildingTower;
    @FXML private GridPane ventureTower;

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

    @FXML private GridPane buildingSpot;
    @FXML private GridPane territorySpot;
    @FXML private GridPane characterSpot;
    @FXML private GridPane ventureSpot;

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

    @FXML private Text blackDice;
    @FXML private Text whiteDice;
    @FXML private Text orangeDice;

    @FXML private void initialize(){
        // add in .fxml per settare le dimensioni dell'immagine
        // <Image url="@pngFiles/Board.png" requestedHeight="1046.0" requestedWidth="716.0" />
    }

    public void initializeView(){
        if(players.size() > 0){
            player0.setText(players.get(0).getUsername());
            player0.setDisable(false);
            player0.setVisible(true);
            currentPlayerShown = players.get(0).getUsername();
        }
        if(players.size() > 1){
            player1.setText(players.get(1).getUsername());
            player1.setDisable(false);
            player1.setVisible(true);
        }
        if(players.size() > 2){
            player2.setText(players.get(2).getUsername());
            player2.setDisable(false);
            player2.setVisible(true);
        }
        if(players.size() > 3){
            player3.setText(players.get(3).getUsername());
            player3.setDisable(false);
            player3.setVisible(true);
        }
        if(players.size() > 4){
            player4.setText(players.get(4).getUsername());
            player4.setDisable(false);
            player4.setVisible(true);
        }
    }

    public void initializeObservable(){
        observablePlayers = FXCollections.observableArrayList(players);

        observablePlayers.addListener((ListChangeListener) obs -> {
                ObservableList<PersonalBoardView> playersObs = (ObservableList<PersonalBoardView>) obs.getList();

            }
        );

        /*players.get(0).addListener((obsVal, oldVal, newVal) -> {
            PersonalBoardView pB = (PersonalBoardView) newVal;
            if(!pB.getUsername().equals(currentPlayerShown)){
                return;
            }
            String folder = "it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/";
            if(pB.getTerritoryCards().size() == 6){
                updateButton(territorySpotCard0, folder, pB.getTerritoryCards().get(0));
                updateButton(territorySpotCard1, folder, pB.getTerritoryCards().get(1));
                updateButton(territorySpotCard2, folder, pB.getTerritoryCards().get(2));
                updateButton(territorySpotCard3, folder, pB.getTerritoryCards().get(3));
                updateButton(territorySpotCard4, folder, pB.getTerritoryCards().get(4));
                updateButton(territorySpotCard5, folder, pB.getTerritoryCards().get(5));
            }
        });*/

        players.get(0).setCoin(5);
    }

    private void updateButton(Button button, String folder, String path){
        if(path.equals("")){
            button.setVisible(false);
            button.setDisable(true);
        }
        else{
            Image image = new LocatedImage(folder + path, 85, 126, false, false);
            Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
            button.setBackground(background);
            button.setDisable(false);
            button.setVisible(true);
        }
    }

    @FXML private void playerPressed(MouseEvent event){
        Button button = (Button) event.getSource();
        String username = button.getText();
        currentPlayerShown = username;

        updateView();
    }

    private void updateView(){
        PersonalBoardView toShow = null;
        for(PersonalBoardView p : players){
            if(p.getUsername().equals(currentPlayerShown)){
                toShow = p;
            }
        }
        if(toShow == null){
            return;
        }

        // update board
        //for(int i = 0; i < board.)

        // update personal board
        String folder = "it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/";
        for(int i = 0; i < 6; i++){
            updateButton((Button)territorySpot.getChildren().get(i), folder, toShow.getTerritoryCards().get(i));
            updateButton((Button)buildingSpot.getChildren().get(i), folder, toShow.getBuildingCards().get(i));
            updateButton((Button)characterSpot.getChildren().get(i), folder, toShow.getCharacterCards().get(i));
            updateButton((Button)ventureSpot.getChildren().get(i), folder, toShow.getVentureCards().get(i));
        }
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
        players.get(0).getTerritoryCards().set(2, "TerritoryCard15.png");
        updateView();

        // tower
        for(int i = 1; i <= 4; i++){
            ((Button)territoryTower.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/TerritoryCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            territoryTower.getChildren().get(i - 1).setDisable(false);
            territoryTower.getChildren().get(i - 1).setVisible(true);

            ((Button)characterTower.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/CharacterCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            characterTower.getChildren().get(i - 1).setDisable(false);
            characterTower.getChildren().get(i - 1).setVisible(true);

            ((Button)buildingTower.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/BuildingCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            buildingTower.getChildren().get(i - 1).setDisable(false);
            buildingTower.getChildren().get(i - 1).setVisible(true);

            ((Button)ventureTower.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/VentureCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            ventureTower.getChildren().get(i - 1).setDisable(false);
            ventureTower.getChildren().get(i - 1).setVisible(true);
        }

        // spot
        for(int i = 1; i <= 6; i++){
            ((Button)buildingSpot.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/BuildingCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            buildingSpot.getChildren().get(i - 1).setDisable(false);
            buildingSpot.getChildren().get(i - 1).setVisible(true);

            ((Button)territorySpot.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/TerritoryCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            territorySpot.getChildren().get(i - 1).setDisable(false);
            territorySpot.getChildren().get(i - 1).setVisible(true);

            ((Button)characterSpot.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/CharacterCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            characterSpot.getChildren().get(i - 1).setDisable(false);
            characterSpot.getChildren().get(i - 1).setVisible(true);

            ((Button)ventureSpot.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/VentureCard" + i + ".png", 85, 126, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            ventureSpot.getChildren().get(i - 1).setDisable(false);
            ventureSpot.getChildren().get(i - 1).setVisible(true);
        }

        vaticanReportCard1.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport1_1.png", 56, 111, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard1.setDisable(false);
        vaticanReportCard1.setVisible(true);

        vaticanReportCard2.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport2_1.png", 56, 105, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard2.setDisable(false);
        vaticanReportCard2.setVisible(true);

        vaticanReportCard3.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport3_1.png", 56, 111, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard3.setDisable(false);
        vaticanReportCard3.setVisible(true);
    }

    @FXML private void startDrag(MouseEvent event){
        System.out.println("startDrag");
        dragButton = (Button) event.getSource();
        dropButton = null;
        System.out.println("" + dragButton + dropButton);
    }

    @FXML private void stopDrag(MouseEvent event){
        System.out.println("stopDrag");
        dragButton = null;
        dropButton = null;
        System.out.println("" + dragButton + dropButton);
    }

    @FXML private void stopDrop(MouseEvent event){
        System.out.println("stopDrop");
        dragButton = null;
        dropButton = null;
        System.out.println("" + dragButton + dropButton);
    }

    @FXML private void startDrop(MouseEvent event){
        System.out.println("startDrop");
        dropButton = (Button) event.getSource();
        System.out.println("" + dragButton + dropButton);
        if(dragButton == null || dropButton == null){
            return;
        }
        System.out.println("YES");
        dropButton.setBackground(new Background(new BackgroundFill(new Color(150,150,150,1), null, null)));
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setPlayersView(List<PersonalBoardView> players){
        this.players = players;
    }

    public void setBoardView(BoardView board){
        this.board = board;
    }
}
