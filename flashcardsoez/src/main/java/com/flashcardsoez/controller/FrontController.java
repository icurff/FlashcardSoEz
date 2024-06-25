package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.UserDAO;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.User;
import com.flashcardsoez.model.Otp;

import com.flashcardsoez.utils.HibernateUtil;
import com.flashcardsoez.utils.SecureUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class FrontController {

    @FXML
    private StackPane frontStackPane;
    @FXML
    private Pane signinPane;
    @FXML
    private Pane registerPane;
    @FXML
    private Pane resetPane;
    @FXML
    private TextField usernameReg;
    @FXML
    private TextField emailReg;
    @FXML
    private TextField passwordReg;
    @FXML
    private Button sigButton;
    @FXML
    private TextField keySig;
    @FXML
    private TextField passwordSig;
    @FXML
    private TextField resetEmail;
    @FXML
    private TextField resetPassword;
    @FXML
    private TextField resetOtp;

    User user = new User();

    public void handleSigninPane() {
        switchPane(signinPane);
        signinPane.setVisible(true);
    }

    public void handleRegisterPane() {
        switchPane(registerPane);
        registerPane.setVisible(true);
    }

    public void handleResetPane() {
        switchPane(resetPane);
        resetPane.setVisible(true);
    }

    public void handleSignin() {
        //keySig = username

        if (keySig.getText().isEmpty() || passwordSig.getText().isEmpty()) {
            new Alert(AlertType.ERROR, "Username and password cannot be empty").showAndWait();
            return;
        }

        User u = new UserDAO().getUserByUsername(keySig.getText().trim());
        String candidate = passwordSig.getText();

        if (u != null && SecureUtil.isEqual(candidate, u.getPassword())) {
            try {
                System.out.println("It matches");
                FlashcardSoEz.curUser = u;
                Parent parent = FXMLLoader.load(getClass().getResource("/view/FlashcardSoEz.fxml"));
                Scene scene = sigButton.getScene();
                scene.setRoot(parent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new Alert(AlertType.ERROR, "Username or Password is incorrect").showAndWait();

        }

    }

    public void handleRegister() {
        UserDAO userDAO = new UserDAO();

        if (usernameReg.getText().isEmpty() || emailReg.getText().isEmpty() || passwordReg.getText().isEmpty()) {
            new Alert(AlertType.ERROR, "Username, email, and password cannot be empty").showAndWait();
            return;
        }

        if (!emailReg.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            new Alert(AlertType.ERROR, "Email format is invalid").showAndWait();
            return;
        }
        User existingUser = userDAO.getUserByUsername(usernameReg.getText());
        if (existingUser != null) {
            new Alert(Alert.AlertType.ERROR, "Username already exists").showAndWait();
            return;
        }
        User existingEmail = userDAO.getUserByEmail(emailReg.getText());
        if (existingEmail != null) {
            new Alert(Alert.AlertType.ERROR, "Email  already exists").showAndWait();
            return;
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            user.setUsername(usernameReg.getText());
            user.setEmail(emailReg.getText());
            user.setPassword(SecureUtil.hash(passwordReg.getText()));
            session.persist(user);
            tx.commit();
        }

        new Alert(AlertType.INFORMATION, "Register Successfully").showAndWait();

    }

    public void sendOtpToEmail() {
        UserDAO userDAO = new UserDAO();
        User existingEmail = userDAO.getUserByEmail(resetEmail.getText().trim());

        if (existingEmail == null) {
            new Alert(Alert.AlertType.ERROR, "There is no account associated with this email").showAndWait();
            return;
        }

        new Alert(AlertType.INFORMATION, "Please check your email to get the OTP code").show();

        Otp newOtp = new Otp(existingEmail);
        existingEmail.setOtp(newOtp);
        new Thread(() -> userDAO.update(existingEmail)).start();
        new Thread(() -> SecureUtil.sendOtp(existingEmail)).start();

    }

    public void resetPassword() {
        UserDAO userDAO = new UserDAO();
        User existingEmail = userDAO.getUserByEmail(resetEmail.getText().trim());
        if (existingEmail == null) {
            new Alert(Alert.AlertType.ERROR, "There is no account associated with this email").showAndWait();
            return;
        }

        Otp userOtp = existingEmail.getOtp();

        if (!userOtp.isExpired() && userOtp.getCode().equals(resetOtp.getText().trim())) {
            existingEmail.setPassword(SecureUtil.hash(resetPassword.getText().trim()));

            // Remove the OTP after successful password reset
            existingEmail.setOtp(null);
            userDAO.update(existingEmail);
            new Alert(Alert.AlertType.INFORMATION, "Password updated successfully").showAndWait();
            resetEmail.setText("");
            resetOtp.setText("");
            resetPassword.setText("");

        } else {
            new Alert(Alert.AlertType.ERROR, "OTP is invalid").showAndWait();
        }
    }

    public void switchPane(Pane pane) {
        frontStackPane.getChildren().setAll(pane);
    }

}
