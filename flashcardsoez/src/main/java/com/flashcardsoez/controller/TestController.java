package com.flashcardsoez.controller;

import com.flashcardsoez.model.Test;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class TestController {

    @FXML
    private Rectangle randomImg;
    @FXML
    private Label testTitle;

    public void setData(Test test , String path) {
        Image thumbnail = new Image(getClass().getResourceAsStream(path));

        randomImg.setFill(new ImagePattern(thumbnail));
        testTitle.setText(test.getTitle());

    }
}
