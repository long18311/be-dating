package com.example.datingbe.dto;

import com.example.datingbe.entity.Authority;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Integer actived;
    private Timestamp created_date;
    private String city;
    private String ward;
    private String district;
    private String avatar;
    private String cover;
    private String nickname;
    private String lastname;
    private String firstname;
    private Date birthday;
    private String about;
    private String sex;
    private String maritalstatus;
    private double latitude;
    private double longitude;
    private Set<Authority> authorities;
    private Boolean security;

    private List<Images> images;

    public UserDto(User user){
        this.actived = user.getActived();
        this.ward = user.getWard();
        this.nickname = user.getNickname();
        this.district = user.getDistrict();
        this.city = user.getCity();
        this.authorities = user.getAuthorities();
        this.avatar = user.getAvatar();
        this.phone = user.getPhone();
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.created_date = user.getCreated_date();
        this.about = user.getAbout();
        this.images = user.getImages();
        this.sex = user.getSex();
        this.maritalstatus = user.getMaritalstatus();
        this.birthday = user.getBirthday();
        this.cover = user.getCover();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.security = user.getSecurity();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
    }


}
