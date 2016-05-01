package edu.utep.cs.cs4330.hw5.control.fragment;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.utep.cs.cs4330.hw5.R;
import edu.utep.cs.cs4330.hw5.control.activity.GameActivity;
import edu.utep.cs.cs4330.hw5.model.Computer;
import edu.utep.cs.cs4330.hw5.model.Coordinates;
import edu.utep.cs.cs4330.hw5.model.Human;
import edu.utep.cs.cs4330.hw5.model.Network;
import edu.utep.cs.cs4330.hw5.model.OmokGame;
import edu.utep.cs.cs4330.hw5.model.P2P;
import edu.utep.cs.cs4330.hw5.model.Player;
import edu.utep.cs.cs4330.hw5.view.BoardView;

public class GameFragment extends Fragment {
    private BoardView boardView;
    private TextView textViewTurn;
    private boolean network = false;
    private boolean p2p = false;
    private Coordinates playCoordinates = new Coordinates();
    private Player player;
    private OmokGame omokGame;
    private SoundPool sound;
    private AudioManager manager;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        sound.load(getContext(), R.raw.sound_placing_token,1);
        sound.load(getContext(), R.raw.sound_winning,2);
        manager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        textViewTurn = (TextView) v.findViewById(R.id.textViewTurn);
        omokGame = ((GameActivity) getActivity()).getOmokGame();
        boardView = (BoardView) v.findViewById(R.id.board_view);
        if(omokGame.getPlayers()[1] instanceof P2P){
            Log.i("Omok", "onCreateView instance of P2P");
            ((P2P) omokGame.getPlayers()[1]).getHandler(gameHandler);
            if(((P2P) omokGame.getPlayers()[1]).isClientFirst()){
                omokGame.flipTurn();
            }
        }
        //Logic for the game.
        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (omokGame.isGameRunning()) {
                    int x = processXY(event.getX(), boardView.getWidth());
                    int y = processXY(event.getY(), boardView.getHeight());

                    Log.i("event x: ", Float.toString(event.getX()));
                    Log.i("event y: ", Float.toString(event.getY()));
                    Log.i("from float x: ", Integer.toString(x));
                    Log.i("from float y: ", Integer.toString(y));

                    float curVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    float maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    float leftVolume = curVolume/maxVolume;
                    float rightVolume = curVolume/maxVolume;
                    int priority = 1;
                    float normal_playback_rate = 1f;
                    sound.play(1, leftVolume, rightVolume, priority, 0, normal_playback_rate);

                    //Check if internet is available if so get coordinates from server else
                    //open wifi actitiviy to connect, also check if playing in network mode
                    if (!isNetworkConnected() && omokGame.getPlayers()[1] instanceof Network) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    } else {
                        if (omokGame.isPlaceOpen(x, y)) {

                            player = omokGame.getCurrentPlayer();
                            if (network){
                                placeNetworkStone();
                            }
                            if (p2p){
                                placeP2PStone();
                            }
                            if (player instanceof Human) {
                                playCoordinates = new Coordinates(x, y);
                                Log.i("Human Coordinates ", " " + x + ", " + y);

                                if (omokGame.getPlayers()[1] instanceof Network) {
                                    ((Network) omokGame.getPlayers()[1]).sendCoordinates(playCoordinates, boardView);
                                    network = true;
                                }
                                if (omokGame.getPlayers()[1] instanceof P2P) {
                                    ((P2P) omokGame.getPlayers()[1]).sendCoordinates(playCoordinates);
                                    p2p = true;
                                }
                                placeStone();
                            }
                            player = omokGame.getCurrentPlayer();

                            return true;
                        }
                    }

