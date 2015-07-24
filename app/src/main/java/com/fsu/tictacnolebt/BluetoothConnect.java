package com.fsu.tictacnolebt;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.Debug;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Daniel Carroll on 7/6/2015.
 */
public class BluetoothConnect extends MainActivity {

    //UUID for application (will need a static UUID)
    private static final UUID MY_UUID =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");


    private static final String NAME = "ParanoidAndroidBT";
    private static final int REQUEST_ENABLE_BT = 2;
    //Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter;


    /**
     * Overrides onCreate method for setting up bluetooth when class called
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Check if adapter is present, if not show toast
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(this, "This phone does not support Bluetooth", Toast.LENGTH_SHORT);
        }

        //if present check to see if bluetooth turned on
        if(mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }


   /*
   * We don't have to use the dialog box, it's there if we need it
   * *//*
    Creates an Alert Dialog box that gives an option
     *//*
    public void relationshipDialog(){

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this);

        mAlertDialog.setMessage("Choose Game Type");

        mAlertDialog.setPositiveButton("Host Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //Accpet Thread here
            }
        });

        mAlertDialog.setNegativeButton("Find Player", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //TODO: class or method to find BT device and connect
            }
        });

    }
*/


    /**
     * Checks paired bluetooth device and then created array adapter so list can be displayed
     */
    public void checkPaired(){

        //Array Adapter for paired list to choose from
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, R.layout.bt_paired_list);

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices){
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

    }

    //TODO: Discover devices


    /**
     * Accept Thread Class - gets incoming connection then hands socket off to Bluetooth Control
     */
    private class AcceptThread extends Thread{

        private final BluetoothServerSocket mmServerSocket;

        /**
         * accept thread for incoming bt connection/socket
         */
        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        /**
         *
         */
        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                //If connection was accepted
                if (socket != null) {
                    BluetoothControl.setmSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }

    }

    /**
     *
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        /**
         * Constructor - store UUID in tmp socket
         * @param device
         */
        public ConnectThread(BluetoothDevice device){

            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try{
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        /**
         * ConnectThread run, will try to connect through socket
         */
        public void run(){

            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {

                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();

                //pass socket

            }catch (IOException connectException){
                try {
                    mmSocket.close();
                }catch (IOException closeException){}
                return;
            }
            // Do work to manage the connection (in a separate thread)
            BluetoothControl.setmSocket(mmSocket);
        }

        /**
         *  will cancel an in progress connection and close socket
         */
        public void cancel(){
            try {
                mmSocket.close();
            }catch (IOException e) {}
        }
    }



}
