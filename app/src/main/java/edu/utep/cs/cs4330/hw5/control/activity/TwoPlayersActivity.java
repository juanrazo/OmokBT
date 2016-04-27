package edu.utep.cs.cs4330.hw5.control.activity;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.fragment.GameFragment;
import edu.utep.cs.cs4330.hw5.control.fragment.TwoPlayersFragment;
import edu.utep.cs.cs4330.hw5.model.Board;
import edu.utep.cs.cs4330.hw5.model.Human;
import edu.utep.cs.cs4330.hw5.model.OmokGame;

public class TwoPlayersActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        omokGame = new OmokGame(false);
        ((Human)omokGame.getPlayers()[0]).setName("Player One");
        ((Human)omokGame.getPlayers()[1]).setName("Player Two");
    }

    @Override
    protected void assignLayout(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(new TwoPlayersFragmentAdapter(getSupportFragmentManager()));
        } else {
            setContentView(R.layout.activity_two_players);
        }
    }

    @Override
    public void startGame() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TwoPlayersFragment settingsFragment = findTwoPlayersFragment();
                GameFragment gameFragment = findGameFragment();
                ((Human) omokGame.getPlayers()[0]).setName(settingsFragment.getEditTextPlayerOne().getText().toString());
                ((Human) omokGame.getPlayers()[1]).setName(settingsFragment.getEditTextPlayerTwo().getText().toString());
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

    private TwoPlayersFragment findTwoPlayersFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (TwoPlayersFragment) ((GameFragmentAdapter) viewPager.getAdapter()).getRegisteredFragment(0);
        } else {
            return (TwoPlayersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_two_players);
        }
    }

    class TwoPlayersFragmentAdapter extends GameFragmentAdapter {

        public TwoPlayersFragmentAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            Fragment fragment = null;
            if (arg0 == 0) {
                fragment = new TwoPlayersFragment();
            } else if (arg0 == 1) {
                fragment = new GameFragment();
            }
            return fragment;
        }

    }
}
