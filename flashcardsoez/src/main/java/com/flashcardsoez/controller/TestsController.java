package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.TestDAO;
import com.flashcardsoez.model.Test;

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

public class TestsController implements Initializable {

    @FXML
    private GridPane grid;
    private BorderPane ParentContent;
    private ObservableList<Test> testList;
    private List<String> listPath;

    public List intitListPath() {
        List<String> list = new ArrayList();
        for (int i = 1; i <= 9; i++) {
            list.add("/images/deck/" + i + ".png");
        }
        Collections.shuffle(list);
        return list;
    }

    public void setParentContent(BorderPane mainContent) {
        this.ParentContent = mainContent;
    }

    public void initTests(int id) {
        testList = FXCollections.observableArrayList();
        testList.addAll(new TestDAO().getListByCourseId(id));
        loadTests(testList);
    }

    public void loadTests(ObservableList<Test> listOfTests) {
        int column = 0;
        int row = 1;
        int imgIndex = 0;
        try {
            for (Test test : listOfTests) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Test.fxml"));
                VBox testItem = fxmlLoader.load();
                //load choice game
                testItem.setOnMouseClicked((event -> {
                    ParentContent.setLeft(null);
                    ParentContent.setCenter(null);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChoiceGame.fxml"));
                        Parent parent = loader.load();
                        ChoiceGameController choiceGameController = loader.getController();
                        choiceGameController.setData(test);
                        ParentContent.setCenter(parent);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));

                // Set random thumbnail to load
                if (imgIndex == listPath.size()) {
                    imgIndex = 0; // Reset index if it exceeds the list size
                }
                // load test interface
                TestController testController = fxmlLoader.getController();
                testController.setData(test, listPath.get(imgIndex));

                imgIndex++; // Increment after setting thumbnail

                if (column == 3) {
                    column = 0;
                    row++;
                }

                // Add the testItem to the grid
                grid.add(testItem, column++, row);
                grid.setMargin(testItem, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get list random thumbnail 
        listPath = intitListPath();
    }

}
