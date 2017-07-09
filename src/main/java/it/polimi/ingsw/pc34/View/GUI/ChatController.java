package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by Povaz on 03/07/2017.
 */
@SuppressWarnings("restriction")
public class ChatController{
    private MainGUI mainGUI;

    @FXML private Button sendMessageButton;
    @FXML private TextField messageTextField;
    @FXML private VBox chatVBOX;
    @FXML private ScrollPane chatScrollPane;

    public void initializeThread(){
        chatScrollPane.vvalueProperty().bind(chatVBOX.heightProperty());
        (new Thread(() -> {
            String result;
            do {
                result = mainGUI.getChatFromServer().get();
                final String toLambda = result;
                Platform.runLater(() -> {
                    if(!toLambda.equals("/close")){
                        addMessage(toLambda);
                    }
                });
            } while(!result.equals("/close"));
        })).start();
    }

    @FXML private void enterPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            messageClick();
        }
    }

    @FXML public void messageClick() {
        if(messageTextField.getText().equals("")){
            return;
        }

        String message = "/chat" + messageTextField.getText();
        messageTextField.clear();

        mainGUI.getFromGuiToServer().put(message);
        mainGUI.getFromServerToGui().get();
    }

    public synchronized void addMessage(String message){
        if(message.startsWith("/error")){
            Text error = new Text(message.substring(6));
            error.setFill(Color.RED);
            error.setFont(new Font("System Bold", 15));
            error.setWrappingWidth(490);
            chatVBOX.getChildren().add(error);
        }
        else if(message.startsWith("/chat")){
            Text text = new Text(message.substring(5));
            text.setWrappingWidth(490);
            chatVBOX.getChildren().add(text);
        }
    }

    public void setMainGUI(MainGUI mainGUI){
        this.mainGUI = mainGUI;
    }
}
