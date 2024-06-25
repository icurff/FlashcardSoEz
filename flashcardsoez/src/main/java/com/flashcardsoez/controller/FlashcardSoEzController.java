package com.flashcardsoez.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.FlashcardSoEzClient;
import java.awt.Desktop;
import java.net.URI;
import javafx.scene.control.TextField;

public class FlashcardSoEzController implements Initializable {

    @FXML
    private BorderPane mainContent;
    @FXML
    private Circle curAvt;
    @FXML
    private Label curUsername;
    @FXML
    TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button crawlBtn;
    @FXML
    private Button logoutBtn;

    private Parent mainChatContent;
    private MainChatController mainChatController;

    public static FlashcardSoEzClient client;

    public void searchInBrowser() {
        try {

            String url = searchField.getText().trim();
            if (!"".equals(url)) {
                Desktop desk = Desktop.getDesktop();
                desk.browse(new URI("http://studylib.net/search/" + url + "?section=flashcards"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDecks() {
        mainContent.setCenter(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Decks.fxml"));
            Parent parent = loader.load();
            DecksController decksController = loader.getController();
            decksController.setParentContent(mainContent);
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showCourses() {
        mainContent.setLeft(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Courses.fxml"));
            Parent parent = loader.load();
            CoursesController coursesController = loader.getController();
            coursesController.setParentContent(mainContent);
            mainContent.setLeft(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showHome() {
        showDecks();
        showCourses();

    }

    public void showGames() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Games.fxml"));
            Parent parent = loader.load();
            GamesController gamesController = loader.getController();
            gamesController.setParentContent(mainContent);
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showMainChat() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            if (mainChatContent == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainChat.fxml"));
                mainChatContent = loader.load();
                mainChatController = loader.getController();

                // Create the client and pass the MainChatController
                // host is the ip address of server and port
                Socket socket = new Socket("localhost", 7001);
                client = new FlashcardSoEzClient(socket, mainChatController);
                client.listenForMessages();
            }
            mainContent.setCenter(mainChatContent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showNewStuffPane() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/NewStuff.fxml"));
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showCrawler() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/StudyLibScraper.fxml"));
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showProfile() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/Profile.fxml"));
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showEditPane() {
        mainContent.setCenter(null);
        mainContent.setLeft(null);
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/Edit.fxml"));
            mainContent.setCenter(parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initProfile() {
        Image avtImg = new Image(FlashcardSoEz.curUser.getAvtPath());
        curAvt.setFill(new ImagePattern(avtImg));
        curUsername.setText(FlashcardSoEz.curUser.getUsername());
    }

    public void logout() {
        try {
            FlashcardSoEz.curUser = null;
            Parent parent = FXMLLoader.load(getClass().getResource("/view/Front.fxml"));
            Stage stage = (Stage) mainContent.getScene().getWindow();
            Scene scene = new Scene(parent, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBtn.setGraphic(new ImageView(new Image("/images/Search.jpg")));
        addBtn.setGraphic(new ImageView(new Image("/images/Plus.png")));
        editBtn.setGraphic(new ImageView(new Image("/images/Edit.png")));
        logoutBtn.setGraphic(new ImageView(new Image("/images/Logout.png")));
        crawlBtn.setGraphic(new ImageView(new Image("/images/Download.png")));
        searchBtn.getStyleClass().add("trans-bg");
        addBtn.getStyleClass().add("trans-bg");
        editBtn.getStyleClass().add("trans-bg");
        logoutBtn.getStyleClass().add("trans-bg");
        crawlBtn.getStyleClass().add("trans-bg");
        initProfile();
        showHome();
    }
}
