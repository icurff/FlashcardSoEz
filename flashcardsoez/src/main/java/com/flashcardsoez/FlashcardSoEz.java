package com.flashcardsoez;

import com.flashcardsoez.model.User;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlashcardSoEz extends Application {

    public static User curUser = new User();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Front.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinHeight(600.0);
            stage.setMinWidth(1200.0);

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
