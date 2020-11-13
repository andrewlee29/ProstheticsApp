package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class HistoryDetailActivity extends AppCompatActivity {

    private String onClickDate;
    private String messagefromserver;
    private BluetoothConnection blueConnection;
    LineChart emgLineChart1;
    TextView humdataTv,tempdatatv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        humdataTv = findViewById(R.id.humdataTv);
        tempdatatv = findViewById(R.id.tempdataTv);
        // for building charts:
        emgLineChart1 = findViewById(R.id.emgChart1);
        ArrayList<Entry> datavals = new ArrayList<Entry>();

        // Retrieve the clicked date from the HistoryActivity
        Intent newint = getIntent();
        onClickDate =  newint.getStringExtra(HistoryActivity.EXTRA_Date);
        blueConnection=BluetoothConnection.getInstance(this);

        //send request for current data"
        blueConnection.testsendmessage("getHisDate:"+(String)onClickDate);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                messagefromserver = blueConnection.testrecievemessage();
                String[] arrOfStr = messagefromserver.split("#", -1);
                humdataTv.setText(arrOfStr[0]);
                tempdatatv.setText(arrOfStr[1]);

                for(int i =2;i<arrOfStr.length-1;i+=2)
                {
                    datavals.add(new Entry(Integer.parseInt(arrOfStr[i]),Integer.parseInt(arrOfStr[i+1])));
                }
                //print Chart data
                Log.d("Test", "data"+arrOfStr);
                LineDataSet lineDataSet1 = new LineDataSet(datavals,"Emg data 1");
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet1);

                LineData data = new LineData(dataSets);
                emgLineChart1.setData(data);
                emgLineChart1.invalidate();
            }
        }, 2000);
    }
}

