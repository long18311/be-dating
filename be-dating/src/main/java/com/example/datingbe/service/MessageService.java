package com.example.datingbe.service;


import com.example.datingbe.dto.UserMeDto;
import com.example.datingbe.entity.Message;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.MessageRepository;
import com.example.datingbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    public String meet(User sen, long userId, String message) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/meet",
                sen.getId()+":"+message);
        return "Thành công";
    }
    public String joinmeet(User sen, long userId, String message) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/joinmeet",
                sen.getId()+":"+message);
        return "Thành công";
    }

    public Message sendMessages(User sender, long receiverId, String content) {
        // Tạo và lưu tin nhắn mới
        User receiver = userRepository.getOne(receiverId);
        Message newMessage = new Message(null, sender, receiver, Contains.encrypt(content), 0, new Date());
        newMessage = messageRepository.save(newMessage);

        // Gửi tin nhắn realtime
        messagingTemplate.convertAndSendToUser(
                String.valueOf(receiverId),
                "/queue/messages",
                sender.getId()
        );

        return newMessage;
    }

    public Message sendMessage(User sen,long userId,String message){

        User rec = userRepository.getOne(userId);
        Message thu = new Message(null,sen,rec,Contains.encrypt(message),0,new Date());
        thu =messageRepository.save(thu);
//        System.out.println("vào rồi nè");
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/messages",
                sen.getId()
        );
//        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/message", String.valueOf(sen.getId()));
        return thu;
    }


    public List<Message> getMessage(User sen,User rec){
        messageRepository.updateMessagesStatus(sen,rec);
        List<Message> messages = messageRepository.findByUsers(sen.getId(),rec.getId());
        for (Message message : messages) {
            message.setContent(Contains.decrypt(message.getContent()));
        }
//

        return messages;
    }
    public List<UserMeDto> getUserMessage(User sen){
        List<UserMeDto> users = messageRepository.getUserMessage(sen.getId());
        Set<String> onlineUsernames = simpUserRegistry.getUsers().stream()
                .map(simpUser -> simpUser.getName())
                .collect(Collectors.toSet());

        for (UserMeDto user : users) {
            // Set the user's online status based on whether they are in the onlineUsernames set
            user.setIsOn(onlineUsernames.contains(user.getId()));
            user.setContent(Contains.decrypt(user.getContent()));
        }
        return users;
    }
    @Transactional
    public int recall(User user , long id){
        if(user.getId() != messageRepository.getOne(id).getSender().getId()) {
            return 0;
        }

        return messageRepository.recallMessage(id, Contains.encrypt("tin nhắn bị thu hồi"));
    }

}
