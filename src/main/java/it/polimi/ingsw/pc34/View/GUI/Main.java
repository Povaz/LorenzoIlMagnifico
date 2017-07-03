package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application{
    private BoardView board = new BoardView(2);
    private List<PersonalBoardView> players = new ArrayList<>();

    private Stage primaryStage;
    private BorderPane rootLayout;

    private boolean canBeFullScreen = false;
    private int windowWidth = 0;
    private int windowHeight = 0;

    private ObservableList<PersonalBoardView> personalBoardViews = FXCollections.observableArrayList();

    private boolean logged = false;

    public Main(){
        players.add(new PersonalBoardView("ciccio"));
        players.add(new PersonalBoardView("WWWWWWWWWWWWWWW"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root, 487, 757);
        scene.getStylesheets().addAll(this.getClass().getResource("Login.css").toExternalForm());

        primaryStage.setTitle("Lorenzo il Magnifico");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();*/

        /*this.primaryStage = primaryStage;
        this.root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        this.scene = new Scene(root, 487, 757);
        scene.getStylesheets().addAll(this.getClass().getResource("cssFiles/Login.css").toExternalForm());

        primaryStage.setTitle("Lorenzo il Magnifico");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();*/

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Lorenzo il Magnifico");
        this.primaryStage.getIcons().add(new Image("it/polimi/ingsw/pc34/View/GUI/pngFiles/Icon.png"));

        initRootLayout();

        // TODO rimetti: showLogin();
        showGame();
    }

    private void initRootLayout(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Give the controller access to the main app.
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMain(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Set attributes
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();

            primaryStage.show();

            primaryStage.show();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showLogin(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Login.fxml"));
            AnchorPane login = (AnchorPane) loader.load();

            // Set login into the center of root layout.
            rootLayout.setCenter(login);
            windowWidth = 500;
            windowHeight = 835;
            primaryStage.setWidth(windowWidth);
            primaryStage.setHeight(windowHeight);

            // Give the controller access to the main app.
            LoginController loginController = loader.getController();
            loginController.setMain(this);

            // Cannot be full screen
            canBeFullScreen = false;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showWaitingRoom(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("WaitingRoom.fxml"));
            AnchorPane waitingRoom = (AnchorPane) loader.load();

            // Set waiting room into the center of root layout.
            rootLayout.setCenter(waitingRoom);
            windowWidth = 500;
            windowHeight = 835;
            primaryStage.setWidth(windowWidth);
            primaryStage.setHeight(windowHeight);

            // Give the controller access to the main app.
            WaitingRoomController waitingRoomController = loader.getController();
            waitingRoomController.setMain(this);

            // Cannot be full screen
            canBeFullScreen = false;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showGame(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Game.fxml"));
            AnchorPane game = (AnchorPane) loader.load();

            // Set game into the center of root layout.
            rootLayout.setCenter(game);
            windowWidth = 1920;
            windowHeight = 1080;
            primaryStage.setWidth(windowWidth);
            primaryStage.setHeight(windowHeight);
            primaryStage.setFullScreen(true);

            // Give the controller access to the main app.
            GameViewController gameController = loader.getController();
            gameController.setMain(this);
            gameController.setPlayersView(players);
            gameController.setBoardView(board);
            gameController.initializeView();
            gameController.initializeObservable();

            // Can be full screen
            canBeFullScreen = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public BorderPane getRootLayout(){
        return rootLayout;
    }

    public ObservableList<PersonalBoardView> getPersonalBoardViews(){
        return personalBoardViews;
    }

    public boolean isCanBeFullScreen(){
        return canBeFullScreen;
    }

    public void setCanBeFullScreen(boolean canBeFullScreen){
        this.canBeFullScreen = canBeFullScreen;
    }

    public int getWindowWidth(){
        return windowWidth;
    }

    public int getWindowHeight(){
        return windowHeight;
    }
}
