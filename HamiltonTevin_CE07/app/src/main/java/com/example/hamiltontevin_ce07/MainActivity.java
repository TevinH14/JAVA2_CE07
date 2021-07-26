package com.example.hamiltontevin_ce07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.hamiltontevin_ce07.database.DataBaseHelper;
import com.example.hamiltontevin_ce07.fragment.ArticleListFragment;
import com.example.hamiltontevin_ce07.network.NetworkTask;
import com.example.hamiltontevin_ce07.network.NetworkUtils;
import com.example.hamiltontevin_ce07.services.ArticleIntentService;
import com.example.hamiltontevin_ce07.services.DownloadArticleWorker;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NetworkTask.OnFinished{

    public static final String TAG = "FUNDAY";

    public static final String PASS_ARRAY_POST = "PASS_ARRAY_POST";

    private static final int EXPANDED_NOTIFICATION = 0x10000000;
    private static final String CHANNEL_ID_SECURITY = "com.example.hamiltontevin_ce07.security";

    private String[] mPostArray = null;

    public DataBaseHelper mDatabase = null;
    private Cursor mCursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = DataBaseHelper.getInstance(MainActivity.this);
        if(mDatabase != null){
            mCursor = DataBaseHelper.getAllArticles();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_articlesContainer, ArticleListFragment.newInstance(mCursor))
                .commit();

        enqueueUniqueWork();
    }


    private void enqueueUniqueWork(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        Data inputData = new Data.Builder()
                .putInt(DownloadArticleWorker.RANDOM_NUMBER,getRandomNumber())
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DownloadArticleWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        UUID mWorkerID = workRequest.getId();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mWorkerID).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                WorkInfo.State state = workInfo.getState();
                if(state == WorkInfo.State.SUCCEEDED)
                {
                    Log.i(MainActivity.TAG,"worker succeeded");

                    Data output = workInfo.getOutputData();
                    String[] outputArray = output.getStringArray(DownloadArticleWorker.RANDOM_POST);
                    if(outputArray != null) {
                        notificationChannel();
                        mPostArray = outputArray;
                        startTask(outputArray[2]);

                    }
                }
            }
        });
    }

    private void startTask(String urlString){
        NetworkTask task = new NetworkTask(this);
        task.execute(urlString);
    }

    private int getRandomNumber(){
        Random r = new Random();
        int low = 0;
        int high = 26;
        return r.nextInt(high - low) + low;

    }

    private  void notificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //create a channel for a notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SECURITY, "Star Wars Post", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Get the latest update on star wars reddit post");
            NotificationManager mgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            if(mgr != null){
                mgr.createNotificationChannel(channel);
            }
        }
    }

    private  void expandedNotification(String[] params, Bitmap _contentImage){

        String linkUrl = "https://www.reddit.com/"+params[1];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_SECURITY);
        builder.setContentTitle(params[0]);

        builder.setSmallIcon(R.drawable.ic_icons8_star_wars);
        Log.i(TAG, "URL:"+ params[2]);
        if(NetworkUtils.checkUrlForImage(params[2])){
            builder.setLargeIcon(_contentImage).build();
        }
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(_contentImage));
        builder.setContentText(linkUrl);

        Intent notifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingItent = PendingIntent.getActivities(this, 0, new Intent[] {notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingItent);

        Intent intent = new Intent(this, ArticleIntentService.class);
        intent.putExtra(PASS_ARRAY_POST,params);
        intent.setAction("ArticleIntentService");
        PendingIntent pi = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_notification_white_24dp, "Save", pi);
        builder.addAction(action);

        NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(mgr != null){
            mgr.notify(EXPANDED_NOTIFICATION  , builder.build());

        }
    }

    @Override
    public void OnPost(Bitmap image) {
        expandedNotification(mPostArray , image);
    }
}
