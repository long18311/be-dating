package com.example.datingbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "information_option")
@NoArgsConstructor
@AllArgsConstructor
public class InformationOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "information_field_id")
    @JsonIgnoreProperties("informationOptions")
    private InformationField informationField;
    @Column(name = "option_value")
    private String option;
}