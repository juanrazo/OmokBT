package edu.utep.cs.cs4330.hw5.control.activity;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import edu.utep.cs.cs4330.hw5.R;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonOnePlayerClick(View v){
        Intent intent= new Intent(getApplicationContext(), OnePlayerActivity.class);
        startActivity(intent);
    }
    public void onButtonTwoPlayersClick(View v){
        Intent intent= new Intent(getApplicationContext(), TwoPlayersActivity.class);
        startActivity(intent);
    }

    public void onButtonNetworkPlayClick(View v){
        Intent intent= new Intent(getApplicationContext(), NetworkActivity.class);
        startActivity(intent);
    }

    public void onButtonP2PPlayClick(View v){
        if(bluetoothAdapter.isEnabled()){
            Intent intent= new Intent(getApplicationContext(), P2PActivity.class);
            startActivity(intent);
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 3 );
        }
    }
}