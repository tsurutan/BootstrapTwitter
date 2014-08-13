package com.twitter_app.tsuru.twitter.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/11.
 */
public class TwitterDestroyRetweetAsync extends AsyncTask<Void, Void, Void> {
    public Twitter twitter;
    public Context context;
    public twitter4j.Status item;
    public long retweetId;


    public TwitterDestroyRetweetAsync(Context context,
                                     twitter4j.Status item,
                                     long retweetId){
        super();
        this.context = context;
        this.item=item;
        twitter= TwitterUtils.getTwitterInstance(context);
        this.retweetId=retweetId;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            twitter.destroyStatus(retweetId);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Void result) {

        Toast.makeText(context, R.string.delete_retweet, Toast.LENGTH_SHORT).show();

    }
}
