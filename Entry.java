package com.example.android.journalapp.model;

import android.annotation.TargetApi;
import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Entry {

    int id;
    String subject;
    String message;
    String dateEntered;

    public Entry() {
    }

    public Entry(int id, String subject, String message, String dateEntered) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.dateEntered = dateEntered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void setDateEntered(String dateEntered) {
        dateEntered = LocalDateTime.now().toString();
        this.dateEntered = dateEntered;
    }
}
