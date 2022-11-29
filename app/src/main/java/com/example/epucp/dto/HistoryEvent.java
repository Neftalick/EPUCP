package com.example.epucp.dto;

public class HistoryEvent {
    private String userKey;
    private String eventKey;

    public HistoryEvent() {
    }

    public HistoryEvent(String userKey, String eventKey) {
        this.userKey = userKey;
        this.eventKey = eventKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
