package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameViewController {
    Logger LOGGER = Logger.getLogger(GameViewController.class.getName());

    private final String DEVELOPMENT_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/DevelopmentCards/";
    private final String LEADER_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/LeaderCards/";
    private final String PAWN_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/Pawn/";
    private final String TILE_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/PersonalBonusTile/";
    private final String REPORT_FOLDER = "it/polimi/ingsw/pc34/View/GUI/pngFiles/VaticanReports/";

    private Main main;
    private ChatController chatController;
    private String currentPlayerShown;
    private BoardView board = null;

    // drag and drop attributes
    private Button dragButton = null;
    private Shape dropShape = null;
    private boolean ghost = false;

    // Game
    @FXML private Button showWinner;

    @FXML private Button zoomedCard;

    @FXML protected BorderPane chatBorder;

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
    @FXML private Text ownerUsername;

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

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    @FXML private void initialize(){
        // add in .fxml per settare le dimensioni dell'immagine
        // <Image url="@pngFiles/Board.png" requestedHeight="1046.0" requestedWidth="716.0" />

        // set background action AnchorPane
        actionSpace.setBackground(new Background(new BackgroundImage(new LocatedImage("it/polimi/ingsw/pc34/View/GUI/pngFiles/ActionBackground.png",
                540, 548, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public void initializeView(){
        // set player text
        ownerUsername.setText(main.getUsername());

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

        // initialize chat
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Chat.fxml"));
            AnchorPane chat = (AnchorPane) loader.load();
            chatBorder.setCenter(chat);
            chatController = loader.getController();
            chatController.setMain(main);
            chatController.initializeThread();
        } catch(IOException e){
            LOGGER.log(Level.SEVERE, "Chat.fxml: Not found", e);
        }
    }

    public void initializeThread(){
        (new Thread(() -> {
            String result;
            do {
                result = main.getOpenWindow().get();
                final String toLambda = result;

                Platform.runLater(() -> {
                    if(toLambda.equals("/login")){
                        String info = main.getInfoFromServer().get();

                        showWinner.setText(info);

                        if(info.equals("Timeout expired")){
                            showWinner.setTextFill(Color.RED);
                        }
                        showWinner.setDisable(false);
                        showWinner.setVisible(true);
                    }
                    else if(toLambda.equals("/bonusaction")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionBonusFamily.fxml"));
                            AnchorPane bonusFamily = (AnchorPane) loader.load();
                            actionBorder.setCenter(bonusFamily);

                            ActionBonusFamilyController bonusController = loader.getController();
                            bonusController.setMain(main);

                            bonusController.typeValue.setText(main.getInfoFromServer().get());

                            updateButton(bonusController.ghostFamily, PAWN_FOLDER, "Ghost.png", 55, 80);

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionBonusFamily.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/supportvatican")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionSupportVatican.fxml"));
                            AnchorPane supportVatican = (AnchorPane) loader.load();
                            actionBorder.setCenter(supportVatican);

                            ActionSupportVaticanController supportController = loader.getController();
                            supportController.setMain(main);

                            main.getInfoFromServer().get();

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionSupportVatican.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/exchangeprivilege")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionExchangePrivilege.fxml"));
                            AnchorPane exchangePrivilege = (AnchorPane) loader.load();
                            actionBorder.setCenter(exchangePrivilege);

                            ActionExchangePrivilegeController exchangeController = loader.getController();
                            exchangeController.setMain(main);

                            exchangeController.number.setText(main.getInfoFromServer().get());

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionExchangePrivilege.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/numberservant")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionNumberServant.fxml"));
                            AnchorPane numberServant = (AnchorPane) loader.load();
                            actionBorder.setCenter(numberServant);

                            ActionNumberServantController servantController = loader.getController();
                            servantController.setMain(main);

                            servantController.servant.setMax(Double.valueOf(main.getInfoFromServer().get()));

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionNumberServant.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/paymilitarypoint")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionPayMilitaryPoint.fxml"));
                            AnchorPane payMilitary = (AnchorPane) loader.load();
                            actionBorder.setCenter(payMilitary);

                            ActionPayMilitaryPointController payMilitaryPointController = loader.getController();
                            payMilitaryPointController.setMain(main);

                            main.getInfoFromServer().get();

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionPayMilitaryPoint.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/choosetrade")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionChooseTrade.fxml"));
                            AnchorPane trade = (AnchorPane) loader.load();
                            actionBorder.setCenter(trade);

                            ActionChooseTradeController tradeController = loader.getController();
                            tradeController.setMain(main);

                            String info = main.getInfoFromServer().get();

                            if(info.substring(0, 1).equals("2")){
                                tradeController.second.setDisable(false);
                                tradeController.second.setVisible(true);
                            }

                            tradeController.tradeCard.setImage(new Image(DEVELOPMENT_FOLDER + info.substring(1)));

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionChooseTrade.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/choosediscount")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionChooseDiscount.fxml"));
                            AnchorPane discount = (AnchorPane) loader.load();
                            actionBorder.setCenter(discount);

                            ActionChooseDiscountController discountController = loader.getController();
                            discountController.setMain(main);

                            String info = main.getInfoFromServer().get();

                            List<String> discountStrings = new ArrayList<>();
                            while(info.endsWith("/")){
                                int index = info.indexOf("/");
                                discountStrings.add(info.substring(0, index));
                                info = info.substring(index + 1);
                            }

                            for(int i = 0; i < discountStrings.size(); i++){
                                ToggleButton toggle = (ToggleButton) discountController.discount.getChildren().get(i);
                                toggle.setText(discountStrings.get(i));
                                toggle.setDisable(false);
                                toggle.setVisible(true);
                            }

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionChooseDiscount.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/leadercard")){
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("ActionLeaderCard.fxml"));
                            AnchorPane leaderCard = (AnchorPane) loader.load();
                            actionBorder.setCenter(leaderCard);

                            ActionLeaderCardController leaderController = loader.getController();
                            leaderController.setMain(main);

                            String info = main.getInfoFromServer().get();

                            String type = "";
                            switch(info.substring(0, 1)){
                                case "P":
                                    type += "place?";
                                    break;
                                case "A":
                                    type += "activate?";
                                    break;
                                case "E":
                                    type += "exchange?";
                                    break;
                                case "C":
                                    type += "copy?";
                                    break;
                            }

                            List<String> leaderNames = new ArrayList<>();
                            info = info.substring(1);
                            while(info.endsWith("/")){
                                int index = info.indexOf("/");
                                leaderNames.add(info.substring(0, index));
                                info = info.substring(index + 1);
                            }

                            leaderController.action.setText(type);

                            for(int i = 0; i < leaderNames.size(); i++){
                                ToggleButton toggle = (ToggleButton) leaderController.leader.getChildren().get(i);
                                toggle.setText(leaderNames.get(i));
                                toggle.setDisable(false);
                                toggle.setVisible(true);
                            }

                            actionBorder.setDisable(false);
                            actionBorder.setVisible(true);
                        } catch(IOException e){
                            LOGGER.log(Level.SEVERE, "ActionLeaderCard.fxml: Not found", e);
                        }
                    }
                    else if(toLambda.equals("/update")){
                        update(main.getBoardViewFromServer().get());
                    }
                });
            } while(!result.equals("/login"));
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    showWinner.setMouseTransparent(false);
                }
            }, 5000);
        })).start();
    }

    @FXML private void playerPressed(ActionEvent event){
        Button button = (Button) event.getSource();
        currentPlayerShown = button.getText();

        // set background color
        for(Button b : playerButtons){
            b.setBackground(buttonDefaultBackground);
        }
        button.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));

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

    @FXML private void showWinnerClicked(){
        main.getChatFromServer().put("/close");
        main.showLogin();
    }

    @FXML private void escPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ESCAPE){
            main.getRootC().setFullScreenOff();
        }
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
        ghost = false;
        dragButton = (Button) event.getSource();
        dropShape = null;
        ((Button) event.getSource()).startFullDrag();
    }

    @FXML private void passClicked(){
        String resultString;

        main.getFromGuiToServer().put("/playturn");
        resultString = main.getFromServerToGui().get();
        if(!resultString.equals("Yes")){
            chatController.addMessage("/error" + resultString);
            return;
        }

        main.getFromGuiToServer().put("5");
        resultString = main.getFromServerToGui().get();
        if(!resultString.equals("Yes")){
            chatController.addMessage("/error" + resultString);
            return;
        }
    }

    @FXML private void leaderClicked(ActionEvent event){
        String actionType;
        switch(((MenuItem)event.getSource()).getId()){
            case "placeleader":
                actionType = "2";
                break;
            case "activateleader":
                actionType = "3";
                break;
            case "exchangeleader":
                actionType = "4";
                break;
            default:
                return;
        }

        String resultString;
        main.getFromGuiToServer().put("/playturn");
        resultString = main.getFromServerToGui().get();
        if(!resultString.equals("Yes")){
            chatController.addMessage("/error" + resultString);
            return;
        }

        main.getFromGuiToServer().put(actionType);
        resultString = main.getFromServerToGui().get();
        if(!resultString.equals("Yes")){
            chatController.addMessage("/error" + resultString);
            return;
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
        String spotType = dropShape.getId().substring(0, 1);
        String spotNumber = dropShape.getId().substring(1, 2);

        // get familyMember color
        String familyColor = dragButton.getText();

        String resultString;

        if(!ghost){
            main.getFromGuiToServer().put("/playturn");
            resultString = main.getFromServerToGui().get();
            if(!resultString.equals("Yes")){
                chatController.addMessage("/error" + resultString);
                return;
            }

            main.getFromGuiToServer().put("1");
            resultString = main.getFromServerToGui().get();
            if(!resultString.equals("Yes")){
                chatController.addMessage("/error" + resultString);
                return;
            }
        }

        main.getFromGuiToServer().put(spotType); // 1 TerritoryT, 2 BuildingT, 3 CharacterT, 4 VentureT, 5 Harvest, 6 Produce, 7 Market, 8 CouncilPalace
        resultString = main.getFromServerToGui().get();
        if(!resultString.equals("Yes")){
            chatController.addMessage("/error" + resultString);
            return;
        }

        if(!spotType.equals("8")){
            main.getFromGuiToServer().put(spotNumber); // spot number: insert number between [0 - numSpot)
            resultString = main.getFromServerToGui().get();
            if(!resultString.equals("Yes")){
                chatController.addMessage("/error" + resultString);
                return;
            }
        }

        if(!ghost){
            main.getFromGuiToServer().put(familyColor); // familyMember: insert 1 black, 2 white, 3 orange, 4 neutral
            resultString = main.getFromServerToGui().get();
            if(!resultString.equals("Yes")){
                chatController.addMessage("/error" + resultString);
                return;
            }
        }

        if(ghost){
            ghost = false;
            actionBorder.setDisable(true);
            actionBorder.setVisible(false);
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
                for(Node n : gridReported.get(i).getChildren()){
                    n.setDisable(true);
                    n.setVisible(false);
                }
                gridReported.get(i).setDisable(true);
                gridReported.get(i).setVisible(false);
            }
            else{
                gridReported.get(i).setDisable(false);
                gridReported.get(i).setVisible(true);
                List<String> reported = boardView.getReported().get(i);
                for(int j = 0; j < 5; j++){
                    Shape shape = (Shape) gridReported.get(i).getChildren().get(j);
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

        current.fire();
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

    public void setDragButton(Button dragButton){
        this.dragButton = dragButton;
    }

    public void setDropShape(Shape dropShape){
        this.dropShape = dropShape;
    }
}
