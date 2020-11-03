package com.example.prostheticsapp;

public class HistoryItem {
    private String date;
    private int temp;
    private int humid;

    public HistoryItem(String date, int temp, int humid)
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }
}