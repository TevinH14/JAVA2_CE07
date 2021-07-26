package com.example.hamiltontevin_ce07.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hamiltontevin_ce07.fragment.ArticleListFragment;

public class PostReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArticleListFragment.updateGridView();

    }
}
