package com.example.datingbe.config;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Service

public class MessageEncryptionService {
    public String encrypt(String message) {
        byte[] encodedBytes = Base64.getEncoder().encode(message.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public String decrypt(String encryptedMessage) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage.getBytes(StandardCharsets.UTF_8));
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Xử lý ngoại lệ, có thể trả về giá trị mặc định hoặc logging
            e.printStackTrace();
            return null;
        }
    }
}
