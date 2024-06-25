package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.CourseDAO;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Course;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CoursesController implements Initializable {

    @FXML
    private VBox courseVBox;
    private ObservableList<Course> courseList;
    private BorderPane ParentContent;

    public void setParentContent(BorderPane mainContent) {
        this.ParentContent = mainContent;
    }

    public void loadCourses() {

        for (Course course : courseList) {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/Course.fxml"));
                VBox courseItem = loader.load();
                courseItem.setOnMouseClicked((event -> {
                    if (FlashcardSoEz.curUser.getId() == course.getTeacher().getId()) {
                        try {
                            FXMLLoader smLoader = new FXMLLoader(getClass().getResource("/view/ScoreManagement.fxml"));
                            Parent parent = smLoader.load();

                            ScoreManagementController scoreManagementController = smLoader.getController();
                            scoreManagementController.setCourse(course);

                            ParentContent.setCenter(parent);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            FXMLLoader testsLoader = new FXMLLoader(getClass().getResource("/view/Tests.fxml"));
                            Parent parent = testsLoader.load();

                            TestsController testsController = testsLoader.getController();
                            testsController.initTests(course.getId());
                            testsController.setParentContent(ParentContent);

                            ParentContent.setCenter(parent);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                }));
                CourseController courseController = loader.getController();
                courseController.setData(course);

                courseVBox.getChildren().add(courseItem);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        courseList = FXCollections.observableArrayList();
        CourseDAO courseDAO = new CourseDAO();

        // Add courses created by the current user
        List<Course> createdCourses = courseDAO.getList();
        courseList.addAll(createdCourses);

        // Add courses joined by the current user
        List<Course> joinedCourses = courseDAO.getJoinedCourses();
        courseList.addAll(joinedCourses);

        loadCourses();
    }

}
