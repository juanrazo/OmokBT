package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Human extends Player{
    private int winCount;
    private int lossCount;
    private String name;

    public Human(boolean playerOne) {
        super(playerOne);
    }

    protected Human(Parcel in) {
        super(in);
        winCount = in.readInt();
        lossCount = in.readInt();
        name = in.readString();
    }

    public void addWin() {
        winCount++;
    }

    public void addLoss() {
        lossCount++;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeInt(winCount);
        dest.writeInt(lossCount);
        dest.writeString(name);
    }


    public static final Parcelable.Creator<Human> CREATOR = new Parcelable.Creator<Human>() {
        @Override
        public Human createFromParcel(Parcel in) {
            return new Human(in);
        }

        @Override
        public Human[] newArray(int size) {
            return new Human[size];
        }
    };
}

