package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.TestDAO;
import com.flashcardsoez.DAO.CourseDAO;
import com.flashcardsoez.DAO.DeckDAO;
import com.flashcardsoez.DAO.CardDAO;
import com.flashcardsoez.DAO.UserDAO;
import com.flashcardsoez.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Test;
import com.flashcardsoez.model.Deck;
import com.flashcardsoez.model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class NewStuffController implements Initializable {

    @FXML
    private TextField courseNameField;
    @FXML
    private TextField deckNameField;
    @FXML
    private TextField testNameField;
    @FXML
    private TextField studentUsernameField;

    @FXML
    private ComboBox<Course> courseTestBox;
    @FXML
    private ComboBox<Deck> deckTestBox;
    @FXML
    private ComboBox<Deck> deckCardBox;
    @FXML
    private ComboBox<Course> courseUserBox;
    @FXML
    private TextField questionField;
    @FXML
    private TextField answerField;

    public ObservableList<Course> courseList;
    public ObservableList<Deck> deckList;

    public void handleNewCourse() {
        Course course = new Course();
        course.setTitle(courseNameField.getText().trim());
        course.setTeacher(FlashcardSoEz.curUser);
        courseList.add(course);
        new CourseDAO().add(course);
        // Clear the text field after adding the course
        courseNameField.clear();
    }

    public void handleNewDeck() {
        Deck deck = new Deck();
        deck.setTitle(deckNameField.getText().trim());
        deck.setAuthor(FlashcardSoEz.curUser);
        deckList.add(deck);
        new DeckDAO().add(deck);
        // Clear the text fields after adding the deck
        deckNameField.clear();

    }

    public void handleNewTest() {
        Deck selectedDeck = new DeckDAO().getById(deckTestBox.getSelectionModel().getSelectedItem().getId());
        if (selectedDeck == null || selectedDeck.getCardList().size() < 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient Cards");
            alert.setHeaderText(null);
            alert.setContentText("Please select a deck with at least 4 cards to create a test.");
            alert.showAndWait();
            return;
        }
        Test test = new Test();
        test.setTitle(testNameField.getText().trim());
        Course course = courseTestBox.getSelectionModel().getSelectedItem();
        test.setCourse(course);
        test.setTestData(selectedDeck);
        new TestDAO().add(test);
        // Clear the text field after adding the test
        testNameField.clear();
    }

    public void handleNewCourseMember() {
        CourseDAO cd = new CourseDAO();
        Course course = cd.getById(courseUserBox.getSelectionModel().getSelectedItem().getId());

        String username = studentUsernameField.getText().trim();
        User user = new UserDAO().getUserByUsername(username);

        if (user == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("User Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No user found with the username: " + username);
            alert.showAndWait();
        } else {

            course.getStudentsInCourse().add(user);
            // update course data
            cd.addUserToCourse(course);
            studentUsernameField.clear();
        }
    }

    public void handleNewCard() {
        Card card = new Card();
        card.setQuestion(questionField.getText().trim());
        card.setAnswer(answerField.getText().trim());
        card.setDeck(deckCardBox.getSelectionModel().getSelectedItem());
        new CardDAO().add(card);
        // Clear the text fields after adding the card
        questionField.clear();
        answerField.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseList = FXCollections.observableArrayList(new CourseDAO().getList());
        deckList = FXCollections.observableArrayList(new DeckDAO().getList());

        setStringConverter();

        courseTestBox.setItems(courseList);
        courseUserBox.setItems(courseList);
        deckTestBox.setItems(deckList);
        deckCardBox.setItems(deckList);
    }

    public void setStringConverter() {
        courseTestBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getTitle() : "";
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });
        courseUserBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getTitle() : "";
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });

        deckTestBox.setConverter(new StringConverter<Deck>() {
            @Override
            public String toString(Deck deck) {
                return deck != null ? deck.getTitle() : "";
            }

            @Override
            public Deck fromString(String string) {
                return null;
            }
        });
        deckCardBox.setConverter(new StringConverter<Deck>() {
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
