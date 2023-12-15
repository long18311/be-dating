package com.example.datingbe.rest;
import com.example.datingbe.config.SecurityUtils;
import com.example.datingbe.dto.MessagePayload;
import com.example.datingbe.entity.User;
import com.example.datingbe.service.MessageService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public String handleMessage(@Payload MessagePayload messagePayload) {
        User usersender = userService.getOne(messagePayload.getSenderId());
        messageService.sendMessage(usersender,messagePayload.getSenderId(),messagePayload.getContent());

        return "";
    }
}