                    // Logic for playing in human vs human or human vs computer
                    if (omokGame.getPlayers()[1] instanceof Human || omokGame.getPlayers()[1] instanceof Computer) {
                        player = omokGame.getCurrentPlayer();
                        if (player instanceof Human) {
                            playCoordinates = new Coordinates(x, y);
                            Log.i("Human Coordinates ", " " + x + ", " + y);
                            placeStone();
                        }
                        player = omokGame.getCurrentPlayer();
                        if (player instanceof Computer) {
                            Log.i("Computer", "play computer");
                            playCoordinates = ((Computer) omokGame.getCurrentPlayer()).findCoordinates(omokGame.getBoard().getBoard());
                            placeStone();
                        }
                        return true;
                    }
                }

                return false;
            }
        });
        return v;
    }

    //Playing against Dr. Cheon's server
    private void placeNetworkStone(){
        player = omokGame.getCurrentPlayer();
        if (player instanceof Network){
            Log.i("Inside network", "to place stone");
            playCoordinates = ((Network) omokGame.getCurrentPlayer()).getCoordinates();
            Log.i("PlayCoor", "" + playCoordinates.getX() + ", " + playCoordinates.getY());
            placeStone();
        }
    }

    //Playing against Peer to Peer
    private void placeP2PStone(){
        Log.i("Omok", "placeP2PStone()");
        player = omokGame.getCurrentPlayer();
        if(((P2P) omokGame.getPlayers()[1]).isClientFirst() && ((P2P) omokGame.getPlayers()[1]).isFirstMove()){
            if(player instanceof Human){
                player.flipStone();
                omokGame.flipTurn();
                player = omokGame.getCurrentPlayer();
                player.flipStone();
            }
            ((P2P) omokGame.getPlayers()[1]).setIsFirstMove(false);
        }
        Log.i("Omok", "placeP2PStone Player " + omokGame.getTurn());
        if (player instanceof P2P){
            Log.i("Inside P2P", "to place stone");
            playCoordinates = ((P2P) omokGame.getCurrentPlayer()).getCoordinates();
            Log.i("PlayCoor", "" + playCoordinates.getX() + ", " + playCoordinates.getY());
            placeStone();
        }
    }

    // All players user placeStone to place a stone in the board.
    private void placeStone(){
        if (omokGame.placeStone(playCoordinates)) {
            boardView.invalidate();
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (player instanceof Human){
                builder.setMessage(((Human) player).getName() + getResources().getString(R.string.win_message));
                float curVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
                float maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float leftVolume = curVolume/maxVolume;
                float rightVolume = curVolume/maxVolume;
                int priority = 2;
                float normal_playback_rate = 1f;
                sound.play(2, leftVolume, rightVolume, priority, 0, normal_playback_rate);
            }
            else{
                //The game is over
                builder.setMessage(getResources().getString(R.string.loss_message));
                // If playing against a P2P close the connection
                if(!omokGame.isGameRunning() && omokGame.getPlayers()[1] instanceof P2P)
                    ((P2P) omokGame.getPlayers()[1]).sendClose();
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (omokGame.getTurn() == 0)
            textViewTurn.setText(R.string.player_one_turn);
        else
            textViewTurn.setText(R.string.player_two_turn);
        boardView.updateBoard(omokGame.getBoard().getBoard());
        boardView.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        boardView.updateBoard(((GameActivity) getActivity()).getOmokGame().getBoard().getBoard());
        if (((GameActivity) getActivity()).getOmokGame().getTurn() == 0)
            textViewTurn.setText(R.string.player_one_turn);
        else
            textViewTurn.setText(R.string.player_two_turn);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }


    private boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return (mNetworkInfo == null) ? false : true;

        }catch (NullPointerException e){
            return false;

        }
    }

    private int processXY(float event, int widthHeight){
        int x;
        int stepX;
        stepX = widthHeight / 9;
        x = (int) (event / stepX);
        if (event % stepX > stepX / 2)
            x++;
        return x;
    }

    /**
     * A handler was added for a case when a message is recived from the connected thread.
     */
    private final Handler gameHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case P2P.PLAY:
                    switch (msg.arg2){
                        case 1:
                            omokGame.flipTurn();
                            break;
                        case 0:
                            omokGame.getPlayers()[0].flipStone();
                            omokGame.getPlayers()[1].flipStone();

                            break;
                    }
                case P2P.MOVE:
                    Log.i("Omok", "Handler: case MOVE");
                    placeP2PStone();
                    break;
                case P2P.MOVE_ACK:
                    break;
                case P2P.CLOSE:
                    break;
                case P2P.QUIT:
                    break;
            }
        }
    };
}

