package com.example.datingbe.entity;

public enum FriendStatus {
    pending("pending"),
    accepted("accepted"),
    rejected("rejected");

    private final String status;
    FriendStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
