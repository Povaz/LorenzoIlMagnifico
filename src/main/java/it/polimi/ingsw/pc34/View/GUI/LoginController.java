package it.polimi.ingsw.pc34.View.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public class LoginController {
    private MainGUI mainGUI;

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

        mainGUI.getFromGuiToServer().put("/login");

        mainGUI.getFromGuiToServer().put(usernameTextField.getText());
        mainGUI.getFromGuiToServer().put(passwordTextField.getText());

        String result = mainGUI.getFromServerToGui().get();
        if(result.equals("Login successful")){
            mainGUI.setUsername(usernameTextField.getText());
            mainGUI.showWaitingRoom();
        }
        else{
            loginMessageText.setText(result);
        }
    }

    @FXML protected void registerClick(){
        if(usernameTextField.getText().equals("") || passwordTextField.getText().equals("")){
            return;
        }

        mainGUI.getFromGuiToServer().put("/registration");

        mainGUI.getFromGuiToServer().put(usernameTextField.getText());
        mainGUI.getFromGuiToServer().put(passwordTextField.getText());

        String result = mainGUI.getFromServerToGui().get();
        loginMessageText.setText(result);
    }

    public void setMessageText(String message){
        loginMessageText.setText(message);
    }

    public void setMainGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
