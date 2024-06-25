package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.CardDAO;
import com.flashcardsoez.DAO.CourseDAO;
import com.flashcardsoez.DAO.DeckDAO;
import com.flashcardsoez.DAO.UserDAO;
import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Course;
import com.flashcardsoez.model.Deck;
import com.flashcardsoez.model.User;
import com.flashcardsoez.utils.HibernateUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EditController implements Initializable {

    @FXML
    StackPane editStackPane;
    @FXML
    VBox courseVBox;
    @FXML
    VBox deckVBox;
    @FXML
    TextField courseNameField;
    @FXML
    TextField studentIdField;
    @FXML
    TextField deckNameField;
    @FXML
    TextField cardIdField;
    @FXML
    TextField cardFrontField;
    @FXML
    TextField cardBackField;
    @FXML
    TableView<User> courseTable;
    @FXML
    TableView<Card> deckTable;
    @FXML
    private ComboBox<Course> courseBox;
    @FXML
    private ComboBox<Deck> deckBox;
    public ObservableList<Course> courseList;
    public ObservableList<Deck> deckList;
    public ObservableList<User> studentsInCourseList;
    public ObservableList<Card> cardsInDeckList;

    public void removeStudentFromCourse() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this student from the course?", "Remove Student", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                CourseDAO cd = new CourseDAO();
                UserDAO ud = new UserDAO();

                Course course = session.get(Course.class, courseBox.getSelectionModel().getSelectedItem().getId());

                User user = session.get(User.class, Integer.valueOf(studentIdField.getText()));

                // bidirectional update
                user.getCourseList().remove(course);
                course.getStudentsInCourse().remove(user);
                session.merge(user);
                session.merge(course);

                tx.commit();

                cd.update(course);
                ud.update(user);

                loadCourseTable(courseBox.getSelectionModel().getSelectedItem()); // Reload course table
                courseTable.refresh(); // Refresh the table view

                JOptionPane.showMessageDialog(null, "Student removed from course successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void removeCardFromDeck() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this card from the deck?", "Remove Card", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            CardDAO cd = new CardDAO();
            Card card = cd.getById(Integer.parseInt(cardIdField.getText()));
            cd.delete(card);

            loadDeckTable(deckBox.getSelectionModel().getSelectedItem()); // Reload deck table
            deckTable.refresh();

            JOptionPane.showMessageDialog(null, "Card removed from deck successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteCourse() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this course?", "Delete Course", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Course course = courseBox.getSelectionModel().getSelectedItem();
            new CourseDAO().delete(course);
            courseList.remove(course);

            loadCourseTable(courseBox.getSelectionModel().getSelectedItem()); // Reload course table

            JOptionPane.showMessageDialog(null, "Course deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteDeck() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this deck?", "Delete Deck", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Deck deck = deckBox.getSelectionModel().getSelectedItem();
            if (!deck.getCardTestList().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Some test use this deck as data so you cant delete this deck", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            new DeckDAO().delete(deck);
            deckList.remove(deck);

            loadDeckTable(deckBox.getSelectionModel().getSelectedItem()); // Reload deck table

            JOptionPane.showMessageDialog(null, "Deck deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateCourseName() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update the course name?", "Update Course Name", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Course course = courseBox.getSelectionModel().getSelectedItem();
            course.setTitle(courseNameField.getText());
            new CourseDAO().update(course);

            // Refresh the combo box
            courseBox.getItems().clear();
            courseBox.setItems(FXCollections.observableArrayList(new CourseDAO().getList()));
            courseBox.getSelectionModel().select(course);

            JOptionPane.showMessageDialog(null, "Course name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateDeckName() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update the deck name?", "Update Deck Name", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Deck deck = deckBox.getSelectionModel().getSelectedItem();
            deck.setTitle(deckNameField.getText());
            new DeckDAO().update(deck);

            // Refresh the combo box
            deckBox.getItems().clear();
            deckBox.setItems(FXCollections.observableArrayList(new DeckDAO().getList()));
            deckBox.getSelectionModel().select(deck);

            JOptionPane.showMessageDialog(null, "Deck name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateCard() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this card?", "Update Card", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            CardDAO cd = new CardDAO();
            Card card = cd.getById(Integer.parseInt(cardIdField.getText()));
            card.setQuestion(cardFrontField.getText());
            card.setAnswer(cardBackField.getText());
            cd.update(card);

            loadDeckTable(deckBox.getSelectionModel().getSelectedItem()); // Reload deck table

            JOptionPane.showMessageDialog(null, "Card updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadCourseName() {
        courseNameField.setText(courseBox.getSelectionModel().getSelectedItem().getTitle());
        loadCourseTable(courseBox.getSelectionModel().getSelectedItem());
    }

    public void loadDeckName() {
        deckNameField.setText(deckBox.getSelectionModel().getSelectedItem().getTitle());
        loadDeckTable(deckBox.getSelectionModel().getSelectedItem());

    }

    public void loadCardDetail() {
        cardIdField.setText(String.valueOf(deckTable.getSelectionModel().getSelectedItem().getId()));
        cardFrontField.setText(deckTable.getSelectionModel().getSelectedItem().getQuestion());
        cardBackField.setText(deckTable.getSelectionModel().getSelectedItem().getAnswer());

    }

    public void loadStudentDetail() {
        studentIdField.setText(String.valueOf(courseTable.getSelectionModel().getSelectedItem().getId()));
    }

    public void initTable() {
        initDeckTable();
        initCourseTable();
    }

    public void loadCourseTable(Course course) {
        courseTable.getItems().clear();
        studentsInCourseList = FXCollections.observableArrayList(new CourseDAO().getById(course.getId()).getStudentsInCourse());

        courseTable.setItems(studentsInCourseList);

    }

    public void loadDeckTable(Deck deck) {
        deckTable.getItems().clear();
        cardsInDeckList = FXCollections.observableArrayList(new DeckDAO().getById(deck.getId()).getCardList());
        deckTable.setItems(cardsInDeckList);
    }

    public void switchPane(VBox vbox) {
        editStackPane.getChildren().setAll(vbox);
    }

    public void handleDeck() {
        switchPane(deckVBox);
        deckVBox.setVisible(true);
        loadDeckName();

    }

    public void handleCourse() {
        switchPane(courseVBox);
        courseVBox.setVisible(true);
        loadCourseName();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseList = FXCollections.observableArrayList(new CourseDAO().getList());
        deckList = FXCollections.observableArrayList(new DeckDAO().getList());
        setStringConverter();
        courseBox.setItems(courseList);
        deckBox.setItems(deckList);
        if (!courseList.isEmpty()) {
            courseBox.setValue(courseList.get(0));
            loadCourseName();

        }
        if (!deckList.isEmpty()) {
            deckBox.setValue(deckList.get(0));
            loadDeckName();
        }
        initTable();

    }

    public void initDeckTable() {
        TableColumn<Card, String> cardIdCol = new TableColumn<>("Id");
        TableColumn<Card, String> cardQuestionCol = new TableColumn<>("Front");
        TableColumn<Card, String> cardAnswerCol = new TableColumn<>("Back");

        cardIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cardQuestionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        cardAnswerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));

        deckTable.getColumns().addAll(cardIdCol, cardQuestionCol, cardAnswerCol);

    }

    public void initCourseTable() {

        TableColumn<User, String> userIdCol = new TableColumn<>("Id");
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        TableColumn<User, String> userFullNameCol = new TableColumn<>("Full Name");
        TableColumn<User, String> userFirstNameCol = new TableColumn<>("First Name");
        TableColumn<User, String> userLastNameCol = new TableColumn<>("Last Name");
        userFullNameCol.getColumns().addAll(userFirstNameCol, userLastNameCol);
        TableColumn<User, String> userEmailCol = new TableColumn<>("Email");

        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        userLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        courseTable.getColumns().addAll(userIdCol, usernameCol, userFullNameCol, userEmailCol);
    }

    public void setStringConverter() {
        courseBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getTitle() : "";
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });
        deckBox.setConverter(new StringConverter<Deck>() {
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
