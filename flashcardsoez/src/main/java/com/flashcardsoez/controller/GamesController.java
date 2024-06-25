package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.DeckDAO;
import com.flashcardsoez.model.Deck;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class GamesController implements Initializable {

    @FXML
    private ComboBox<Deck> deckBox1;
    @FXML
    private Rectangle thumbnail1;

    private BorderPane ParentContent;
    public ObservableList<Deck> deckList;

    public void playChoiceGame() {
        Deck selectedDeck = deckBox1.getSelectionModel().getSelectedItem();

        if (selectedDeck == null || selectedDeck.getCardList().size() < 4) {
            // Show an alert if the deck is not selected or does not have at least 4 cards
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Deck");
            alert.setHeaderText(null);
            alert.setContentText("Please select a deck with at least 4 cards.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChoiceGame.fxml"));
            Parent parent = loader.load();
            ChoiceGameController choiceGameController = loader.getController();
            choiceGameController.setData(selectedDeck);
            ParentContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadThumbnail() {
        Image choicegame = new Image(getClass().getResourceAsStream("/images/choicegame.png"));
        thumbnail1.setFill(new ImagePattern(choicegame));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        deckList = FXCollections.observableArrayList(new DeckDAO().getList());
        setStringConverter();
        deckBox1.setItems(deckList);

        loadThumbnail();
    }

    public void setParentContent(BorderPane mainContent) {
        this.ParentContent = mainContent;
    }

    public void setStringConverter() {

        deckBox1.setConverter(new StringConverter<Deck>() {
            @Override
            public String toString(Deck deck) {
                return deck != null ? deck.getTitle() : "";
            }

            @Override
            public Deck fromString(String string) {
                return null;
            }
        });

    }
}
