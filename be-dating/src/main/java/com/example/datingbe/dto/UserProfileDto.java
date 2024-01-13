package com.example.datingbe.dto;

import com.example.datingbe.entity.Authority;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.InformationOption;
import com.example.datingbe.entity.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
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
    private String verify;
    private float height;
    private float weight;
    private double latitude;
    private double longitude;
    private Boolean security;
    private Set<Authority> authorities;
    private Set<InformationOption> informationOptions;
    private List<Images> images = new ArrayList<>();
    private Long numberFriend;
    public UserProfileDto(User user,Long numberFriend){
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
        this.informationOptions = user.getInformationOptions();
        this.about = user.getAbout();
        this.images = user.getImages();
        this.sex = user.getSex();
        this.maritalstatus = user.getMaritalstatus();
        this.birthday = user.getBirthday();
        this.cover = user.getCover();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.security = user.getSecurity();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
        this.verify = user.getVerify();
        this.numberFriend = numberFriend;
    }
}
