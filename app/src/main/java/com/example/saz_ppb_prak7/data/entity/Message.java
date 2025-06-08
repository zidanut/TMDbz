package com.example.saz_ppb_prak7.data.entity;

public class Message {
    private String text;
    private long timestamp;
    private String senderName;

    public Message() {}
    public Message(String text, long timestamp, String senderName) {
        this.text = text;
        this.timestamp = timestamp;
        this.senderName = senderName;
    }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}


