package edu.utep.cs.cs4330.hw5.control.activity;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

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
import android.widget.Toast;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.fragment.GameFragment;
import edu.utep.cs.cs4330.hw5.control.fragment.NetworkFragment;
import edu.utep.cs.cs4330.hw5.model.Board;
import edu.utep.cs.cs4330.hw5.model.Human;
import edu.utep.cs.cs4330.hw5.model.Network;
import edu.utep.cs.cs4330.hw5.model.OmokGame;

public class NetworkActivity extends GameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        omokGame = new OmokGame(1);
    }

    @Override
    protected void assignLayout(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new NetworkFragmentAdapter(getSupportFragmentManager()));
        } else {
            setContentView(R.layout.activity_network_play);
        }
    }

    @Override
    public void startGame() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                NetworkFragment settingsFragment = findNetworkFragment();
                GameFragment gameFragment = findGameFragment();
                ((Human) omokGame.getPlayers()[0]).setName(settingsFragment.getEditTextPlayerOne().getText().toString());
                ((Network) omokGame.getPlayers()[1]).setWebServer(settingsFragment.getEditServer().getText().toString());
                if (!((Network) omokGame.getPlayers()[1]).isSmart()){
                    Log.i("startGame()", "randomWebService");
                    ((Network) omokGame.getPlayers()[1]).randomWebService();
                }
                else{
                    Log.i("startGame()", "smartWebService");
                    ((Network) omokGame.getPlayers()[1]).smartWebService();
                }

                ((Network) omokGame.getPlayers()[1]).startStrategy();
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

    private NetworkFragment findNetworkFragment() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (NetworkFragment) ((GameFragmentAdapter) viewPager.getAdapter()).getRegisteredFragment(0);
        } else {
            return (NetworkFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_network_play);
        }
    }

    class NetworkFragmentAdapter extends GameFragmentAdapter {
        public NetworkFragmentAdapter(FragmentManager fm) {
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
                fragment = new NetworkFragment();
            } else if (position == 1) {
                fragment = new GameFragment();
            }
            return fragment;
        }

    }
}

