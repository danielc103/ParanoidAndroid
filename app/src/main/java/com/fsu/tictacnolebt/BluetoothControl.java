package com.fsu.tictacnolebt;

import android.bluetooth.BluetoothSocket;

/**
 * Created by DanielCarroll on 7/5/2015.
 */
public class BluetoothControl {

    private static BluetoothSocket mSocket;

    //TODO: Once connected with streams, we can manipulate data here?

    public static void setBtSocket(BluetoothSocket _socket){
        mSocket = _socket;
    }

}
