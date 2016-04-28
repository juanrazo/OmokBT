package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.UUID;

public class P2P extends Player {

    public final static int RECEIVING = 0;
    public final static int SENDING = 1;
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket socket;
    private final static UUID MY_UUID = UUID.fromString("f5d23654-5558-40bc-ba2c-2277b1269274");
    private Listener listener;
    private ConnectThread connectingThread;
    private AcceptThread acceptingThread;
    private int state = 0;

    private boolean isServer = true;

    private Coordinates p2pCoordinates = new Coordinates();
    private NetworkAdapter p2pConnected;

    public P2P(boolean playerOne) {
        super(playerOne);
        listener = new Listener();
        server();
    }

    protected P2P(Parcel in){
        super(in);
    }

    public void server(){
        Log.i("Omok", "server()");
        if (connectingThread != null) {
            connectingThread.cancel();
            connectingThread = null;
        }
        acceptingThread = new AcceptThread();
        acceptingThread.start();
        state=RECEIVING;
    }

    public void client(BluetoothDevice device){
        Log.i("Omok", "client()");
        isServer = false;
        if (acceptingThread != null) {
            acceptingThread.cancel();
            acceptingThread = null;
        }
        connectingThread = new ConnectThread(device);
        connectingThread.start();
        state = SENDING;
    }

    public void sendPlay(){
        if(state==SENDING){
            p2pConnected.writePlay();
            state=RECEIVING;
            Log.i("Omok", "sendPlay()");
        }
    }

    public void sendMove(Coordinates coordinates) {
        if (state == SENDING) {
            p2pConnected.writeMove(coordinates.getX(), coordinates.getY());
            state= RECEIVING;
            Log.i("Message", "Move Sent");
        }
    }
    public void recieveMessage(){

        while(state ==RECEIVING){
            while(state==RECEIVING && p2pConnected!=null){
                Log.i("Omok", "recieveMessage()");
                if(p2pConnected.receiveMessages())
                    state=SENDING;
            }
        }
        //setMessage(networkAdapter.getPlayType());
    }

    public boolean isServer(){
        return isServer;
    }

    public void setIsServer(boolean client){
        isServer = client;
    }

    public Coordinates getCoordinates(){
        return p2pCoordinates;
    }

    public void sendCoordinates(Coordinates coordinates, View view){

    }

    public void ackPlay(){
        if (state==RECEIVING){
            recieveMessage();
        }

        if(state==SENDING){
            p2pConnected.writePlayAck(true,true);
        }
    }


    public void ackMove(int x, int y){

        if(state==SENDING){
            p2pConnected.writeMoveAck(x , y);
        }
        state=SENDING;
    }

    public void currentState(){

    }

    private void startNetworkAdapter(BluetoothSocket socket){
        p2pConnected = new NetworkAdapter(socket);
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = BA.listenUsingRfcommWithServiceRecord("Secure", MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                Log.i("Waiting ", "For Connetion");
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    Log.i("Socket ", "Found");
                    // Do work to manage the connection (in a separate thread)
                    BluetoothDevice device = socket.getRemoteDevice();
//                    networkAdapter = new NetworkAdapter(socket);
//                    networkAdapter.setMessageListener(listener);
                    Log.i("RemoteDevice", device.getName());
                    Log.i("Main", "Recieve");
                    startNetworkAdapter(socket);
                    //networkAdapter.receiveMessages();

                    try{
                        mmServerSocket.close();
                    }catch (IOException e){

                    }
                    break;
                }
            }

        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            BA.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.i("Connect Thread", "Connected");

            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            startNetworkAdapter(mmSocket);
            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
            //networkAdapter = new NetworkAdapter(mmSocket);
            //networkAdapter.setMessageListener(listener);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class Listener implements NetworkAdapter.MessageListener{

        public Listener(){

        }

        @Override
        public void messageReceived(NetworkAdapter.MessageType type, int x, int y) {
            switch (type){
                case PLAY:
                    break;
                case PLAY_ACK:
                    if(x == y){
                        state = RECEIVING;
                        recieveMessage();
                    }
                    if(x!=y){
                        state = SENDING;
                    }
                    break;
                case MOVE:
                    p2pCoordinates.setX(x);
                    p2pCoordinates.setY(y);
                    state=SENDING;
                    ackMove(x, y);
                    break;
                case MOVE_ACK:
                    state=RECEIVING;
                    break;
                case CLOSE:
                    break;
                case QUIT:
                    break;
                case UNKNOWN:
                    break;
                default:
                    break;
            }
        }
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

    }


    public static final Parcelable.Creator<P2P> CREATOR = new Parcelable.Creator<P2P>() {
        @Override
        public P2P createFromParcel(Parcel in) {
            return new P2P(in);
        }

        @Override
        public P2P[] newArray(int size) {
            return new P2P[size];
        }
    };
}