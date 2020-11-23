package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Bluetooth.BluetoothConnection;

public class HomeActivity extends AppCompatActivity {

    Button mCurrentDataBtn, mHistoryDataBtn, mSettingBtn;
    private BluetoothConnection blueConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mCurrentDataBtn =  findViewById(R.id.CurrentDataBtn);
        mHistoryDataBtn = findViewById(R.id.HistoryDataBtn);
        mSettingBtn = findViewById(R.id.SettingBtn);
        blueConnection=BluetoothConnection.getInstance(this);

        //currentDataBtn onclick
        mCurrentDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blueConnection.isConnected())
                {
                    Intent intent = new Intent(HomeActivity.this, CurrentDataActivity.class);
                    startActivity(intent);
                }
                else
                {
                    showToast("Please link the device first!!");
                }
            }
        });
        //HistoryDataBtn onClick
        mHistoryDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blueConnection.isConnected())
                {
                    Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
                else
                {
                    showToast("Please link the device first!!");
                }
            }
        });
        //Setting Btn onClick
        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}