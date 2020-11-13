package com.example.prostheticsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DateListAdapter extends ArrayAdapter<HistoryItem> {
    private static final String TAG = "DateListAdapter";
    private Context mContext;
    int mResource;

    public DateListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HistoryItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String date = getItem(position).getDate();
        String temp = getItem(position).getTemp();
        String humid = getItem(position).getHumid();

        HistoryItem item = new HistoryItem(date,temp,humid);

        LayoutInflater inflater = LayoutInflater.from((mContext));
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDate = convertView.findViewById(R.id.ItemtextView1);
        TextView tvtemp = convertView.findViewById(R.id.ItemtextView2);
        TextView tvhumid = convertView.findViewById(R.id.ItemtextView3);

        tvDate.setText(date);
        tvtemp.setText("temp : "+String.valueOf(temp)+" \u00B0"+"C");
        tvhumid.setText("humid : "+String.valueOf(humid)+"%");

        return convertView;

    }
}
