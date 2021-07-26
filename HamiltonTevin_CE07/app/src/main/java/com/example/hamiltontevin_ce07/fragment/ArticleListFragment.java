package com.example.hamiltontevin_ce07.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.hamiltontevin_ce07.R;
import com.example.hamiltontevin_ce07.adapter.ArticleAdapter;
import com.example.hamiltontevin_ce07.services.PostReceiver;

import java.io.File;


public class ArticleListFragment extends ListFragment{


    private static Cursor mCursor = null;
    private ArticleAdapter mArticleAdapter = null;
    public static PostReceiver mReceiver = new PostReceiver();


    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(Cursor _cursor) {
        mCursor = _cursor;

        Bundle args = new Bundle();
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        if(getActivity() != null) {
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        Log.i(getClass().getName(),"onPause()");
        super.onPause();
        if(getActivity() != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_articles,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mCursor.getCount() > 0){
            mArticleAdapter = new ArticleAdapter(getContext(),R.layout.article_display_layout,mCursor);
            setListAdapter(mArticleAdapter);

        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
    public static void updateGridView(){
      // mReceiver.
    }


}
