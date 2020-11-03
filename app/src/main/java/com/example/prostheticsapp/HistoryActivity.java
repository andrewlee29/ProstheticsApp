package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private BluetoothConnection blueConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        blueConnection=BluetoothConnection.getInstance(this);

        //List create
        ListView mListView = (ListView) findViewById(R.id.datelistLv);
        ArrayList<HistoryItem> DateList = new ArrayList<>();
        //sample data
        HistoryItem sample1 =  new HistoryItem("2020/9/1",30,4);
        HistoryItem sample2 =  new HistoryItem("2020/9/2",30,4);
        HistoryItem sample3 =  new HistoryItem("2020/9/3",30,4);
        DateList.add(sample1);
        DateList.add(sample2);
        DateList.add(sample3);

        DateListAdapter adapter = new DateListAdapter(this, R.layout.dateadpter_view_layout,DateList);
        mListView.setAdapter(adapter);

        //Item OnClick
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}