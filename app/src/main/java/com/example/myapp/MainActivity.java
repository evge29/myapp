package com.example.myapp;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


import com.example.myapp.folder1.ServiceBroadcastReceiver;


public class MainActivity extends Activity {

    private static final String TEXT_STATE = "currentText";
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

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
