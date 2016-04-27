package edu.utep.cs.cs4330.hw5.control.fragment;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.activity.TwoPlayersActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class TwoPlayersFragment extends Fragment {
    private EditText editTextPlayerOne;
    private EditText editTextPlayerTwo;
    private Button buttonNewGame;

    public TwoPlayersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_players, container, false);
        editTextPlayerOne = (EditText) view.findViewById(R.id.editTextPlayerOneName);
        editTextPlayerTwo = (EditText) view.findViewById(R.id.editTextPlayerTwoName);
        buttonNewGame = (Button) view.findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TwoPlayersActivity)getActivity()).startGame();
            }
        });
        return view;
    }

    public EditText getEditTextPlayerOne() {
        return editTextPlayerOne;
    }

    public EditText getEditTextPlayerTwo() {
        return editTextPlayerTwo;
    }

    public Button getButtonNewGame() {
        return buttonNewGame;
    }
}
