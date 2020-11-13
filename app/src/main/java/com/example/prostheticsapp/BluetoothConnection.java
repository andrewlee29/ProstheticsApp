package com.example.prostheticsapp;

import android.content.Context;

public class BluetoothConnection {
    public String mDeviceAddress = null;
    private static BluetoothConnection connectionObj;
    protected ConnectionTask mBluetoothConnection;
    private Context context;
    private OnConnectedAction onConnectedAction;
    private boolean fullmsgreceived = false;
    public BluetoothConnection(Context context) {
        this.context = context;
        this.onConnectedAction = onConnectedAction;
    }

    public static BluetoothConnection getInstance(Context context){
        if(connectionObj == null) {
            connectionObj = new BluetoothConnection(context);
        }
        return connectionObj;
    }

    //getDeviceAddress from the activity
    public void setDeviceAddress(String x){
        mDeviceAddress = x;
    }

    //Build connection
    public void buildConnection(OnConnectedAction onConnectedAction){
        this.onConnectedAction = onConnectedAction;
        //create mBluetoothConnection (new ConnectTask)
        mBluetoothConnection = new ConnectionTask(context, mDeviceAddress, onConnectedAction);
        mBluetoothConnection.execute();
    }

    // check if there is a connection built
    public boolean isConnected(){
        return mBluetoothConnection.returnStatus();
    }

    //end connection
    public void endConnection(){
        mBluetoothConnection.disconnect();
    }




    //--------------------------------------------------------------------------
    //data transfer testing
//    public void testsendmessage()
//    {
//        mBluetoothConnection.write((byte)'a');
//        mBluetoothConnection.write((byte)'u');
//        mBluetoothConnection.write((byte)'.');
//    }
    public void testsendmessage(String message)
    {
        for(int i=0; i<message.length();i++)
        {
            char c = message.charAt(i);
            mBluetoothConnection.write((byte)c);
        }
        mBluetoothConnection.write((byte)'.');
    }
    public String testrecievemessage()
    {
        String mMessageFromServer = "";
        while (mBluetoothConnection.available() > 0) {

            char c = (char)mBluetoothConnection.read();

            if (c == '@') {

                if (mMessageFromServer.length() > 0) {

                    return mMessageFromServer;
                }
            }
            else {
                mMessageFromServer += c;
            }
        }
        return null;
    }
}
