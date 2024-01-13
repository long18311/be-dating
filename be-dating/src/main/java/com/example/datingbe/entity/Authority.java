package com.example.datingbe.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Getter
@Setter
@ToString
public class Authority {

    @Id
    private String name;

    public Authority() {
    }
    public Authority(String name) {
        this.name = name;
    }
}