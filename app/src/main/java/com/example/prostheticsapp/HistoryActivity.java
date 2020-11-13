package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

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
        //recieve message after 1 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                messagefromserver = blueConnection.testrecievemessage();
                String[] arrOfStr = messagefromserver.split("#", -2);
                for(int i =0; i<arrOfStr.length-1;i+=3)
                {
                    //arrOfStr[i]=date   arrOfStr[i+1]=temp  arrOfStr[i+2]=humid
                    HistoryItem sample1 =  new HistoryItem(arrOfStr[i],arrOfStr[i+1],arrOfStr[i+2]);
                    DateList.add(sample1);
                }
                mListView.setAdapter(adapter);
            }
        }, 1000);

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
    }
}