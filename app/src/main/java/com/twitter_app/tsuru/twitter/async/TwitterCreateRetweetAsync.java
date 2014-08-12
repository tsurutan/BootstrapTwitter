package com.twitter_app.tsuru.twitter.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.TwitterGetId;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/09.
 */
public class TwitterCreateRetweetAsync extends AsyncTask<Void, Void, Long> {
    Twitter twitter;
    Context activity;
    twitter4j.Status item;
    Long id;
    Long retweetId;
    TwitterGetId retweetGetId;

    public TwitterCreateRetweetAsync(Context activity,
                                     twitter4j.Status item,
                                     TwitterGetId retweetGetId){
        super();
        this.activity = activity;
        this.item=item;
        twitter = TwitterUtils.getTwitterInstance(activity);
        id=item.getId();
        this.retweetGetId=retweetGetId;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(Void... params) {

        try {
            retweetGetId.Id=twitter.retweetStatus(id).getId();
            id=retweetGetId.Id;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return retweetId;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Long result) {

        Toast.makeText(activity, "リツイートしました。", Toast.LENGTH_SHORT).show();
    }



}
