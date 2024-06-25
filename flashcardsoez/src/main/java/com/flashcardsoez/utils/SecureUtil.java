package com.flashcardsoez.utils;

import com.flashcardsoez.model.User;
import java.security.Key;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public class SecureUtil {

    Random random = new Random();

    public static String getSalt() {
        return BCrypt.gensalt();
    }

    public static Boolean isEqual(String plaintext, String hashedtext) {
        return BCrypt.checkpw(plaintext, hashedtext);
    }

    public String getRandomOtp() {
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, getSalt());
    }

    public static void sendOtp(User u) {
        String content = "Your Otp is : " + u.getOtp().getCode();
        MailFactory.sendEmail(u.getEmail(), "Your OTP", content);
    }

    public static Key getKey() {
        String encodedKey = "0ApDcfTG1jA83m9kUgk5UMe5KrkA6BebxGc69KToFNc=";
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }

    public static String encrypt(String plainText, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] message = plainText.getBytes("UTF-8");
            byte[] ciphertext = cipher.doFinal(message);

            // Encode the encrypted bytes to Base64
            return Base64.getEncoder().encodeToString(ciphertext);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "System: Something went wrong";
    }

    public static String decrypt(String cipherText, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decode Base64 string to byte array
            byte[] ciphertext = Base64.getDecoder().decode(cipherText);
            byte[] plaintext = cipher.doFinal(ciphertext);

            return new String(plaintext, "UTF-8");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "System: Something went wrong";
    }

}
