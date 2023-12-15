package com.example.datingbe.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name="images_product")
@Entity
@Getter
@Setter
public class Images_product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Images_product(String url) {
        this.url = url;
    }
    public Images_product() {
    }
    @Override
    public String toString() {
        return "Images_product{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
