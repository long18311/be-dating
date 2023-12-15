package com.example.datingbe.dto;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.User;

public class ImagesDto {
    private Long id;
    private String url;
    private User user;

    public ImagesDto() {
    }
    public ImagesDto(Images images) {
        this.id = images.getId();
        this.url = images.getUrl();
        this.user = images.getUser();
    }

}
