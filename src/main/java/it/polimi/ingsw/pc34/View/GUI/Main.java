package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<PersonalBoardView> personalBoardViews = FXCollections.observableArrayList();

    /*public static Parent root;
    public static Scene scene;
    public static Stage primaryStage;*/

    public Main(){
        // costruttore
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

        initRootLayout();

        showLogin();
    }

    private void initRootLayout(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
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
            // TODO: AnchorPane login = (AnchorPane) loader.load();
            Pane login = (Pane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(login);

            // Give the controller access to the main app.
            LoginController loginController = loader.getController();
            loginController.setMain(this);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    public ObservableList<PersonalBoardView> getPersonalBoardViews(){
        return personalBoardViews;
    }
}
