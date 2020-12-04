package Bluetooth;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

public class BluetoothConnection {
    public String mDeviceAddress = null;
    private static BluetoothConnection connectionObj;
    protected ConnectionTask mBluetoothConnection;
    private Context context;
    private OnConnectedAction onConnectedAction;
    public boolean fullmsgreceived = false;
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
        mBluetoothConnection.write((byte)'@');
    }
    public String testrecievemessage()
    {
        String mMessageFromServer = "";
        while (mBluetoothConnection.available() > 0) {

            /*char c = (char)mBluetoothConnection.read();
            if (c == '@') {

                if (mMessageFromServer.length() > 0) {
                    fullmsgreceived = true;
                    return mMessageFromServer;
                }
            }
            else {
                mMessageFromServer += c;
            }*/

            byte[] dd = mBluetoothConnection.readBytes();

            for (int j = 0; j < dd.length; j++) {
                char c = (char)dd[j];
                Log.d("ZZZZZZZZZZZZZZZZ", String.valueOf(c));
                if (c == '@') {
                    if (mMessageFromServer.length() > 0) {
                        fullmsgreceived = true;
                        return mMessageFromServer;
                    }
                }
                else {
                    mMessageFromServer += c;
                }
            }
        }
        return null;
    }
    // check if there is a connection built
    public boolean IsRecieved(){
        return fullmsgreceived;
    }
    public void resetRecieveStatus(){
        fullmsgreceived = false;
    }
}


