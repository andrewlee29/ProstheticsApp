package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HistoryDetailActivity extends AppCompatActivity {

    private BluetoothConnection blueConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        blueConnection=BluetoothConnection.getInstance(this);
    }
}