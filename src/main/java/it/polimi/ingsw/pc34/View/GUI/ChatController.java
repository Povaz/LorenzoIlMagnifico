package it.polimi.ingsw.pc34.View.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.soap.Text;

/**
 * Created by Povaz on 03/07/2017.
 */
public class ChatController extends Application {
    @FXML private Button sendMessageButton;
    @FXML private TextField messageTextField;
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

    @FXML private void initialize() {
        TextField firstTextField = new TextField("Welcome to the MeglioChat!");
        chatScrollPane.setContent(firstTextField);
    }

    @FXML public void messageClick() {
        String message = messageTextField.getText();
        messageTextField.clear();
        if (message.equals("")) {
            return;
        }
        else {
        }
    }
}
