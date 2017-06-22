package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Parent root;
    public static Scene scene;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root, 487, 757);
        scene.getStylesheets().addAll(this.getClass().getResource("Login.css").toExternalForm());

        primaryStage.setTitle("Lorenzo il Magnifico");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();*/
        this.primaryStage = primaryStage;
        this.root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        this.scene = new Scene(root, 487, 757);
        //scene.getStylesheets().addAll(this.getClass().getResource("cssFiles/Login.css").toExternalForm());

        primaryStage.setTitle("Lorenzo il Magnifico");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
