package com.fsu.tictacnolebt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fsu.tictacnolebt.BluetoothConnect;

/**
 * Created by Quothmar on 7/7/2015.
 * SOURCE: http://developer.android.com/training/basics/fragments/creating.html
 */
public class GameSelectFragment extends Fragment implements View.OnClickListener {

    Button mBtnPlayAsHost;
    Button mBtnPlayAsClient;
    Button mBtnPlayWithoutBt;
    ProgressDialog mSpinnerDialog;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View gameSelectView = inflater.inflate(R.layout.game_select, container, false);

        // Obtain the buttons on the Game Select menu.
        mBtnPlayAsHost = (Button)gameSelectView.findViewById(R.id.btn_play_as_host);
        mBtnPlayAsClient = (Button)gameSelectView.findViewById(R.id.btn_play_as_client);
        mBtnPlayWithoutBt = (Button)gameSelectView.findViewById(R.id.btn_play_without_bt);

        // Use the fragment as the click listener (see 'onClick' below).
        // SOURCE: http://stackoverflow.com/questions/1972579/android-how-to-set-a-named-method-in-button-setonclicklistener
        mBtnPlayAsHost.setOnClickListener(this);
        mBtnPlayAsClient.setOnClickListener(this);
        mBtnPlayWithoutBt.setOnClickListener(this);

        return gameSelectView;
    }

    // Respond to Game Select button clicks.
    public void onClick(View v) {

        Fragment nextFragment;   // to be used for next screen (scanned server list, solo TTN, ...)
        BluetoothConnect btConnActivity = (BluetoothConnect)getActivity();

        if (v == mBtnPlayAsHost) {

            // Create a 'Waiting for players...' message while listening for a client.
            mSpinnerDialog = new ProgressDialog(getActivity());
            mSpinnerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerDialog.setTitle("Waiting for players...");
            mSpinnerDialog.show();

            // Begin accepting requests from clients.
            // [SOURCE] http://stackoverflow.com/questions/9212574/calling-activity-methods-from-fragment
            btConnActivity.startAcceptThread();

            // TODO: Utilize socket obtained in acceptThread.
            Toast.makeText(getActivity(), "TODO: Utilize socket obtained in acceptThread", Toast.LENGTH_SHORT).show();

        }
        else if (v == mBtnPlayAsClient) {

            // Look for nearby Bluetooth devices hosting Tic Tac Nole.
            btConnActivity.scanForDevices();

            // TODO: Initiate a game of Tic Tac Nole with accepting host.

        }
        else {
            //Single phone Tic-Tac-Nole against AI

            Intent intent = new Intent(getActivity(), TicTacNole.class);
            startActivity(intent);


        }
    }

}
