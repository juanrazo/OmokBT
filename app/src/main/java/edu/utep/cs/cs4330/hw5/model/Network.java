package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class Network extends Player {

    private boolean isSmart = false;
    private WebServiceHandler webServiceHandler;
    private String pid = "";
    private Coordinates networkCoordinates = new Coordinates();

    public Network(boolean playerOne) {
        super(playerOne);
        webServiceHandler = new WebServiceHandler();
    }

    protected Network(Parcel in){
        super(in);
    }

    public void smartWebService(){
        isSmart = true;
        webServiceHandler.setStrategy("smart");

    }

    public void randomWebService(){
        isSmart = false;
        webServiceHandler.setStrategy("random");
    }

    public boolean isSmart(){
        return isSmart;
    }

    public Coordinates getCoordinates(){
        return networkCoordinates;
    }

    public void sendCoordinates(Coordinates coordinates, View view){
        Log.i("PID send", pid);
        webServiceHandler.passCoordinates(coordinates.getX(), coordinates.getY(), view);
        networkCoordinates = webServiceHandler.getCoordinates();
    }

    public void startStrategy(){
        webServiceHandler.executeStrategy();
        pid = webServiceHandler.getPid();
        Log.i("Network PID", pid);
    }

    public String getPid() {
        return pid;
    }

    public void setWebServer(String server){
        webServiceHandler.setServer(server);
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
        dest.writeString(pid);
    }


    public static final Parcelable.Creator<Network> CREATOR = new Parcelable.Creator<Network>() {
        @Override
        public Network createFromParcel(Parcel in) {
            return new Network(in);
        }

        @Override
        public Network[] newArray(int size) {
            return new Network[size];
        }
    };
}

