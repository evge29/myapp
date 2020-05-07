package com.example.myapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.Random;

class SimpleAsyncTask extends AsyncTask<Void, Void, String> {

    private static String TAG = "SimpleAsyncTask";



    @Override
    protected String doInBackground(Void... voids) {

        Random r = new Random();
        int n = r.nextInt(11);

        int s = n * 200;

        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                String count = job.getString("count");
                Log.i(TAG, "count="+count);
                String packetSize = job.getString("packetSize");
                Log.i(TAG, "packetSize="+packetSize);
                String date=job.getString("date");
                Log.i(TAG, "date="+date);
                String jobPeriod=job.getString("jobPeriod");
                Log.i(TAG, "jobPeriod="+jobPeriod);



                String pingCmd = "ping -c" + count;
                pingCmd = pingCmd + "-s" + packetSize;
                pingCmd = pingCmd + " " + host;

                String pingResult = "";
                Process ping = Runtime.getRuntime().exec(pingCmd);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(ping.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    pingResult += inputLine;
                }
                in.close();
                if (ping != null) {
                    ping.getOutputStream().close();
                    ping.getErrorStream().close();
                    ping.getInputStream().close();
                }
                Log.i(TAG, "pingResult="+pingResult);
                return pingResult;
            }
            i++;
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Awake at last after sleeping for " + s + " milliseconds!";
    }
    protected void  onPostExecute(String string) {
        super.onPostExecute(string);
    }
}

