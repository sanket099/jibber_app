package com.san.jibberapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_table")//room ; to create sqlite objects //one table
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private int id;// for data objects

    private String message;
    private boolean isBotMessage;
    private String date;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBotMessage() {
        return isBotMessage;
    }

    public void setBotMessage(boolean botMessage) {
        isBotMessage = botMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

