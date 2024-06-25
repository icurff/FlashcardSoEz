package com.flashcardsoez.controller;

import com.flashcardsoez.DAO.CardDAO;
import com.flashcardsoez.DAO.DeckDAO;
import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Deck;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StudyLibScraperController implements Initializable {

    @FXML
    private TextField messageField;
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messVbox;

    public void loadMessToVBox(String text, boolean isInternal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Message.fxml"));
            Parent parent = loader.load();
            MessageController messageController = loader.getController();
            messageController.setData(text, isInternal);

            // Ensure adding the new message is done on the JavaFX Application Thread
            Platform.runLater(() -> {
                messVbox.getChildren().add(parent);

                // Always scroll to the latest message
                scrollPane.applyCss();
                scrollPane.layout();
                scrollPane.setVvalue(scrollPane.getVmax());
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage() {
        String text = messageField.getText();
        if (text != null && !text.trim().isEmpty()) {
            loadMessToVBox(text, true);
            messageField.clear();
            loadMessToVBox("Connecting to " + text + " . Please wait!", false);
            new Thread(() -> crawl(text)).start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMessToVBox("We only accept Flashcard sets from studylib.net like following example URL format : \n"
                + " https://studylib.net/flashcards/set/world-capitals_184973", false);
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
    }

    public void crawl(String urlToCrawl) {
        //  headless browser
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(urlToCrawl);
            Platform.runLater(() -> loadMessToVBox("Connected  !", false));

            // Explicit wait to ensure the page is loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

            // Attempt to find the button within the specified wait period
            WebElement button = null;
            try {
                button = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//button[@class='btn btn-light' and @data-click='loadMoreInList']")));
            } catch (TimeoutException e) {
               
            }

            if (button != null) {
                // Scroll to the button and click
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", button);
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                } catch (Exception e) {
                    Platform.runLater(() -> loadMessToVBox("It will take more time! Keep calm.", false));
                }
            }

            WebElement deckTitle = driver.findElement(By.cssSelector(".fl-pack-header__title"));
            Deck deck = new Deck();
            deck.setTitle(deckTitle.getText());
            deck.setAuthor(FlashcardSoEz.curUser);
            new DeckDAO().add(deck);
            Platform.runLater(() -> loadMessToVBox("Created deck with name " + deckTitle.getText(), false));

            // Wait for data to load
            Thread.sleep(Duration.ofSeconds(4).toMillis());
            Platform.runLater(() -> loadMessToVBox("Crawling", false));

            // Retrieve the list of cards
            List<WebElement> cardList = driver.findElements(By.cssSelector(".fl-pack-plist__item"));
            int totalCard = cardList.size();
            int[] count = {0};
            for (WebElement cardElement : cardList) {
                WebElement question = cardElement.findElement(By.cssSelector(".fl-pack-plist__face .fl-tt-text"));
                WebElement answer = cardElement.findElement(By.cssSelector(".fl-pack-plist__back .fl-tt-text"));


                Card card = new Card();
                card.setQuestion(question.getText());
                card.setAnswer(answer.getText());
                card.setDeck(deck);
                new CardDAO().add(card);
                count[0] += 1;

                Platform.runLater(() -> loadMessToVBox("added " + count[0] + " / " + totalCard + " to deck ", false));
            }

            Platform.runLater(() -> loadMessToVBox("Crawled Successfully", false));

        } catch (Exception e) {
            Platform.runLater(() -> loadMessToVBox("Something wrong! Stop crawling", false));

        } finally {
            driver.quit();
        }
    }

}
