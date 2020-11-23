package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;

import Bluetooth.BluetoothConnection;
import JsonReader.Historydetaildata;
import JsonReader.Historylistdata;

public class HistoryDetailActivity extends AppCompatActivity {

    private String onClickDate;
    private String messagefromserver;
    private BluetoothConnection blueConnection;
    private int section= 1;
    private int sectioncounter = 0;
    LineChart emgLineChart1;
    TextView humdataTv,tempdatatv, sectiontv;
    Button nextbtn,previousbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        humdataTv = findViewById(R.id.humdataTv);
        tempdatatv = findViewById(R.id.tempdataTv);
        sectiontv = findViewById(R.id.SectonTv);
        nextbtn = findViewById(R.id.nextbtn);
        previousbtn = findViewById(R.id.previousbtn);
        //sectiontv set to 1
        sectiontv.setText("Section: "+ String.valueOf(section));
        // for building charts:
        emgLineChart1 = findViewById(R.id.emgChart1);
        ArrayList<Entry> datavals = new ArrayList<Entry>();

        // Retrieve the clicked date from the HistoryActivity
        Intent newint = getIntent();
        onClickDate =  newint.getStringExtra(HistoryActivity.EXTRA_Date);
        blueConnection=BluetoothConnection.getInstance(this);

        //send request for current data: getHisDate:YYYY/MM/DD:1
        blueConnection.testsendmessage("getHisDate:"+(String)onClickDate+":"+String.valueOf(section));

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    Historydetaildata historydetaildata = getserverdata();
                    humdataTv.setText(historydetaildata.gethumid());
                    tempdatatv.setText(historydetaildata.getTemp());
                    updatechart(section,historydetaildata,datavals);
                    //if click next btn
                    nextbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sectioncounter <historydetaildata.getSection().size()-1)
                            {
                                sectioncounter++;
                                section=historydetaildata.getSection().get(sectioncounter);
                                blueConnection.testsendmessage("getHisDate:"+(String)onClickDate+":"+String.valueOf(section));
                                sectiontv.setText("Section: "+ String.valueOf(section));
                                Historydetaildata historydetaildata = getserverdata();
                                updatechart(section,historydetaildata,datavals);
                            }
                            else
                            {
                                showToast("This is the last section");
                            }
                        }
                    });
                    //if click previous btn
                    previousbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sectioncounter >0)
                            {
                                sectioncounter--;
                                section=historydetaildata.getSection().get(sectioncounter);
                            blueConnection.testsendmessage("getHisDate:"+(String)onClickDate+":"+String.valueOf(section));
                            sectiontv.setText("Section: "+ String.valueOf(section));
                            Historydetaildata historydetaildata = getserverdata();
                            updatechart(section,historydetaildata,datavals);
                            }
                            else
                            {
                                showToast("This is the first section");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                messagefromserver = blueConnection.testrecievemessage();
//                while (messagefromserver == null) {
//                    messagefromserver = blueConnection.testrecievemessage();
//                }
//                blueConnection.resetRecieveStatus();
//
//                Gson gson = new Gson();
//                Historydetaildata historydetaildata = gson.fromJson(messagefromserver, Historydetaildata.class);
//                humdataTv.setText(historydetaildata.gethumid());
//                tempdatatv.setText(historydetaildata.getTemp());
//                LineData data = updatechart(section,historydetaildata,datavals);
//                emgLineChart1.setData(data);
//                emgLineChart1.invalidate();
//
//                //if click next btn
//                nextbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        section++;
//                        sectiontv.setText("Section: "+ String.valueOf(section));
//                        LineData data = updatechart(section,historydetaildata,datavals);
//                        emgLineChart1.setData(data);
//                        emgLineChart1.invalidate();
//                    }
//                });
//                //if click previous btn
//                previousbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        section--;
//                        sectiontv.setText("Section: "+ String.valueOf(section));
//                        LineData data = updatechart(section,historydetaildata,datavals);
//                        emgLineChart1.setData(data);
//                        emgLineChart1.invalidate();
//                    }
//                });
//
//            }
//        });
    }

    private Historydetaildata getserverdata(){
        Log.d("ZZZZZZZZZZ","waiting for message");
        messagefromserver = blueConnection.testrecievemessage();
        while (messagefromserver == null) {
            messagefromserver = blueConnection.testrecievemessage();
        }
        Log.d("ZZZZZZZZZZ","message recieved");
        blueConnection.resetRecieveStatus();

        Gson gson = new Gson();
        Historydetaildata historydetaildata = gson.fromJson(messagefromserver, Historydetaildata.class);
        return historydetaildata;
    }
    private void updatechart(int section,Historydetaildata historydetaildata, ArrayList<Entry> datavals){
        datavals.clear();
        for(int i =0;i<historydetaildata.getemgdata1().size();i++)
        {
            String time = historydetaildata.getemgdata1().get(i).gettime();
            String mV = historydetaildata.getemgdata1().get(i).getmV();
            datavals.add(new Entry(Integer.parseInt(time),Integer.parseInt(mV)));
        }

        //print Chart data
        LineDataSet lineDataSet1 = new LineDataSet(datavals,"Emg data 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data = new LineData(dataSets);
        emgLineChart1.setData(data);
        emgLineChart1.invalidate();
    }
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

