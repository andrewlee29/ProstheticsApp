package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CurrentDataActivity extends AppCompatActivity {

    TextView mdevEnviromentDataTv,mbodypartDetectedDataTv,mmuscleStatusDataTv;
    Button mmorebtn;
    private BluetoothConnection blueConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data);

        mdevEnviromentDataTv =findViewById(R.id.tempdataTv);
        mbodypartDetectedDataTv=findViewById(R.id.humiddataTv);
        mmuscleStatusDataTv=findViewById(R.id.muscleStatusDataTv);
        mmorebtn = findViewById(R.id.morebtn);
        blueConnection=BluetoothConnection.getInstance(this);

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
//        blueConnection.testsendmessage();
//
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