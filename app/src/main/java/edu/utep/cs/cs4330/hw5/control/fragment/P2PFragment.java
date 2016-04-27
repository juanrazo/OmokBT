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
import android.widget.RadioButton;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.activity.GameActivity;
import edu.utep.cs.cs4330.hw5.control.activity.P2PActivity;
import edu.utep.cs.cs4330.hw5.model.P2P;


public class P2PFragment extends Fragment {
    private EditText editTextPlayerOne;
    private RadioButton radioButtonServer;
    private RadioButton radioButtonClient;
    private Button buttonNewGame;
    private EditText editServer;

    public P2PFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_p2p, container, false);
        editTextPlayerOne = (EditText) view.findViewById(R.id.editTextPlayerOneName);
        editServer = (EditText) view.findViewById(R.id.serverAddress);
        radioButtonServer = (RadioButton) view.findViewById(R.id.radioButtoServer);
        radioButtonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        radioButtonClient = (RadioButton) view.findViewById(R.id.radioButtonClient);
        radioButtonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        buttonNewGame = (Button) view.findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((P2PActivity) getActivity()).startGame();
            }
        });
        return view;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtoServer:
                if (checked) {
                    ((P2P) ((GameActivity) getActivity()).
                            getOmokGame().getPlayers()[1]).randomWebService();
                }
                break;
            case R.id.radioButtonClient:
                if (checked) {
                    ((P2P) ((GameActivity) getActivity()).
                            getOmokGame().getPlayers()[1]).smartWebService();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(){
//            radioButtonServer.setSelected(true);
//            Log.i("On Resume", "random true");
//        }
//        else{
//            radioButtonClient.setSelected(true);
//            Log.i("On Resume", "smart true");
//        }
    }

    public EditText getEditServer(){return editServer;}

    public EditText getEditTextPlayerOne() {
        return editTextPlayerOne;
    }

    public RadioButton getRadioButtonServer() {
        return radioButtonServer;
    }

    public RadioButton getRadioButtonClient() {
        return radioButtonClient;
    }

    public Button getButtonNewGame() {
        return buttonNewGame;
    }
}


