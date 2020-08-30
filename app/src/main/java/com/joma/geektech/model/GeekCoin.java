package com.joma.geektech.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class GeekCoin {
    private String id;
    private String from;
    private String fromName;
    private String to;
    private String toName;
    private String forWhat;
    @ServerTimestamp
    private Timestamp time;

    public GeekCoin() {
    }

    public GeekCoin(String from, String fromName, String to, String toName, String forWhat) {
        this.from = from;
        this.fromName = fromName;
        this.to = to;
        this.toName = toName;
        this.forWhat = forWhat;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getForWhat() {
        return forWhat;
    }

    public void setForWhat(String forWhat) {
        this.forWhat = forWhat;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
