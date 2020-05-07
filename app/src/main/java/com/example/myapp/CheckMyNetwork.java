package com.example.myapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckMyNetwork {
    public Context context1;
    public boolean connetion;
    public CheckMyNetwork(Context context){
        context1 = context;
    }


    public boolean networkCheck(){
        ConnectivityManager connMgr = (ConnectivityManager)
                context1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo !=null)
        {
            connetion =true;
        }
        else
        {
            connetion = false;
        }
        return connetion;
    }
}