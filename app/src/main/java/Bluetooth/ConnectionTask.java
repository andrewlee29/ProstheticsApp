package Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class ConnectionTask extends AsyncTask<Void, Void, Void> {
    private static boolean isConnected = false;
    private boolean mConnected = true;
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket mBluetoothSocket = null;
    private Context mCurrentContext = null;
    private String mAddress = null;
    private OnConnectedAction onConnectedAction;

    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ConnectionTask(Context context, String address, OnConnectedAction onConnectedAction) {
        mCurrentContext = context;
        mAddress =  address;
        this.onConnectedAction = onConnectedAction;
    }

    @Override
    protected void onPreExecute()     {
        mProgressDialog = ProgressDialog.show(mCurrentContext, "Connecting...", "Please wait!!!");  //show a progress dialog
    }

    @Override
    protected Void doInBackground(Void... devices) { //while the progress dialog is shown, the connection is done in background

        try {
            if (mBluetoothSocket == null || !mConnected) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);//connects to the device's address and checks if it's available
                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBluetoothSocket.connect();//start connection
            }
        }
        catch (IOException e) {
            mConnected = false;//if the try failed, you can check the exception here
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) { //after the doInBackground, it checks if everything went fine

        super.onPostExecute(result);

        if (!mConnected){
            message("Connection Failed. Is it a SPP Bluetooth running a server? Try again.");
        }
        else {
            message("Connected.");
            isConnected = true;
            //Change the textview in activity
            if (onConnectedAction != null)
                onConnectedAction.onConnected("Status: Connected");
        }
        mProgressDialog.dismiss();
    }

    public void write(byte b) {

        try {
            mBluetoothSocket.getOutputStream().write((int)b);
        }
        catch (IOException e) {
        }
    }

//    public int read() {
//
//        int i = -1;
//
//        try {
//            i = mBluetoothSocket.getInputStream().read();
//            Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf((char)i));
//        }
//        catch (IOException e) {
//        }
//
//        return i;
//    }

    public byte[] readBytes() {

        int i = -1;
        byte[] bb = new byte[10];

        try {
            i = mBluetoothSocket.getInputStream().read(bb, 0, 10);
            //Log.d("ZZZZZZZZZZZZZZZZ", Arrays.toString(bb));
        }
        catch (IOException e) {
        }


        return bb;
    }

    public int available() {

        int n = 0;

        try {
            n = mBluetoothSocket.getInputStream().available();
        }
        catch (IOException e) {
        }

        return n;
    }

    public void disconnect() {
        if (mBluetoothSocket!=null) //If the btSocket is busy
        {
            try  {
                mBluetoothSocket.close(); //close connection
            }
            catch (IOException e) {
                message("Error");
            }
        }

        message("Disconnected");
        isConnected = false;

    }
    public static Boolean returnStatus(){
        return isConnected;
    }

    private void message(String s) {
        Toast.makeText(mCurrentContext.getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

}