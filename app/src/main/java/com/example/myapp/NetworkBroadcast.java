package com.example.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NetworkBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CheckMyNetwork myNetwork = new CheckMyNetwork(context);
        if (myNetwork.networkCheck() == true)
        {
            Toast.makeText(context,"NETWORK_CHANGE_BROAD : Connected", Toast.LENGTH_SHORT).show();
            Log.i("TAG","Connected");
        }
        else if(myNetwork.networkCheck() == false){
            Toast.makeText(context,"NETWORK_CHANGE_BROAD : DISconnected", Toast.LENGTH_SHORT).show();
            Log.i("TAG","Disconnect");
        }
    }
}