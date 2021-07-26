package com.example.hamiltontevin_ce07.services;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.work.Data;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.hamiltontevin_ce07.MainActivity;
import com.example.hamiltontevin_ce07.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DownloadArticleWorker extends Worker {

    public static final String RANDOM_POST = "RANDOM_POST";

    public static final String RANDOM_NUMBER = "RANDOM_NUMBER";

    public DownloadArticleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        int randomNumberInput  = inputData.getInt(RANDOM_NUMBER,1);
        Log.i(MainActivity.TAG, "randomNumberInput: " + randomNumberInput);

       String[] ra = new String[3];

        String data = null;
        try{
            data = NetworkUtils.getNetworkData("https://www.reddit.com/r/starwars/hot.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(data != null) {

            try {
                JSONObject response = new JSONObject(data);

                JSONObject postJson = response.getJSONObject("data");
                JSONArray postJSONArray = postJson.getJSONArray("children");

                JSONObject obj = postJSONArray.getJSONObject(randomNumberInput);

                JSONObject postObj = obj.getJSONObject("data");

                String title = postObj.getString("title");
                String endUrl = postObj.getString("permalink");
                String url = postObj.getString("url");
                Log.i(MainActivity.TAG,"data:"
                        +" title: " + title
                        +" endUrl: " +endUrl
                        +" url: "+ url);
               ra[0] = title;
               ra[1] = endUrl;
               ra[2] = url;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Data outputData = new Data.Builder()
                .putStringArray(RANDOM_POST,ra)
                .build();

        return Result.success(outputData);
    }

}
