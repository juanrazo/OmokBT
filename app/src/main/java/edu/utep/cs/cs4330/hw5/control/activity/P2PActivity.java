package edu.utep.cs.cs4330.hw5.control.activity;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.fragment.GameFragment;
import edu.utep.cs.cs4330.hw5.control.fragment.P2PFragment;
import edu.utep.cs.cs4330.hw5.model.Board;
import edu.utep.cs.cs4330.hw5.model.Human;
import edu.utep.cs.cs4330.hw5.model.OmokGame;
import edu.utep.cs.cs4330.hw5.model.P2P;

public class P2PActivity extends GameActivity {
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    ListView pairedDeviceslistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        omokGame = new OmokGame(2);
    }

    @Override
    protected void assignLayout(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new P2PFragmentAdapter(getSupportFragmentManager()));
        } else {
            setContentView(R.layout.activity_network_play);
        }
    }

    @Override
    public void startGame() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                P2PFragment settingsFragment = findP2PFragment();
                GameFragment gameFragment = findGameFragment();
                ((Human) omokGame.getPlayers()[0]).setName(settingsFragment.getEditTextPlayerOne().getText().toString());
                //((P2P) omokGame.getPlayers()[1]).setWebServer(settingsFragment.getEditServer().getText().toString());
                if (!((P2P) omokGame.getPlayers()[1]).isServer()){
                    Log.i("startGame()", "randomWebService");
                    ((P2P) omokGame.getPlayers()[1]).server();
                }
                else{
                    Log.i("startGame()", "smartWebService");
                }

                ((P2P) omokGame.getPlayers()[1]).startStrategy();
                omokGame.setBoard(new Board());
                omokGame.setGameRunning(true);
                omokGame.setTurn(0);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    viewPager.setCurrentItem(1);
                gameFragment.getBoardView().invalidate();
                Toast.makeText(getBaseContext(), R.string.game_started, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(builder.getContext(), R.string.new_game_canceled, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setMessage(R.string.new_game_prompt);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private P2PFragment findP2PFragment() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (P2PFragment) ((GameFragmentAdapter) viewPager.getAdapter()).getRegisteredFragment(0);
        } else {
            return (P2PFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_p2p);
        }
    }

    class P2PFragmentAdapter extends GameFragmentAdapter {
        public P2PFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            Fragment fragment = null;
            if (position == 0) {
                fragment = new P2PFragment();
            } else if (position == 1) {
                fragment = new GameFragment();
            }
            return fragment;
        }

    }

    public void viewPairedDevices(View view){
        final Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();

        pairedDeviceslistView = (ListView) findViewById(R.id.pairedDeviceslistView);

        final ArrayList pairedDevicesArayList = new ArrayList();

        //Add the Device name and address to the arraylist
        for(BluetoothDevice bluetoothDevice : pairedDevices){
            pairedDevicesArayList.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
        }

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevicesArayList);

        pairedDeviceslistView.setAdapter(arrayAdapter);

        //Get address and toast from ListView
        pairedDeviceslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();

                BluetoothDevice device = BA.getRemoteDevice(address);
                // Attempt to connect to the device
                ((P2P) omokGame.getPlayers()[1]).client(device);
                //networkAdapter.writePlay();
                //networkAdapter.setMessageListener(listener);
            }
        });

    }

    public ListView getPairedDeviceslistView(){
        return pairedDeviceslistView;
    }


}

