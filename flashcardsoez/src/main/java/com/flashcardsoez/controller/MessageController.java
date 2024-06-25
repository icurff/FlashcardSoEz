package com.flashcardsoez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

public class MessageController implements Initializable {

    @FXML
    Label messContent;
    @FXML
    HBox container;
    @FXML
    TextFlow textFlow;

    public void setData(String mess,boolean isInternal) {

        messContent.setText(mess);
        if (isInternal) {
            container.setAlignment(Pos.CENTER_RIGHT);
            textFlow.getStyleClass().add("myMess");
            messContent.getStyleClass().add("white-lb");

        } else {
            container.setAlignment(Pos.CENTER_LEFT);
            textFlow.getStyleClass().add("externalMess");
             messContent.getStyleClass().add("black-lb");

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
