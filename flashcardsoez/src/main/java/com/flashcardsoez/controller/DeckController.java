package com.flashcardsoez.controller;

import com.flashcardsoez.model.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class DeckController {

    @FXML
    private Rectangle deckImg;
    @FXML
    private Label deckTitle;
    @FXML
    private Label cardCount;

    public void setData(Deck deck) {

        Image thumbnail = new Image(getClass().getResourceAsStream(deck.getThumbnailPath()));

        deckImg.setFill(new ImagePattern(thumbnail));
        deckTitle.setText(deck.getTitle());
        cardCount.setText(deck.getCardList().size() + " terms");
    }
}
