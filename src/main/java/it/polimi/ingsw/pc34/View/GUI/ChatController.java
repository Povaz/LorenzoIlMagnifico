package it.polimi.ingsw.pc34.View.GUI;

import com.sun.prism.paint.Paint;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Povaz on 03/07/2017.
 */
public class ChatController extends Application {
    private Main main;

    @FXML private Button sendMessageButton;
    @FXML private TextField messageTextField;
    @FXML private VBox chatVBOX;
    @FXML private ScrollPane chatScrollPane;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Scene scene = new Scene (root);

        primaryStage.setTitle("La migliore Chat");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
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

        String message = "/chat" + "username" + "/: " + messageTextField.getText();
        messageTextField.clear();


        message = "/chatpaoloTrilli:ciaosonopaoloefacciolepuzzette,masoloneigiornidisparimasoloneigiornidispari";

        String received = message;
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

    public void setMain(Main main){
        this.main = main;
    }
    
    public static void main(String[] args){
    	launch(ChatController.class);
    }
}
