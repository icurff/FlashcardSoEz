package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.DeckDAO;
import com.flashcardsoez.model.Deck;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DecksController implements Initializable {

    @FXML
    private GridPane grid;
    private BorderPane ParentContent;
    private ObservableList<Deck> deckList;
    private List<String> listPath;

    public void setParentContent(BorderPane mainContent) {
        this.ParentContent = mainContent;
    }

    public List intitListPath() {
        List<String> list = new ArrayList();
        for (int i = 1; i <= 9; i++) {
            list.add("/images/deck/" + i + ".png");
        }
        Collections.shuffle(list);
        return list;
    }

    public void loadDecks(ObservableList<Deck> listOfDecks) {
        int column = 0;
        int row = 1;
        int imgIndex = 0; // Start with 0

        try {
            for (Deck deck : listOfDecks) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Deck.fxml"));
                VBox deckItem = fxmlLoader.load();

                // Show deck detail 
                deckItem.setOnMouseClicked((event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeckDetail.fxml"));
                        Parent parent = loader.load();
                        DeckDetailController deckDetailController = loader.getController();
                        deckDetailController.setData(deck);
                        ParentContent.setLeft(null);
                        ParentContent.setCenter(parent);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));

                // Set random thumbnail for deck to load
                if (imgIndex == listPath.size()) {
                    imgIndex = 0; // Reset index if it exceeds the list size
                }
                deck.setThumbnailPath(listPath.get(imgIndex));
                imgIndex++; // Increment after setting thumbnail

                DeckController deckController = fxmlLoader.getController();
                deckController.setData(deck);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                // Add the deckItem to the grid
                grid.add(deckItem, column++, row);
                grid.setMargin(deckItem, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckList = FXCollections.observableArrayList();
        deckList.addAll(new DeckDAO().getList());
        // get list random thumbnail 
        listPath = intitListPath();

        loadDecks(deckList);
    }
}
