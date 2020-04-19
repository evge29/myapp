package com.example.myapp.folder1;


import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapp.Global;
import com.example.myapp.ProcessClass;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {
    private static String TAG = JobService.class.getSimpleName();
    private static ServiceBroadcastReceiver restartSensorServiceReceiver;
    private static JobService instance;
    private static JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        ProcessClass bck = new ProcessClass();
        bck.launchService(this);
        registerRestarterReceiver();
        instance = this;
        JobService.jobParameters = jobParameters;

        return false;
    }

    private void registerRestarterReceiver() {

        if (restartSensorServiceReceiver == null)
            restartSensorServiceReceiver = new ServiceBroadcastReceiver();
        else try {
            unregisterReceiver(restartSensorServiceReceiver);
        } catch (Exception e) {

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Global.RESTART_INTENT);
                try {
                    registerReceiver(restartSensorServiceReceiver, filter);
                } catch (Exception e) {
                    try {
                        getApplicationContext().registerReceiver(restartSensorServiceReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "Stopping job");
        Intent broadcastIntent = new Intent(Global.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                unregisterReceiver(restartSensorServiceReceiver);
            }
        }, 1000);

        return false;
    }

    public static void stopJob(Context context) {
        if (instance != null && jobParameters != null) {
            try {
                instance.unregisterReceiver(restartSensorServiceReceiver);
            } catch (Exception e) {
            }
            Log.i(TAG, "Finishing job");
            instance.jobFinished(jobParameters, true);
        }
    }
}
