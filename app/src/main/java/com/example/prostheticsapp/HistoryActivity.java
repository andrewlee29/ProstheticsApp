package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Bluetooth.BluetoothConnection;
import JsonReader.Historylistdata;
import JsonReader.Summarydata;

public class HistoryActivity extends AppCompatActivity {

    private BluetoothConnection blueConnection;
    private String messagefromserver;

    public static String EXTRA_Date = "request_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        blueConnection=BluetoothConnection.getInstance(this);

        //send request for current data"
        blueConnection.testsendmessage("requestHis");

        //List create
        ListView mListView = (ListView) findViewById(R.id.datelistLv);
        ArrayList<HistoryItem> DateList = new ArrayList<>();
        DateListAdapter adapter = new DateListAdapter(this, R.layout.dateadpter_view_layout,DateList);
        mListView.setAdapter(adapter);

        //Item OnClick
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem clickitem = (HistoryItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
                intent.putExtra(EXTRA_Date,clickitem.getDate());
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

                Gson gson = new Gson();
                Historylistdata historylistdata = gson.fromJson(messagefromserver, Historylistdata.class);
                for(int i =0; i<historylistdata.gethistorydate().size();i++)
                {
                    String date = historylistdata.gethistorydate().get(i).getDate();
                    String temp = historylistdata.gethistorydate().get(i).getTemp();
                    String humid = historylistdata.gethistorydate().get(i).getHumid();
                    HistoryItem sample1 = new HistoryItem(date,temp,humid);
                    DateList.add(sample1);
                }
                mListView.setAdapter(adapter);

            }
        });

    }
}