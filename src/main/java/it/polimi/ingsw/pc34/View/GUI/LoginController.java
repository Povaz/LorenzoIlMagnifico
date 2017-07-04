package it.polimi.ingsw.pc34.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class LoginController {
    private Main main;

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Text loginMessageText;

    @FXML private void initialize(){
        // TODO inizializza
    }

    @FXML protected void loginClick(ActionEvent event) throws Exception{
        main.getServerComunication().put("/login");
        main.getServerComunication().put(usernameTextField.getText());
        main.getServerComunication().put(passwordTextField.getText());
        String result = main.getServerComunication().get();

        if(result.equals("Login Successful")){
            main.showWaitingRoom();
        }
        else{
            loginMessageText.setText(result);
        }
    }

    @FXML protected void registerClick(ActionEvent event){
        main.getServerComunication().put("/registration");
        main.getServerComunication().put(usernameTextField.getText());
        main.getServerComunication().put(passwordTextField.getText());
        String result = main.getServerComunication().get();

        loginMessageText.setText(result);
    }

    public void setMessageText(String message){
        loginMessageText.setText(message);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
