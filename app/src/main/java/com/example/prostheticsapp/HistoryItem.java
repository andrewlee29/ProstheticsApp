package com.example.prostheticsapp;

public class HistoryItem {
    private String date;
    private String temp;
    private String humid;

    public HistoryItem(String date, String temp, String humid)
    {
        this.date = date;
        this.temp = temp;
        this.humid = humid;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumid() {
        return humid;
    }

    public void setHumid(String humid) {
        this.humid = humid;
    }
}