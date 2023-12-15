package com.example.datingbe.dto;

import com.example.datingbe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchPercent {
    private UserProfileDto user;
    private int percent;

    public UserSearchPercent(User user,Long numberFriend, int percent) {
        this.user = new UserProfileDto(user,numberFriend);
        this.percent = percent;
    }
}