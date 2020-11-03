package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

public class CurrentDataDetailActivity extends AppCompatActivity {

    LineChart emgLineChart1,emgLineChart2;
    private BluetoothConnection blueConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data_detail);


        emgLineChart1 = findViewById(R.id.emgChart1);
        blueConnection=BluetoothConnection.getInstance(this);

    }
}