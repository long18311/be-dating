package com.example.datingbe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "information_field")
@NoArgsConstructor
@AllArgsConstructor
public class InformationField implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InformationType informationType;

    private String name; // Ví dụ: "Cung Hoàng Đạo"
    private String decsription;// chứa câu hỏi :"cung hoàng đạo của bạn là gì"
    private boolean multiSelect; // true nếu cho phép chọn nhiều, false nếu chỉ chọn một
//    @JsonIgnore
    @OneToMany(mappedBy = "informationField", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InformationOption> informationOptions = new ArrayList<>();
}