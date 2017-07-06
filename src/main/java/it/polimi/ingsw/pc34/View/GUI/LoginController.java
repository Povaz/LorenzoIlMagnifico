package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class LoginController {
    private Main main;

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Text loginMessageText;

    @FXML private void enterPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            loginClick();
        }
    }

    @FXML protected void loginClick(){
        if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")){
            return;
        }
        main.getFromGuiToServer().put("/login");
        main.getFromGuiToServer().put(usernameTextField.getText());
        System.out.println("put user");
        main.getFromGuiToServer().put(passwordTextField.getText());
        System.out.println("put pass");
        String result = main.getFromServerToGui().get();
        System.out.println("get res");

        System.out.println(result);
        if(result.equals("Login successful")){
            main.setUsername(usernameTextField.getText());

            main.showWaitingRoom();
        }
        else{
            loginMessageText.setText(result);
        }
    }

    @FXML protected void registerClick(){
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
