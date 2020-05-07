package com.example.myapp;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


import com.example.myapp.folder1.ServiceBroadcastReceiver;


public class MainActivity extends Activity {
    Context context;
    private static final String TEXT_STATE = "currentText";
    public Context getContext(){
       return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        context = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessClass bck = new ProcessClass();
            bck.launchService(getApplicationContext());
        }
        finish();

    }
}
