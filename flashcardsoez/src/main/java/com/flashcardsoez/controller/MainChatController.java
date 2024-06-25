package com.flashcardsoez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;

public class MainChatController implements Initializable {

    @FXML
    private TextField messageField;
   
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messVbox;

    public void loadMessToVBox(String text, boolean isInternal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Message.fxml"));
            Parent parent = loader.load();
            MessageController messageController = loader.getController();
            messageController.setData(text, isInternal);

            // Ensure adding the new message is done on the JavaFX Application Thread
            Platform.runLater(() -> {
                messVbox.getChildren().add(parent);

                // Always scroll to the latest message
                scrollPane.applyCss();
                scrollPane.layout();
                scrollPane.setVvalue(scrollPane.getVmax());
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage() {
        String text = messageField.getText();
        if (text != null && !text.trim().isEmpty()) {
            loadMessToVBox(text, true);
            FlashcardSoEzController.client.sendMessage(text);
            messageField.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }
   
}
