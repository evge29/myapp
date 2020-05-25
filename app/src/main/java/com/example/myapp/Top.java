package com.example.myapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Top {
    private static String TAG = "TOP";

    public static void doTop(){
        String returnString = null;
        String statResult = "";
        try{
            Process stat = Runtime.getRuntime().exec("top -n 1");
            BufferedReader in = new BufferedReader(new InputStreamReader(stat.getInputStream()));
            String inputLine;

            while (returnString == null || returnString.contentEquals("")){
                returnString = in.readLine();
            }
            statResult += returnString + ",";
            while ((inputLine = in.readLine()) != null)
            {
                inputLine += ";";
                statResult += inputLine;
            }
            in.close();
            if(stat != null){
                stat.getOutputStream().close();
                stat.getInputStream().close();
                stat.getErrorStream().close();
            }
            Log.i(TAG, "stat =" +stat);
            NetworkUtils2.postInfo(statResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
