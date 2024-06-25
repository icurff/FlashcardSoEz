package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.TestScoreDAO;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Deck;
import com.flashcardsoez.model.Test;
import com.flashcardsoez.model.TestScore;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ChoiceGameController implements Initializable {

    @FXML
    Label choice1;
    @FXML
    Label choice2;
    @FXML
    Label choice3;
    @FXML
    Label choice4;
    @FXML
    Label scoreLabel;
    @FXML
    Label questionField;
    @FXML
    VBox choiceVBox;
    private Test test = null;
    private ObservableList<Card> cardList;
    private List<String> choices = new ArrayList<>(4);
    private List<String> answerMatrix;
    private int curCardIndex;
    private Card curCard;
    private int score = 0;
    private int totalCard = 0;
    private int scoreForEachQuestion;
    private int correctAnswer = 0;
    private boolean isTestMode = false;

    public void showCard() {
        curCard = cardList.get(curCardIndex);
        questionField.setText(curCard.getQuestion());

        initChoices(curCard.getAnswer());
        // load choices to label
        assignAnswers();
    }

    public void onChoiceSelected(MouseEvent event) {
        Label selectedChoice = (Label) event.getSource();
        String selectedText = selectedChoice.getText();

        if (curCard.getAnswer().equals(selectedText)) {
            score += scoreForEachQuestion;
            correctAnswer += 1;
            score = score > 100 ? 100 : score;
            scoreLabel.setText("Score: " + score);
        }
        curCardIndex += 1;
        if (curCardIndex < totalCard) {
            showCard();
        } else {
            showResult();
        }
    }

    public void initChoices(String correctAnswer) {
        choices.clear();
        choices.add(correctAnswer);

        // add three more random answers
        String randomAnswer;
        while (choices.size() < 4) {
            randomAnswer = answerMatrix.get(getRandomIndex(totalCard));
            if (!choices.contains(randomAnswer)) {
                choices.add(randomAnswer);
            }
        }

        Collections.shuffle(choices);
    }

    public void setData(Deck deck) {
        cardList = FXCollections.observableArrayList();
        cardList.setAll(deck.getCardList());

        totalCard = cardList.size();
        scoreForEachQuestion = (int) Math.round(100 / totalCard);

        Collections.shuffle(cardList);
        // init answer list
        answerMatrix = new ArrayList<>(totalCard);
        for (Card c : cardList) {
            answerMatrix.add(c.getAnswer());
        }
        Collections.shuffle(answerMatrix);

        showCard();

    }

    public void setData(Test t) {
        test = t;
        isTestMode = true;
        cardList = FXCollections.observableArrayList();
        cardList.setAll(test.getTestData().getCardList());

        totalCard = cardList.size();
        scoreForEachQuestion = (int) Math.round(100 / totalCard);

        Collections.shuffle(cardList);
        // init answer list
        answerMatrix = new ArrayList<>(totalCard);
        for (Card c : cardList) {
            answerMatrix.add(c.getAnswer());
        }
        Collections.shuffle(answerMatrix);

        showCard();

    }

    public void showResult() {
        choiceVBox.getChildren().clear();
        questionField.setText("Congratulation, You have finished the test.\nYou have " + correctAnswer + "/" + totalCard + " correct answers"
                + "\nYour score is: " + score + "\nYou can go back to home page now.");
        if (isTestMode) {
            TestScore ts = new TestScore();
            ts.setStudent(FlashcardSoEz.curUser);
            ts.setTest(test);
            ts.setScore(score);
            new TestScoreDAO().add(ts);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scoreLabel.setText("Score: " + score);

    }

    public int getRandomIndex(int bound) {
        Random random = new Random();
        //get random number from 0 to bound then + 1
        int randomNumber = random.nextInt(bound);
        return randomNumber;
    }

    public void assignAnswers() {
        choice1.setText(choices.get(0));
        choice2.setText(choices.get(1));
        choice3.setText(choices.get(2));
        choice4.setText(choices.get(3));
    }

}
