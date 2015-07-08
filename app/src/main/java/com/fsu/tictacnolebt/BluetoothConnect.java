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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Daniel Carroll on 7/6/2015.
 */
public class BluetoothConnect extends MainActivity {

    //UUID for application (probably not doing this correctly)
    private static final UUID MY_UUID = UUID.randomUUID();


    private static final String NAME = "ParanoidAndroidBT";
    private static final int REQUEST_ENABLE_BT = 2;
    //Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter;


    /*
    Overrides onCreate method for setting up bluetooth when class called
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


    /*
    Checks paired bluetooth device and then created array adapter so list can be displayed
    TODO: Create bt_paired_list view so list can be displayed
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


    /*
    Accept Thread Class - gets incoming connection then hands socket off to Bluetooth Control
     */
    private class AcceptThread extends Thread{

        private final BluetoothServerSocket mmServerSocket;
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
        //TODO: Figure out how to pass the socket for use in BluetoothControl
        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();

                } catch (IOException e) {
                    break;
                }
                //If connection was accepted
                if (socket != null) {

                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }

    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){

            BluetoothSocket tmp = null;
            mmDevice = device;

            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }
    }



}
