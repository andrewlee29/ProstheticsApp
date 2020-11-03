package com.example.prostheticsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class SettingActivity extends AppCompatActivity implements OnConnectedAction {

    ListView mDeviceList;
    Button mscanbtn, mbluetoothbtn;
    TextView mstatustv;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Set<BluetoothDevice> mPairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    private BluetoothConnection blueConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mDeviceList = findViewById(R.id.deviceLv);
        mbluetoothbtn = findViewById(R.id.bluetoothBtn);
        mscanbtn = findViewById(R.id.scanBtn);
        mstatustv = findViewById(R.id.statusTv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        blueConnection=BluetoothConnection.getInstance(this);

        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }

        if(blueConnection.isConnected())
        {
            mstatustv.setText("Connected");
        }
        else{
            mstatustv.setText("No Connection");
        }
        // bluetooth on and off
        mbluetoothbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(!mBluetoothAdapter.isEnabled()){
                    //intent to on bluetooth
                    Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnBTon, 1);
                    showToast("Turning on Bluetooth...");
                    mbluetoothbtn.setText("off");
                }
                else {
                    mBluetoothAdapter.disable();
                    showToast("Turning off Bluetooth...");
                    mbluetoothbtn.setText("on");
                }
            }
        });
        // scan button press
        mscanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBluetoothAdapter.isEnabled()){
                    showToast("Please turn on bluetooth!!");
                }
                else {
                    showToast("Scanning paired device...");
                    listPairedDevices();
                }
            }
        });
    }
    private void listPairedDevices() {
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (mPairedDevices.size()>0)
        {
            for(BluetoothDevice bt : mPairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        mDeviceList.setAdapter(adapter);
        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                // Get the device MAC address, the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                //Log.d("Test", "here is the address"+info);
//              //build connection
                if(blueConnection.isConnected())
                {
                    //disconnect
                    blueConnection.endConnection();
                    mstatustv.setText("No Connection");
                }
                else{
                    blueConnection.setDeviceAddress(address);
                    blueConnection.buildConnection(SettingActivity.this);
                    //mstatustv.setText("Connected");
                }
            }
        });
    }


    //toast message function
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(String msg) {
        mstatustv.setText(msg);
    }
}