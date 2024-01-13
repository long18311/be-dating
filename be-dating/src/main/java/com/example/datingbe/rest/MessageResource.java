package com.example.datingbe.rest;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datingbe.dto.UserMeDto;
import com.example.datingbe.entity.Message;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.MessageRepository;
import com.example.datingbe.repository.UserRepository;
import com.example.datingbe.service.Contains;
import com.example.datingbe.service.MessageService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageResource {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam("meet") String meet)  {

        return ResponseEntity.ok(Contains.encrypt(meet));
    }
    @GetMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam("meet") String meet)  {
        return ResponseEntity.ok(Contains.decrypt(meet));
    }
    @PostMapping("meet/{userId}")
    public ResponseEntity<String> meet(@PathVariable long userId, @RequestBody MessagePayload payload)  {

        User sen = userService.getUserWithAuthority();
        return ResponseEntity.ok(messageService.meet(sen,userId,payload.getMessage()));
    }
    @PostMapping("joinmeet/{userId}")
    public ResponseEntity<String> joinmeet(@PathVariable long userId, @RequestBody MessagePayload payload)  {

        User sen = userService.getUserWithAuthority();
        return ResponseEntity.ok(messageService.joinmeet(sen,userId,payload.getMessage()));
    }
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadAudio(@PathVariable long userId,@RequestParam("audioFile") MultipartFile audioFile) {
        User sen = userService.getUserWithAuthority();
        try {
            // Chuyển file ghi âm thành một map với các giá trị cần thiết
            Map uploadResult = cloudinary.uploader().upload(audioFile.getBytes(), ObjectUtils.asMap(
                    "resource_type", "video","folder", "voice" // Dùng "video" làm resource_type cho cả âm thanh và video
            ));

            // Lấy URL từ kết quả upload và chuyển đổi nó thành chuỗi
            String audioUrl = (String) uploadResult.get("url");
            System.out.println("Audio URL: " + audioUrl); // Sysout để kiểm tra

            // Thay vì lưu vào cơ sở dữ liệu, chúng ta sẽ trả về URL cho client
            return ResponseEntity.ok(messageService.sendMessage(sen,userId,audioUrl).getContent());
        } catch (IOException e) {
            // Xử lý ngoại lệ nếu việc upload không thành công
            System.out.println("Could not upload audio file: " + e.getMessage());
            return ResponseEntity.status(500).body("Could not upload audio file: " + e.getMessage());
        }
    }
    @GetMapping("/user")
    public List<UserMeDto> getUserMessage(){
        User sen = userService.getUserWithAuthority();
        return messageService.getUserMessage(sen);
    }
    @GetMapping("/numberNotification")
    public ResponseEntity<Long> numberNotification(){
        User sen = userService.getUserWithAuthority();
        return ResponseEntity.ok(messageRepository.countUnreadConversations(sen.getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Message>> getMessage(@PathVariable long userId){
        User sen = userService.getUserWithAuthority();
        User rec = userRepository.getOne(userId);
        return ResponseEntity.ok(messageService.getMessage(sen,rec));
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<String> recall(@PathVariable long id){
        User sen = userService.getUserWithAuthority();
        if(messageService.recall(sen,id)>=1){
            return ResponseEntity.ok("Thành công");
        }
        return ResponseEntity.badRequest().body("thất bại");

    }
    @PostMapping("/{userId}")
    public ResponseEntity<Message> sendMessage(@PathVariable long userId, @RequestBody MessagePayload payload)  {

        User sen = userService.getUserWithAuthority();
        return ResponseEntity.ok(messageService.sendMessage(sen,userId,payload.getMessage()));
    }
    @PostMapping("/upload-files/{userId}")
    public ResponseEntity<?> uploadFile(@PathVariable long userId,@RequestParam("file") MultipartFile file) {
        String folderPath = file.getContentType().startsWith("image") ? "images" : "videos";
        System.out.println(folderPath+"/"+file.getContentType());
        User sen = userService.getUserWithAuthority();
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", folderPath
            ));
            // In URL để kiểm tra
            String url = (String) uploadResult.get("url");
            System.out.println("Uploaded file URL: " + url);

            return ResponseEntity.ok(messageService.sendMessage(sen,userId,url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file: " + e.getMessage());
        }
    }

public static class MessagePayload {
    private String message;

    public String getMessage() {
        return  message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
}

