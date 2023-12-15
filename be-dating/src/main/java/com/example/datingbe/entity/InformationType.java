package com.example.datingbe.entity;

public enum InformationType {
    hobby("hobby"),
    passion("passion"),//phong các sống
    profession("profession"),
    basic_information("basic_information");
    private final String description;

    InformationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
