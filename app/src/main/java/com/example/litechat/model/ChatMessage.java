package com.example.litechat.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class ChatMessage {
    String senderId;
    String text;
    String time;
    boolean isMine;

    public ChatMessage(String senderId, String text, String time) {
        this.senderId = senderId;
        this.text = text;
        this.time = time;

    }

    public ChatMessage() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMine() {
        if(senderId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return true;
        }
        return false;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String convertTime(){

        // Parse the time string to Instant
        Instant instant = Instant.ofEpochMilli(Long.parseLong(getTime()));

        // Convert Instant to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Format LocalDateTime to a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }
}
