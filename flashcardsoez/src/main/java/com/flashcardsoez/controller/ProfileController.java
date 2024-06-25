package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.UserDAO;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.User;
import com.flashcardsoez.utils.SecureUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ProfileController implements Initializable {

    @FXML
    private Circle avt;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField oldPassField;
    @FXML
    private TextField newPassField;

    public void updateInfo() {
        UserDAO userDAO = new UserDAO();
        User existingEmail = userDAO.getUserByEmail(emailField.getText());
        if (existingEmail != null) {
            new Alert(Alert.AlertType.ERROR, "Email  already exists").showAndWait();
            return;
        }
        
        FlashcardSoEz.curUser.setFirstName(firstnameField.getText().trim());
        FlashcardSoEz.curUser.setLastName(lastnameField.getText().trim());
        FlashcardSoEz.curUser.setEmail(emailField.getText().trim());

        new UserDAO().update(FlashcardSoEz.curUser);

        new Alert(Alert.AlertType.INFORMATION, "Update Successfully").showAndWait();

    }

    public void changePass() {
        String newPass = SecureUtil.hash(newPassField.getText().trim());
        if (SecureUtil.isEqual(oldPassField.getText().trim(), FlashcardSoEz.curUser.getPassword())) {
            FlashcardSoEz.curUser.setPassword(newPass);
            new UserDAO().update(FlashcardSoEz.curUser);
            new Alert(Alert.AlertType.INFORMATION, "Password updated successfully").showAndWait();
            oldPassField.clear();
            newPassField.clear();
        } else {
            new Alert(Alert.AlertType.ERROR, "Old password is incorrect").showAndWait();
        }
    }

    public void loadAvt() {

        Image defaultImage = new Image("/images/defaultAvt.png");
        avt.setFill(new ImagePattern(defaultImage));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAvt();
        firstnameField.setText(FlashcardSoEz.curUser.getFirstName());
        lastnameField.setText(FlashcardSoEz.curUser.getLastName());
        emailField.setText(FlashcardSoEz.curUser.getEmail());

    }

}
