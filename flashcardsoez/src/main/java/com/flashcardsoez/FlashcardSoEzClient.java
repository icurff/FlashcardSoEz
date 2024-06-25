package com.flashcardsoez;

import com.flashcardsoez.controller.MainChatController;
import com.flashcardsoez.utils.SecureUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import javafx.application.Platform;

public class FlashcardSoEzClient {

    private Socket socket;
    private BufferedReader dis;
    private PrintWriter dos;
    private MainChatController mainChatController;

    public FlashcardSoEzClient(Socket socket, MainChatController mainChatController) {
        try {
            this.socket = socket;
            this.mainChatController = mainChatController;
            this.dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.dos = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            closeAll(socket, dis, dos);
        }
    }

    public Key key = SecureUtil.getKey();

    public void sendMessage(String messageFromThisClient) {
        String message = SecureUtil.encrypt(FlashcardSoEz.curUser.getUsername() + ": " + messageFromThisClient, key);
        dos.println(message);
    }

    public void listenForMessages() {
        new Thread(() -> {
            String messageFromGroupChat;
            try {
                while ((messageFromGroupChat = dis.readLine()) != null) {
                    if (isFromSystem(messageFromGroupChat)) {
                        // Message from system, no need to decrypt
                        final String message = messageFromGroupChat;
                        Platform.runLater(() -> mainChatController.loadMessToVBox(message, false));
                    } else {
                        // Message is encrypted, decrypt it
                        String decryptedMessage = SecureUtil.decrypt(messageFromGroupChat, key);
                        Platform.runLater(() -> mainChatController.loadMessToVBox(decryptedMessage, false));
                    }
                }
            } catch (IOException e) {
                closeAll(socket, dis, dos);
            }
        }).start();
    }

    private boolean isFromSystem(String message) {
        return message.startsWith("System:");
    }

    public void closeAll(Socket socket, BufferedReader dis, PrintWriter dos) {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
