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
        if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")){
            return;
        }
        main.getFromGuiToServer().put("/login");
        main.getFromGuiToServer().put(usernameTextField.getText());
        main.getFromGuiToServer().put(passwordTextField.getText());
        String result = main.getFromServerToGui().get();

        if(result.equals("Login successful")){
            main.showWaitingRoom();
        }
        else{
            loginMessageText.setText(result);
        }
    }

    @FXML protected void registerClick(ActionEvent event){
        if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")){
            return;
        }
        main.getFromGuiToServer().put("/registration");
        main.getFromGuiToServer().put(usernameTextField.getText());
        main.getFromGuiToServer().put(passwordTextField.getText());
        String result = main.getFromServerToGui().get();

        loginMessageText.setText(result);
    }

    public void setMessageText(String message){
        loginMessageText.setText(message);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
