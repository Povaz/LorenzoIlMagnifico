package it.polimi.ingsw.pc34.View.GUI;

import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Client;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application{
    // connection canal
    private SynchronizedString openWindow;
    private SynchronizedString chatComunication;
    private SynchronizedString fromGuiToServer;
    private SynchronizedString fromServerToGui;

    // controller
    private RootLayoutController rootC = null;
    private LoginController loginC = null;
    private WaitingRoomController waitingRoomC = null;
    private GameViewController gameViewC = null;

    // username
    private String username;

    // schermata: 1 login, 2 waiting, 3 game
    private AtomicInteger screen = new AtomicInteger(0);

    // view
    private Stage primaryStage;
    private BorderPane rootLayout;

    private boolean canBeFullScreen = false;
    private int windowWidth = 0;
    private int windowHeight = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(Client.guiReference);
        Client.guiReference = this;
        System.out.println(Client.guiReference);
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

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });

        initRootLayout();

        // TODO rimetti: showLogin();
        showGame();
    }

    public void initializeObservable(){
        chatComunication.getAvailable().addListener(((observable, oldValue, newValue) -> {
            try {
                String message = chatComunication.get();
                boolean chat = false;
                if(message.length() >= 5){
                    if(message.startsWith("/chat")){
                        message = message.substring(5, message.length() - 1);
                        chat = true;
                    }
                }

                int screenValue = screen.get();
                if(screenValue == 1){
                    if(loginC != null){
                        if(!chat){
                            loginC.setMessageText(message);
                        }
                    }
                }
                else if(screenValue == 2){
                    if(waitingRoomC != null){
                        if(!chat){
                            waitingRoomC.setMessageText(message);
                        }
                    }
                }
                else if(screenValue == 3){
                    if(gameViewC != null){
                        if(!chat){
                            // TODO waitingRoomC.setMessageText(message);
                        }
                        else{
                            // TODO chat
                        }
                    }
                }
            } catch(NullPointerException e){}
        }));
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
            rootC = rootLayoutController;

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Set attributes
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();

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

            // Set screen
            loginC = loginController;
            screen.set(1);

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
            waitingRoomController.initializeThread();

            // Cannot be full screen
            canBeFullScreen = false;

            // Set screen
            waitingRoomC = waitingRoomController;
            screen.set(2);
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
            gameController.initializeView();
            gameController.initializeObservable();

            // Can be full screen
            canBeFullScreen = true;

            // Set screen
            gameViewC = gameController;
            screen.set(3);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updateGame(BoardView boardView){
        GameViewController gC = gameViewC;
        if(screen.get() == 3 && gC != null){
            gC.update(boardView);
        }
    }

    public static void main(String[] args){
        Application.launch(Main.class);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public BorderPane getRootLayout(){
        return rootLayout;
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

    public SynchronizedString getChatComunication(){
        return chatComunication;
    }

    public void setChatComunication(SynchronizedString chatComunication){
        this.chatComunication = chatComunication;
    }

    public SynchronizedString getFromGuiToServer(){
        return fromGuiToServer;
    }

    public void setFromGuiToServer(SynchronizedString fromGuiToServer){
        this.fromGuiToServer = fromGuiToServer;
    }

    public SynchronizedString getFromServerToGui(){
        return fromServerToGui;
    }

    public void setFromServerToGui(SynchronizedString fromServerToGui){
        this.fromServerToGui = fromServerToGui;
    }

    public SynchronizedString getOpenWindow(){
        return openWindow;
    }

    public void setOpenWindow(SynchronizedString openWindow){
        this.openWindow = openWindow;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public RootLayoutController getRootC(){
        return rootC;
    }
}
