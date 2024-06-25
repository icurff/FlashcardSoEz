package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.TestDAO;
import com.flashcardsoez.DAO.TestScoreDAO;
import com.flashcardsoez.model.Course;
import com.flashcardsoez.model.Test;
import com.flashcardsoez.model.TestScore;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.JOptionPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ScoreManagementController implements Initializable {

    @FXML
    TableView<TestScore> tableView;
    @FXML
    ComboBox<Test> testBox;
    @FXML
    TextField testName;
    @FXML
    TextField testResultId;
    @FXML
    TextField testScoreField;
    public ObservableList<TestScore> testResults;
    public ObservableList<Test> testList;
    public Course course;

    public void updateTestName() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update the test name?", "Update Test Name", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Test test = testBox.getSelectionModel().getSelectedItem();
            test.setTitle(testName.getText());
            new TestDAO().update(test);

            // Refresh the combo box
            testBox.getItems().clear();
            testBox.setItems(FXCollections.observableArrayList(new TestDAO().getList()));
            testBox.getSelectionModel().select(test);

            JOptionPane.showMessageDialog(null, "Test name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadTestScore() {
        testResultId.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getId()));
        testScoreField.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getScore()));
    }

    public void updateScore() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update the score?", "Update Score", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            TestScoreDAO ts = new TestScoreDAO();
            TestScore testScore = ts.getById(Integer.parseInt(testResultId.getText()));
            testScore.setScore(Double.parseDouble(testScoreField.getText()));
            ts.update(testScore);

            loadTestScoreTable(testBox.getSelectionModel().getSelectedItem());

            JOptionPane.showMessageDialog(null, "Score updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteTest() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this test?", "Delete Test", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            Test test = testBox.getSelectionModel().getSelectedItem();
            new TestDAO().delete(test);
            testList.remove(test);

            loadTestScoreTable(testBox.getSelectionModel().getSelectedItem()); // Reload table

            JOptionPane.showMessageDialog(null, "Test deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadTestScoreTable(Test test) {
        tableView.getItems().clear();
        testResults = FXCollections.observableArrayList(new TestDAO().getById(test.getId()).getTestResult());
        tableView.setItems(testResults);
    }

    public void loadTestScoreDetail() {
        testResultId.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getId()));
        testScoreField.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getScore()));

    }

    public void initTestScoreTable() {
        TableColumn<TestScore, Integer> userIdCol = new TableColumn<>("Id");
        TableColumn<TestScore, String> usernameCol = new TableColumn<>("Username");
        TableColumn<TestScore, String> userEmailCol = new TableColumn<>("Email");
        TableColumn<TestScore, String> userScoreCol = new TableColumn<>("Score");
        TableColumn<TestScore, String> userFinishTime = new TableColumn<>("Finished At");

        userIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStudent().getId()).asObject());
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getUsername()));
        userEmailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getEmail()));
        userScoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        userFinishTime.setCellValueFactory(new PropertyValueFactory<>("finished_At"));
        tableView.getColumns().addAll(userIdCol, usernameCol, userEmailCol, userScoreCol, userFinishTime);
    }

    public void setStringConverter() {
        testBox.setConverter(new StringConverter<Test>() {
            @Override
            public String toString(Test test) {
                return test != null ? test.getTitle() : "";
            }

            @Override
            public Test fromString(String string) {
                return null;
            }
        });
    }

    public void setCourse(Course course) {
        testList = FXCollections.observableArrayList(new TestDAO().getListByCourseId(course.getId()));
        testBox.setItems(testList);
        if (!testList.isEmpty()) {
            testBox.setValue(testList.get(0));
            loadTestName();

        }
        initTestScoreTable();
    }

    public void loadTestName() {
        testName.setText(testBox.getSelectionModel().getSelectedItem().getTitle());
        loadTestScoreTable(testBox.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStringConverter();
    }

    public void exportToXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        Stage stage = (Stage) tableView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                Document doc = new Document();
                Element rootElement = new Element("TestScores");
                doc.setRootElement(rootElement);

                ObservableList<TestScore> testScores = tableView.getItems();
                for (TestScore testScore : testScores) {
                    Element testScoreElement = new Element("TestScore");

                    Element idElement = new Element("Id");
                    idElement.setText(String.valueOf(testScore.getId()));
                    testScoreElement.addContent(idElement);

                    Element usernameElement = new Element("Username");
                    usernameElement.setText(testScore.getStudent().getUsername());
                    testScoreElement.addContent(usernameElement);

                    Element emailElement = new Element("Email");
                    emailElement.setText(testScore.getStudent().getEmail());
                    testScoreElement.addContent(emailElement);

                    Element scoreElement = new Element("Score");
                    scoreElement.setText(String.valueOf(testScore.getScore()));
                    testScoreElement.addContent(scoreElement);

                    Element finishedAtElement = new Element("FinishedAt");
                    finishedAtElement.setText(testScore.getFinished_At().toString());
                    testScoreElement.addContent(finishedAtElement);

                    rootElement.addContent(testScoreElement);
                }

                XMLOutputter xmlOutputter = new XMLOutputter();
                xmlOutputter.setFormat(Format.getPrettyFormat());
                xmlOutputter.output(doc, new FileWriter(file));

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Test scores were successfully exported to XML file.");
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while exporting to XML file.");
                alert.showAndWait();
            }
        }
    }
}
