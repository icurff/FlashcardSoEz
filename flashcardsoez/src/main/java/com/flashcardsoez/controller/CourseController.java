package com.flashcardsoez.controller;

import com.flashcardsoez.model.Course;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class CourseController implements Initializable {

    @FXML
    private Label courseName;
    @FXML
    private Label courseTeacher;

    public void setData(Course c) {
        courseName.setText(c.getTitle());
        courseTeacher.setText("Creator: " + c.getTeacher().getUsername());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
