package com.example.datingbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
@Entity
@Table(name = "user")
@Getter
@Setter
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer actived;
    @Column( columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean security;
    private String activation_key;
    private Timestamp created_date;
    private String city;
    private String ward;
    private String district;
    private String avatar;
    private String cover;
    private String lastname;
    private String firstname;
    private Date  birthday;
    private String about;
    private String sex;
    private String nickname;
    private String maritalstatus;
    private String verify;
    private float height;
    private float weight;
    private double latitude;
    private double longitude;

    @JsonIgnoreProperties(value = {"user"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();


    @JsonIgnoreProperties(value = {"user"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_informationOption",joinColumns = {
            @JoinColumn(name = "user_id",referencedColumnName = "id")
    },inverseJoinColumns = {
            @JoinColumn(name = "informationOption_id", referencedColumnName = "id")
    }
    )
    @BatchSize(size = 20)
    private Set<InformationOption> informationOptions = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Images> images = new ArrayList<>();
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> sentNotfiications = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> receivedNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Like> likes;
}
