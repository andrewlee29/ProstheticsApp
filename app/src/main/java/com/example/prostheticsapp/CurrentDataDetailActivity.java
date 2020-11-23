package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Bluetooth.BluetoothConnection;
import JsonReader.Historydetaildata;
import JsonReader.Summarydata;

public class CurrentDataDetailActivity extends AppCompatActivity {

    TextView tempdataTv,humiddataTv;
    LineChart emgLineChart1;

    private BluetoothConnection blueConnection;
    private String messagefromserver;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data_detail);

        blueConnection=BluetoothConnection.getInstance(this);
        humiddataTv = findViewById(R.id.humdataTv);
        tempdataTv = findViewById(R.id.tempdataTv);
        // for building charts:
        emgLineChart1 = findViewById(R.id.emgChart1);
        ArrayList<Entry> datavals = new ArrayList<Entry>();
        handler.post(new Runnable() {
            @Override
            public void run() {
                blueConnection.testsendmessage("requestRealtime");
                String[] historydetaildata = getserverdata();
                humiddataTv.setText("/");
                tempdataTv.setText("/");
                updatechart(historydetaildata,datavals);
                handler.postDelayed(this, 200);

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    private String[] getserverdata(){
        Log.d("ZZZZZZZZZZ","waiting for message");
        messagefromserver = blueConnection.testrecievemessage();
        while (messagefromserver == null) {
            messagefromserver = blueConnection.testrecievemessage();
        }
        Log.d("ZZZZZZZZZZ","message recieved");
        blueConnection.resetRecieveStatus();

        String dataarray[]= messagefromserver.split("#");
        return dataarray;
    }
    private void updatechart(String[] historydetaildata, ArrayList<Entry> datavals){
        String time = historydetaildata[0];
        String mV = historydetaildata[1];
        datavals.add(new Entry(Integer.parseInt(time),Integer.parseInt(mV)));
        if(datavals.size() == 20)
        {
            datavals.remove(0);
        }
        //print Chart data
        LineDataSet lineDataSet1 = new LineDataSet(datavals,"Emg data 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data = new LineData(dataSets);
        emgLineChart1.setData(data);
        emgLineChart1.invalidate();
        Log.d("ZZZZZZZZZZ","Chart updated");
    }
}