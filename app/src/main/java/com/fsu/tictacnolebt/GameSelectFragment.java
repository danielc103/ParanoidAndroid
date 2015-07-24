package com.fsu.tictacnolebt;

import android.app.Activity;
import android.app.ProgressDialog;
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

            /*
            // TODO: Bluetooth listen-for-client implementation.
            Toast.makeText(getActivity(), "TODO: Implement Bluetooth Listen-For-Client",
                    Toast.LENGTH_SHORT).show();
            */

            // Use AcceptThread here
            // [SOURCE] http://stackoverflow.com/questions/9212574/calling-activity-methods-from-fragment
            btConnActivity.startAcceptThread();

            // TODO: Utilize socket obtained in acceptThread.

        }
        else if (v == mBtnPlayAsClient) {
            /*
            // TODO: Bluetooth scan-for-servers implementation.
            Toast.makeText(getActivity(), "TODO: Implement Bluetooth Scan",
                    Toast.LENGTH_SHORT).show();
            */
            btConnActivity.scanForDevices();

        }
        else {
            // TODO: Single phone Tic-Tac-Nole goes here.
            Toast.makeText(getActivity(), "TODO: Implement Single Phone Tic-Tac-Nole",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
