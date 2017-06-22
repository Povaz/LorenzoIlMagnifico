package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller{
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Text loginMessageText;

    @FXML protected void loginClick(ActionEvent event) throws Exception{
        boolean logged = false;
        if(usernameTextField.getText().equals("Paolo") && passwordTextField.getText().equals("Trilli")){
            logged = true;
        }
        if(logged){
            loginMessageText.setText("Successfully logged");
            Main.root = FXMLLoader.load(getClass().getResource("Game.fxml"));
            Main.scene = new Scene(Main.root, 1280, 720);
            Main.scene.getStylesheets().addAll(this.getClass().getResource("cssFiles/Login.css").toExternalForm());

            Main.primaryStage.setTitle("Lorenzo il Magnifico");
            Main.primaryStage.setScene(Main.scene);
            Main.primaryStage.setResizable(false);
            Main.primaryStage.show();
        }
        else{
            loginMessageText.setText("Incorrect username or password!");
        }
    }

    @FXML protected void registerClick(ActionEvent event){
        boolean registred = false;
        if(usernameTextField.getText().equals("Paolo")){
            registred = true;
        }
        if(!registred){
            loginMessageText.setText("Successfully registred");
        }
        else{
            loginMessageText.setText("Username " + usernameTextField.getText() + " already used!");
        }
    }

    private void timer(long second){
        long init = System.currentTimeMillis();
        while(init + second * 1000 > System.currentTimeMillis()){}
    }
}
