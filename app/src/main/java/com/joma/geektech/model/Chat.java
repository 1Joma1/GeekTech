package com.joma.geektech.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Chat {
    private String id;
    private String from;
    private String fromImage;
    private String fromId;
    private String message;
    @ServerTimestamp
    private Timestamp time;

    public Chat() {
    }

    public Chat(String from, String message, String fromImage, String fromId) {
        this.from = from;
        this.message = message;
        this.fromImage = fromImage;
        this.fromId = fromId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getFromImage() {
        return fromImage;
    }

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
