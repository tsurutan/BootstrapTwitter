package com.twitter_app.tsuru.twitter.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/09.
 */
public class TwitterDestroyFavoriteAsync extends AsyncTask<Void, Void, Void> {
    Twitter twitter;
    Context activity;
    twitter4j.Status item;
    Long id;


    public TwitterDestroyFavoriteAsync(Context activity,
                                      twitter4j.Status item){
        super();
        this.activity = activity;
        this.item=item;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        twitter = TwitterUtils.getTwitterInstance(activity);
        try {
            twitter=TwitterUtils.getTwitterInstance(activity);
            id=item.getId();

            twitter.destroyFavorite(id);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Void result) {

        Toast.makeText(activity, "お気に入りを削除しました。", Toast.LENGTH_SHORT).show();

    }
}