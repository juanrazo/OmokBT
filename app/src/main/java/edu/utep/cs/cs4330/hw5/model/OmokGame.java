package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class OmokGame implements Parcelable{

    private Board board;
    private Player[] players;
    private int turn;
    private boolean gameRunning = false;


    public OmokGame() {
        board = new Board();
        players = new Player[2];
        turn = 0;
    }

    public OmokGame(boolean strategyMode) {
        board = new Board();
        players = new Player[2];
        gameMode(strategyMode);
        turn = 0;
    }

    public OmokGame(int network){
        board = new Board();
        players = new Player[2];
        if (network==1){
            networkMode();
        }
        if(network==2){
            p2pMode();
        }
    }

    protected OmokGame(Parcel in) {
        board = in.readParcelable(Board.class.getClassLoader());
        players[0] = in.readParcelable(Player.class.getClassLoader());
        players[1] = in.readParcelable(Player.class.getClassLoader());
        turn = in.readInt();
        gameRunning = in.readByte() != 0;
    }

    public static final Creator<OmokGame> CREATOR = new Creator<OmokGame>() {
        @Override
        public OmokGame createFromParcel(Parcel in) {
            return new OmokGame(in);
        }

        @Override
        public OmokGame[] newArray(int size) {
            return new OmokGame[size];
        }
    };

    public void gameMode(boolean strategyMode) {
        players[0] = new Human(true);
        players[1] = strategyMode ? new Computer(false) : new Human(false);
    }

    public boolean isPlaceOpen(int x, int y){
        return board.isPlaceOpen(x,y);
    }

    private void networkMode(){
        players[0] = new Human(true);
        players[1] = new Network(false);
    }

    private void p2pMode() {
        players[0] = new Human(true);
        players[1] = new P2P(false);
    }

    public boolean placeStone(Coordinates coordinates) {
        int current = turn;
        boolean winState;
        if (board.placeStone(players[current], coordinates))
            flipTurn();
        winState = board.checkWinner(players[current], coordinates);
        gameRunning = !winState;
        //gameRunning = !winState;
        return winState;
    }

    private void flipTurn() {
        turn = (turn == 0) ? 1 : 0;
    }

    public int getTurn() {
        return turn;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(board, flags);
        dest.writeParcelable(players[0], flags);
        dest.writeParcelable(players[1], flags);
        dest.writeInt(turn);
        dest.writeByte((byte) (gameRunning ? 1 : 0));
    }
}

