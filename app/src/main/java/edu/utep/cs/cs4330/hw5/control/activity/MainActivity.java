package edu.utep.cs.cs4330.hw5.control.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.utep.cs.cs4330.hw5.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Intent intent= new Intent(getApplicationContext(), P2PActivity.class);
        startActivity(intent);
    }
}