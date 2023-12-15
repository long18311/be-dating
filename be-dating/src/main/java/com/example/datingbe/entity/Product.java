package com.example.datingbe.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String name;
    private Double price;
    private int active;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Images_product> imagesProducts = new ArrayList<>();

    @Override
    public String toString() {
        return "Product{" +
                        "id=" + id +
                        ", photo='" + photo + '\'' +
                        ", name='" + name + '\'' +
                        ", price=" + price +
                        ", active=" + active +
                            ", imagesProducts=" + imagesProducts +
                            '}';
    }
}
