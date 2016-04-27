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
import android.widget.Toast;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.fragment.GameFragment;
import edu.utep.cs.cs4330.hw5.control.fragment.OnePlayerFragment;
import edu.utep.cs.cs4330.hw5.model.Board;
import edu.utep.cs.cs4330.hw5.model.Computer;
import edu.utep.cs.cs4330.hw5.model.Human;
import edu.utep.cs.cs4330.hw5.model.OmokGame;
import edu.utep.cs.cs4330.hw5.model.StrategyRandom;
import edu.utep.cs.cs4330.hw5.model.StrategySmart;

public class OnePlayerActivity extends GameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        omokGame = new OmokGame(true);
        ((Human) omokGame.getPlayers()[0]).setName("Player One");
    }

    @Override
    protected void assignLayout(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new OnePlayerFragmentAdapter(getSupportFragmentManager()));
        } else {
            setContentView(R.layout.activity_one_player);
        }
    }

    @Override
    public void startGame() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OnePlayerFragment settingsFragment = findOnePlayerFragment();
                GameFragment gameFragment = findGameFragment();
                ((Human) omokGame.getPlayers()[0]).setName(settingsFragment.getEditTextPlayerOne().getText().toString());
                if (settingsFragment.getRadioButtonRandom().isSelected())
                    ((Computer) omokGame.getPlayers()[1]).setStrategyMode(new StrategyRandom());
                else
                    ((Computer) omokGame.getPlayers()[1]).setStrategyMode(new StrategySmart());
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

    private OnePlayerFragment findOnePlayerFragment() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (OnePlayerFragment) ((GameFragmentAdapter) viewPager.getAdapter()).getRegisteredFragment(0);
        } else {
            return (OnePlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_one_player);
        }
    }

    class OnePlayerFragmentAdapter extends GameFragmentAdapter {
        public OnePlayerFragmentAdapter(FragmentManager fm) {
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
                fragment = new OnePlayerFragment();
            } else if (position == 1) {
                fragment = new GameFragment();
            }
            return fragment;
        }

    }
}

