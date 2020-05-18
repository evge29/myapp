package com.example.myapp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class NetworkUtils{
    private static String TAG = "Network";


    static String getInfo() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {

            Uri builtURI = Uri.parse("http://192.168.0.15:5000/getjobs/hardware").buildUpon().build();
            Log.i(TAG, "connecting to" + builtURI);
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            JSONString = builder.toString();


        } catch (IOException e) {
            e.printStackTrace();


        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        Log.i(TAG,"getJSON:" +JSONString);
        return JSONString;

    }
}
