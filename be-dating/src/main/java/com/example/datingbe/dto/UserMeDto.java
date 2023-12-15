package com.example.datingbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor


public class UserMeDto {
    private Long id;
    private String username;
    private String content;
    private Date time;
    private String img;
    private Boolean isOn = false;
    private long numberUnread;


    public UserMeDto(Long id, String username, String img, String content, Date time, long numberUnread) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.time = time;
        this.img = img;
        this.numberUnread = numberUnread;
        if(img == null || img.length() == 0){
            this.img = "https://static.turbosquid.com/Preview/001292/481/WV/_D.jpg";
        }
    }
}
