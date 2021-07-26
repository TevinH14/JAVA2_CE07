package com.example.hamiltontevin_ce07.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.hamiltontevin_ce07.MainActivity;
import com.example.hamiltontevin_ce07.database.DataBaseHelper;
import com.example.hamiltontevin_ce07.fragment.ArticleListFragment;


public class ArticleIntentService extends IntentService {
    public ArticleIntentService() {
        super("ArticleIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(MainActivity.TAG, "reached on ArticleIntentService");
        if(intent != null && intent.hasExtra(MainActivity.PASS_ARRAY_POST)){
            saveArticleData(intent.getStringArrayExtra(MainActivity.PASS_ARRAY_POST));
        }
    }

    private void saveArticleData(String[] _params){
            DataBaseHelper.insertArticles(_params);
            sendBroadcast();
    }

    private void sendBroadcast(){
        Intent intent = new Intent();
        sendBroadcast(intent);
    }
}
