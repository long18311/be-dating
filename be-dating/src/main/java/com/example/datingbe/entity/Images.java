package com.example.datingbe.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Table(name="images")
@Entity
@Data
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("images")
    private User user;
    public Images(String url) {
     this.url = url;

    }
    public Images() {
    }

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

}
