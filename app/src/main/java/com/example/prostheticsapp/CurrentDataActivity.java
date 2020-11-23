package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import Bluetooth.BluetoothConnection;
import JsonReader.Summarydata;

public class CurrentDataActivity extends AppCompatActivity {

    TextView mdevEnviromentDataTv,dateDataTv,mmuscleStatusDataTv;
    Button mmorebtn;
    private BluetoothConnection blueConnection;
    private String messagefromserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data);

        dateDataTv =findViewById(R.id.tempdataTv);
        mdevEnviromentDataTv=findViewById(R.id.humdataTv);
        mmuscleStatusDataTv=findViewById(R.id.muscleStatusDataTv);
        mmorebtn = findViewById(R.id.morebtn);
        blueConnection=BluetoothConnection.getInstance(this);


        //send request for current data"
        blueConnection.testsendmessage("requestSum");
        //more button on click
        mmorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentDataActivity.this, CurrentDataDetailActivity.class);
                startActivity(intent);
            }
        });
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              messagefromserver = blueConnection.testrecievemessage();
                              while (messagefromserver == null) {
                                  messagefromserver = blueConnection.testrecievemessage();
                              }
                              blueConnection.resetRecieveStatus();
                              // Received data , load json data to display
                              Gson gson = new Gson();
                              try {
                                  Summarydata summarydata = gson.fromJson(messagefromserver, Summarydata.class);
                                  dateDataTv.setText(summarydata.getDate());
                                  mdevEnviromentDataTv.setText(summarydata.getEnvironmentStatus());
                                  mmuscleStatusDataTv.setText(summarydata.getMuscleStatus());

                              } catch (JsonSyntaxException e) {
                                  Log.d("Er","jsondata is wrong");
                                  e.printStackTrace();
                              }

                          }
                      });

    }
}