package com.flashcardsoez.controller;

import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Deck;
import java.net.URL;
import java.util.Collections;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DeckDetailController implements Initializable {

    @FXML
    private Label deckTitle;
    @FXML
    private Label deckAuthor;

    @FXML
    private Label cardContent;
    @FXML
    private Label progress;
    @FXML
    private Button backBtn;
    @FXML
    private Button forwardBtn;
    @FXML
    private Button shuffleBtn;
    @FXML
    private VBox cardVBox;
    private ObservableList<Card> cardList;
    int totalCard;
    private Boolean flag;
    private int curCardIndex;

    public void showCard() {
        Card c = cardList.get(curCardIndex);
        cardContent.setText(c.getQuestion());

        cardVBox.setOnMouseClicked(e -> {
            if (flag == false) {
                cardContent.setText(c.getAnswer());
                flag = true;
            } else {
                cardContent.setText(c.getQuestion());
                flag = false;
            }
        });

        progress.setText((curCardIndex + 1) + "/" + totalCard);

    }

    public void shuffle() {
        Collections.shuffle(cardList);
        curCardIndex = 0;
        showCard();
    }

    public void handleForwardBtn() {
        curCardIndex += 1;
        if (curCardIndex >= totalCard) {
            curCardIndex = totalCard - 1;
        }
        showCard();
    }

    public void handleBackBtn() {
        curCardIndex -= 1;
        if (curCardIndex < 0) {
            curCardIndex = 0;
        }
        showCard();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backBtn.setGraphic(new ImageView(new Image("/images/Back.png")));
        forwardBtn.setGraphic(new ImageView(new Image("/images/Forward.png")));
        shuffleBtn.setGraphic(new ImageView(new Image("/images/Shuffle.png")));
        flag = false;
        curCardIndex = 0;

    }

    public void setData(Deck deck) {
        cardList = FXCollections.observableArrayList();

        cardList.setAll(deck.getCardList());
        totalCard = cardList.size();
        deckTitle.setText(deck.getTitle());
        deckAuthor.setText("Author: " + deck.getAuthor().getUsername());
        showCard();

    }

}
