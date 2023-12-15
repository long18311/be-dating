package com.example.datingbe.dto;

import lombok.Data;

@Data
public class MessagePayload {

    private Long senderId;
    private Long receiverId;
    private String content;

}
