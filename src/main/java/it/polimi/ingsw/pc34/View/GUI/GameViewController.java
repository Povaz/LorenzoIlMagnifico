package it.polimi.ingsw.pc34.View.GUI;

import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameViewController {
    private final String DEVELOPMENT_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/";
    private final String LEADER_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/LeaderCards/";
    private final String PAWN_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/Pawn/";
    private final String TILE_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/PersonalBonusTile/";
    private final String REPORT_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/";

    private Main main;
    private String currentPlayerShown;
    private BoardView board = null;
    private boolean canDoAction = true;

    // drag and drop attributes
    private Button dragButton = null;
    private Shape dropShape = null;

    @FXML private Button bt;

    // Game
    @FXML private Button zoomedCard;

    @FXML private AnchorPane actionSpace;
    @FXML private BorderPane actionBorder;

    @FXML private Button player0;
    @FXML private Button player1;
    @FXML private Button player2;
    @FXML private Button player3;
    @FXML private Button player4;
    private List<Button> playerButtons = new ArrayList<>();
    private Background buttonDefaultBackground;

    // Board
    @FXML private GridPane territoryTower;
    @FXML private GridPane characterTower;
    @FXML private GridPane buildingTower;
    @FXML private GridPane ventureTower;

    @FXML private GridPane territoryTowerSpot;
    @FXML private GridPane characterTowerSpot;
    @FXML private GridPane buildingTowerSpot;
    @FXML private GridPane ventureTowerSpot;
    @FXML private GridPane councilPalace;
    @FXML private GridPane harvestSpot;
    @FXML private GridPane productionSpot;
    @FXML private GridPane market;

    @FXML private ImageView harvestCover;
    @FXML private ImageView productionCover;
    @FXML private ImageView marketCover2;
    @FXML private ImageView marketCover3;

    @FXML private GridPane order;

    @FXML private Button vaticanReportCard1;
    @FXML private Button vaticanReportCard2;
    @FXML private Button vaticanReportCard3;

    @FXML private GridPane reported1;
    @FXML private GridPane reported2;
    @FXML private GridPane reported3;
    List<GridPane> gridReported = new ArrayList<>();

    @FXML private Text turn;
    @FXML private Text current;

    @FXML private Text blackDice;
    @FXML private Text whiteDice;
    @FXML private Text orangeDice;

    // Personal board
    @FXML private GridPane buildingSpot;
    @FXML private GridPane territorySpot;
    @FXML private GridPane characterSpot;
    @FXML private GridPane ventureSpot;
    @FXML private GridPane leaderCards;
    @FXML private ImageView tile;

    @FXML private Button blackFamilyMember;
    @FXML private Button whiteFamilyMember;
    @FXML private Button orangeFamilyMember;
    @FXML private Button neutralFamilyMember;
    List<Button> familyButton = new ArrayList<>();

    @FXML private Text coin;
    @FXML private Text wood;
    @FXML private Text stone;
    @FXML private Text servant;
    @FXML private Text faithPoint;
    @FXML private Text militaryPoint;
    @FXML private Text victoryPoint;

    @FXML private void initialize(){
        // add in .fxml per settare le dimensioni dell'immagine
        // <Image url="@pngFiles/Board.png" requestedHeight="1046.0" requestedWidth="716.0" />

        // set background action AnchorPane
        actionSpace.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/ActionBackground.png",
                540, 548, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public void initializeView(){
        // fill playerButton List
        playerButtons.add(player0);
        playerButtons.add(player1);
        playerButtons.add(player2);
        playerButtons.add(player3);
        playerButtons.add(player4);
        buttonDefaultBackground = player0.getBackground();

        // fill familyButton List
        familyButton.add(blackFamilyMember);
        familyButton.add(whiteFamilyMember);
        familyButton.add(orangeFamilyMember);
        familyButton.add(neutralFamilyMember);

        // fill gridReported List
        gridReported.add(reported1);
        gridReported.add(reported2);
        gridReported.add(reported3);
    }

    public void initializeThread(){
        (new Thread(() -> {
            String result;
            do {
                result = main.getOpenWindow().get();
                final String toLambda = result;
                System.out.println(toLambda);
                Platform.runLater(() -> { // TODO inseriscine uno in ogni brach per velocizzare
                    if(toLambda.equals("/login")){
                        main.showLogin();
                    }
                    else if(toLambda.equals("/exchangeprivilege")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionExchangePrivilege.fxml"));
                            AnchorPane exchangePrivilege = (AnchorPane) loader.load();
                            actionBorder.setCenter(exchangePrivilege);

                            ActionExchangePrivilegeController exchangeController = loader.getController();
                            exchangeController.setMain(main);

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    else if(toLambda.equals("/numberservant")){

                    }
                    else if(toLambda.equals("/paymilitarypoint")){

                    }
                    else if(toLambda.equals("/choosetrade")){

                    }
                    else if(toLambda.equals("/choosediscount")){

                    }
                    else if(toLambda.equals("/update")){
                        update(main.getBoardViewFromServer().get());
                    }
                });
            } while(result.equals("/login"));
            System.out.println("out thread");
        })).start();
    }

    @FXML private void playerPressed(ActionEvent event){
        Button button = (Button) event.getSource();
        currentPlayerShown = button.getText();

        // set background color
        for(Button b : playerButtons){
            b.setBackground(buttonDefaultBackground);
        }
        button.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

        // update view with correct playerBoardView
        PlayerBoardView current = null;
        for(PlayerBoardView p : board.getPlayers()){
            if(p.getUsername().equals(currentPlayerShown)){
                current = p;
            }
        }
        updateView(current);
    }

    private void updateView(PlayerBoardView playerBoardView){
        // rewards
        coin.setText(playerBoardView.getCoin());
        wood.setText(playerBoardView.getWood());
        stone.setText(playerBoardView.getStone());
        servant.setText(playerBoardView.getServant());
        faithPoint.setText(playerBoardView.getFaithPoint());
        militaryPoint.setText(playerBoardView.getMilitaryPoint());
        victoryPoint.setText(playerBoardView.getVictoryPoint());

        // development cards
        for(int i = 0; i < 6; i++){
            Button button = (Button) territorySpot.getChildren().get(i);
            String path = playerBoardView.getTerritoryCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 6; i++){
            Button button = (Button) buildingSpot.getChildren().get(i);
            String path = playerBoardView.getBuildingCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 6; i++){
            Button button = (Button) characterSpot.getChildren().get(i);
            String path = playerBoardView.getCharacterCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 6; i++){
            Button button = (Button) ventureSpot.getChildren().get(i);
            String path = playerBoardView.getVentureCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }

        // leader cards
        for(int i = 0; i < 4; i++){
            Button button = (Button) leaderCards.getChildren().get(i);
            String path = playerBoardView.getLeaderCards().get(i);
            updateButton(button, LEADER_FOLDER, path, 95, 147);
            button.setText(playerBoardView.getLeaderCardsState().get(i));
        }

        // tile
        if(playerBoardView.getPersonalBonusTile().equals("")){
            tile.setImage(null);
        }
        else{
            tile.setImage(new LocatedImage(TILE_FOLDER + playerBoardView.getPersonalBonusTile(), 46, 402,
                    false, false));
        }


        // family members
        boolean yourFamilyMember = currentPlayerShown.equals(main.getUsername());
        for(int i = 0; i < 4; i++){
            Button button = familyButton.get(i);
            String path = playerBoardView.getFamilyMembers().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
            if(yourFamilyMember){
                button.setMouseTransparent(false);
            }
            else{
                button.setMouseTransparent(true);
            }
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

    @FXML private void escPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ESCAPE){
            main.getRootC().setFullScreenOff();
        }
    }

    @FXML private void bTP(){
        // TODO elimina
        //main.getOpenWindow().put("/exchangeprivilege");
        // vatican report
        vaticanReportCard1.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport1_1.png", 56, 111, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard1.setDisable(false);
        vaticanReportCard1.setVisible(true);

        vaticanReportCard2.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport2_1.png", 56, 105, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard2.setDisable(false);
        vaticanReportCard2.setVisible(true);

        vaticanReportCard3.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/VaticanReport3_1.png", 56, 111, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        vaticanReportCard3.setDisable(false);
        vaticanReportCard3.setVisible(true);

        Player p1 = new Player("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.BLUE);
        Player p2 = new Player("Affetti", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.RED);
        Player p3 = new Player("Trilli", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.GREEN);
        Player p4 = new Player("Venneri", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.PURPLE);
        Player p5 = new Player("Tesser", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.YELLOW);
        List<PlayerBoard> p = new ArrayList<>();
        p.add(p1.getPlayerBoard());
        p.add(p2.getPlayerBoard());
        p.add(p3.getPlayerBoard());
        p.add(p4.getPlayerBoard());
        p.add(p5.getPlayerBoard());
        Board board = new Board(new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5)));
        BoardView bv = new BoardView(board, p, "Cugola");
        update(bv);


        /*players.get(0).getTerritoryCards().set(2, "TerritoryCard15.png");
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

        // leader cards
        for(int i = 1; i <= 4; i++){
            ((Button)leaderCards.getChildren().get(i - 1)).setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/LeaderCards/LeaderCard" + i + ".png", 95, 147, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            leaderCards.getChildren().get(i - 1).setDisable(false);
            leaderCards.getChildren().get(i - 1).setVisible(true);
        }
        ((Button)leaderCards.getChildren().get(0)).setText("IN HAND");
        ((Button)leaderCards.getChildren().get(1)).setText("PLACED");
        ((Button)leaderCards.getChildren().get(2)).setText("ACTIVATED");
        ((Button)leaderCards.getChildren().get(3)).setText("EXCHANGED");
        */
    }

    @FXML private void showState(MouseEvent event){
        Button button = (Button)event.getSource();
        button.setTextFill(new Color(1, 1, 1, 1));
    }

    @FXML private void hideState(MouseEvent event){
        Button button = (Button)event.getSource();
        button.setTextFill(new Color(0, 0, 0, 0));
    }

    @FXML private void startDrag(MouseEvent event){
        dragButton = (Button) event.getSource();
        dropShape = null;
        ((Button) event.getSource()).startFullDrag();
    }

    @FXML private void passClicked(){
        if(!canDoAction){
            return;
        }
        main.getFromGuiToServer().put("/playerturn");
        String response = main.getFromServerToGui().get();
        if(response.equals("Yes")){
            main.getFromGuiToServer().put("5");
        }
    }

    @FXML private void enterDrop(MouseEvent event){
        dropShape = (Shape) event.getSource();
    }

    @FXML private void exitDrop(MouseEvent event){
        dropShape = null;
    }

    @FXML private void drop(MouseEvent event){
        if(dragButton == null || dropShape == null){
            return;
        }

        // get action spot
        String spotType = dropShape.getId().substring(0, 0);
        String spotNumber = dropShape.getId().substring(1, 1);
        System.out.println(spotType);
        System.out.println(spotNumber);

        // get familyMember color
        String familyColor = dragButton.getText();
        System.out.println(familyColor);

        /*if(!canDoAction){
            return;
        }*/

        main.getFromGuiToServer().put("/playerturn");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        //TODO va qui?
        //canDoAction = false;
        main.getFromGuiToServer().put("1");
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        main.getFromGuiToServer().put(spotType); // 1 TerritoryT, 2 BuildingT, 3 CharacterT, 4 VentureT, 5 Harvest, 6 Produce, 7 Market, 8 CouncilPalace
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
        if(!spotType.equals("8")){
            main.getFromGuiToServer().put(spotNumber); // spot number: inserisci numero 0-3
            if(!main.getFromServerToGui().get().equals("Yes")){
                return;
            }
        }
        main.getFromGuiToServer().put(familyColor); // familiare: inserisci 1 black, 2 white, 3 orange, 4 neutral
        if(!main.getFromServerToGui().get().equals("Yes")){
            return;
        }
    }

    public void update(BoardView boardView){

        // +++ INIZIALIZZA +++
        if(board == null){
            initializeBoard(boardView);
        }

        // +++ UPDATE BOARD +++

        // turn + currentPlayer
        turn.setText(boardView.getTurn());
        current.setText(boardView.getCurrentPlayer());

        // tower cards
        for(int i = 0; i < 4; i++){
            Button button = (Button) territoryTower.getChildren().get(i);
            String path = boardView.getTerritoryCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) buildingTower.getChildren().get(i);
            String path = boardView.getBuildingCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) characterTower.getChildren().get(i);
            String path = boardView.getCharacterCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) ventureTower.getChildren().get(i);
            String path = boardView.getVentureCards().get(i);
            updateButton(button, DEVELOPMENT_FOLDER, path, 85, 126);
        }

        // dices
        blackDice.setText(boardView.getBlackDice());
        whiteDice.setText(boardView.getWhiteDice());
        orangeDice.setText(boardView.getOrangeDice());

        // vatican report card
        updateButton(vaticanReportCard1, REPORT_FOLDER, boardView.getVaticanReports().get(0), 56, 111);
        updateButton(vaticanReportCard2, REPORT_FOLDER, boardView.getVaticanReports().get(1), 56, 105);
        updateButton(vaticanReportCard3, REPORT_FOLDER, boardView.getVaticanReports().get(2), 56, 111);

        // vatican reported
        for(int i = 0; i < 3; i++){
            if(boardView.getReported().get(i).get(0).equals("")){
                // non ci sono reported
                for(Node n : reported1.getChildren()){
                    n.setDisable(true);
                    n.setVisible(false);
                }
                reported1.setDisable(true);
                reported1.setVisible(false);
            }
            else{
                List<String> reported = boardView.getReported().get(i);
                for(int j = 0; j < 5; j++){
                    Shape shape = (Shape) reported1.getChildren().get(j);
                    if(reported.get(j).equals("")){
                        shape.setDisable(true);
                        shape.setVisible(false);
                    }
                    else{
                        shape.setFill(Color.valueOf(reported.get(j)));
                        shape.setDisable(false);
                        shape.setVisible(true);
                    }
                }
            }
        }

        // order
        for(int i = 0; i < 5; i++){
            ImageView image = (ImageView) order.getChildren().get(i);
            String path = boardView.getOrder().get(i);
            if(path.equals("")){
                image.setDisable(true);
                image.setVisible(false);
            }
            else{
                image.setImage(new LocatedImage(PAWN_FOLDER + path, 40, 28, false, false));
                image.setDisable(false);
                image.setVisible(true);
            }
        }

        // council palace spot
        for(int i = 0; i < 20; i++){
            Button button = (Button) councilPalace.getChildren().get(i);
            String path = boardView.getCouncilPalace().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }

        // tower spot
        for(int i = 0; i < 4; i++){
            Button button = (Button) territoryTowerSpot.getChildren().get(i);
            String path = boardView.getTerritoryTower().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) buildingTowerSpot.getChildren().get(i);
            String path = boardView.getBuildingTower().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) characterTowerSpot.getChildren().get(i);
            String path = boardView.getCharacterTower().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }
        for(int i = 0; i < 4; i++){
            Button button = (Button) ventureTowerSpot.getChildren().get(i);
            String path = boardView.getVentureTower().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }

        // market spot
        for(int i = 0; i < 4; i++){
            Button button = (Button) market.getChildren().get(i);
            String path = boardView.getMarket().get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }

        // harvest spot
        updateButton((Button) harvestSpot.getChildren().get(0), PAWN_FOLDER, boardView.getHarvestArea().get(0).get(0), 55, 80);
        for(int i = 0; i < 9; i++){
            Button button = (Button) harvestSpot.getChildren().get(i + 1);
            String path = boardView.getHarvestArea().get(1).get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }

        // production spot
        updateButton((Button) productionSpot.getChildren().get(0), PAWN_FOLDER, boardView.getProductionaArea().get(0).get(0), 55, 80);
        for(int i = 0; i < 9; i++){
            Button button = (Button) productionSpot.getChildren().get(i + 1);
            String path = boardView.getProductionaArea().get(1).get(i);
            updateButton(button, PAWN_FOLDER, path, 55, 80);
        }


        // +++ aggiorna il riferimento a boardView +++
        board = boardView;


        // +++ UPDATE PLAYER BOARD +++
        Button current = null;
        for(Button b : playerButtons){
            if(b.getText().equals(currentPlayerShown)){
                current = b;
            }
        }
        System.out.println("prima di fire");
        System.out.println(current);
        current.fire();
        System.out.println("dopo fire");
        System.out.println(current);
    }

    private void initializeBoard(BoardView boardView){
        int numberOfPlayers = boardView.getPlayers().size();

        // blocca la harvest e la production area 1, visualizza le coperture
        if(numberOfPlayers < 3){
            harvestSpot.getChildren().get(11).setDisable(true);
            harvestSpot.getChildren().get(11).setVisible(false);
            harvestCover.setDisable(false);
            harvestCover.setVisible(true);

            productionSpot.getChildren().get(11).setDisable(true);
            productionSpot.getChildren().get(11).setVisible(false);
            productionCover.setDisable(false);
            productionCover.setVisible(true);
        }

        // blocca i market 2 e 3, visualizza le coperture
        if(numberOfPlayers < 4){
            market.getChildren().get(6).setDisable(true);
            market.getChildren().get(6).setVisible(false);
            marketCover2.setDisable(false);
            marketCover2.setVisible(true);

            market.getChildren().get(7).setDisable(true);
            market.getChildren().get(7).setVisible(false);
            marketCover3.setDisable(false);
            marketCover3.setVisible(true);
        }

        // inizializza i bottoni player
        for(int i = 0; i < numberOfPlayers; i++){
            Button button = playerButtons.get(i);
            button.setText(boardView.getPlayers().get(i).getUsername());
            button.setTextFill(Color.valueOf(boardView.getPlayers().get(i).getColor()));
            button.setDisable(false);
            button.setVisible(true);
        }
        currentPlayerShown = playerButtons.get(0).getText();
    }

    private void updateButton(Button button, String folder, String path, int width, int height){
        if(path.equals("")){
            button.setVisible(false);
            button.setDisable(true);
        }
        else{
            Image image = new LocatedImage(folder + path, width, height, false, false);
            Background background = new Background(new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
            button.setBackground(background);
            button.setDisable(false);
            button.setVisible(true);
        }
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setCanDoAction(boolean canDoAction){
        this.canDoAction = canDoAction;
    }
}
