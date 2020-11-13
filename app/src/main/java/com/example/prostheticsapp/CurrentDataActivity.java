package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        //recieve message after 1 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                messagefromserver = blueConnection.testrecievemessage();
                String[] arrOfStr = messagefromserver.split("#", 5);
                dateDataTv.setText(arrOfStr[0]);
                mdevEnviromentDataTv.setText(arrOfStr[1]);
                mmuscleStatusDataTv.setText(arrOfStr[2]);

            }
        }, 500);

        //more button on click
        mmorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentDataActivity.this, CurrentDataDetailActivity.class);
                startActivity(intent);
            }
        });






        //--------------------------------------------------------------------------
        //example send and get message from raspberry

//        blueConnection.testsendmessage("au");
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 1s = 1000ms
//                String a = blueConnection.testrecievemessage();
//                Log.d("Test123", "Test2 Message :"+a);
//            }
//        }, 1000);
    }
}