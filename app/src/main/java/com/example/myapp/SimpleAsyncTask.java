package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

class SimpleAsyncTask extends AsyncTask<Void, Void, String> {
    private static String TAG = "SimpleAsyncTask";
    public Context mContext;
    public CheckMyNetwork networkcheck;
    private SharedPreferences prefs;

    public SimpleAsyncTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        networkcheck = new CheckMyNetwork(mContext.getApplicationContext());

        Log.i(TAG,"do in background");
       /* Random r = new Random();
        int n = r.nextInt(11);

        int s = n * 200;

        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try {
            String data = NetworkUtils.getInfo();
            Log.i(TAG, data);

            JSONArray items = new JSONArray(data);

            int i = 0;
            while (i < items.length()) {
                JSONObject job = items.getJSONObject(i);
                Log.i(TAG, "json = "+job.toString());
                String jobType= job.getString("jobType");
                Log.i(TAG, "type="+jobType);
                String host = job.getString("host");
                Log.i(TAG, "host="+host);
                int count = job.getInt("count");
                Log.i(TAG, "count="+count);
                int packetSize = job.getInt("packetSize");
                Log.i(TAG, "packetSize="+packetSize);
                String date=job.getString("date");
                Log.i(TAG, "date="+date);
                int jobPeriod=job.getInt("jobPeriod");
                Log.i(TAG, "jobPeriod="+jobPeriod);


                for (int j=0; j<=(int)(600/jobPeriod);j++)
                {
                    makePing(host,count,packetSize);
                }
                try{
                    Thread.sleep(jobPeriod*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            i++;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    protected void  onPostExecute(String string) {
        super.onPostExecute(string);
    }
    public String makePing(String host, int count, int packetSize) {
        String pingResult = "";
        try {

            String pingCmd = "ping -c" + count;
            pingCmd = pingCmd + "-s" + packetSize;
            pingCmd = pingCmd + " " + host;


            Process ping = Runtime.getRuntime().exec(pingCmd);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(ping.getInputStream()));
            String inputLine=" ";
            while ((inputLine = in.readLine()) != null) {
                pingResult += inputLine;
            }
            in.close();
                /*if (ping != null) {
                    ping.getOutputStream().close();
                    ping.getErrorStream().close();
                    ping.getInputStream().close();
                }*/
            Log.i(TAG, "pingResult="+pingResult);

            prefs = mContext.getSharedPreferences("com.example.myapp.Service",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(networkcheck.networkCheck())
            {
                NetworkUtils2.postInfo(pingResult);
                if(prefs.getString("result1",null)!= null)
                {
                    NetworkUtils2.postInfo(prefs.getString("result1",null));
                    editor.putString("result1",null);
                }
                if(prefs.getString("result2",null)!= null)
                {
                    NetworkUtils2.postInfo(prefs.getString("result2",null));
                    editor.putString("result2",null);
                }
                else
                {
                    if((prefs.getString("result1",null)!=null ) && (prefs.getString("result1",null)!=null)
                            || (prefs.getString("result1",null)==null ) && (prefs.getString("result1",null)==null) ){
                        editor.putString("result1",pingResult);
                    }else {
                        editor.putString("result2",pingResult);
                    }
                    editor.apply();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return pingResult;
    }
}

