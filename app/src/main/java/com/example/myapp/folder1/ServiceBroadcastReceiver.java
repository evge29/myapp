package com.example.myapp.folder1;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.example.myapp.Global;
import com.example.myapp.ProcessClass;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class ServiceBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = ServiceBroadcastReceiver.class.getSimpleName();
    private static JobScheduler jobScheduler;
    private ServiceBroadcastReceiver restartSensorServiceReceiver;


    public static long getVersionCode(Context context) {
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long versionCode = System.currentTimeMillis();
            return versionCode;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return 0;
    }



    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "about to start timer " + context.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            scheduleJob(context);
        } else {
            registerRestarterReceiver(context);
            ProcessClass bck = new ProcessClass();
            bck.launchService(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        if (jobScheduler == null) {
            jobScheduler = (JobScheduler) context
                    .getSystemService(JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName = new ComponentName(context,
                JobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setOverrideDeadline(0)
                .setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }


    public static void reStartTracker(Context context) {
        Log.i(TAG, "Restarting tracker");
        Intent broadcastIntent = new Intent(Global.RESTART_INTENT);
        context.sendBroadcast(broadcastIntent);
    }


    private void registerRestarterReceiver(final Context context) {

        if (restartSensorServiceReceiver == null)
            restartSensorServiceReceiver = new ServiceBroadcastReceiver();
        else try{
            context.unregisterReceiver(restartSensorServiceReceiver);
        } catch (Exception e){

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                IntentFilter filter = new IntentFilter();
                filter.addAction(Global.RESTART_INTENT);
                try {
                    context.registerReceiver(restartSensorServiceReceiver, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(restartSensorServiceReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

}
