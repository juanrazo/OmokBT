package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Computer extends Player {
    private Strategy strategyMode;


    public Computer(boolean playerOne) {
        super(playerOne);
    }

    public Computer(Parcel in) {
        super(in);
    }

    public Coordinates findCoordinates(char[][] board) {
        return strategyMode.findCoordinates(board);
    }

    public Strategy getStrategyMode() {
        return strategyMode;
    }

    public void setStrategyMode(Strategy strategyMode) {
        this.strategyMode = strategyMode;
    }

    public static final Parcelable.Creator<Computer> CREATOR = new Parcelable.Creator<Computer>() {
        @Override
        public Computer createFromParcel(Parcel in) {
            return new Computer(in);
        }

        @Override
        public Computer[] newArray(int size) {
            return new Computer[size];
        }
    };
}

