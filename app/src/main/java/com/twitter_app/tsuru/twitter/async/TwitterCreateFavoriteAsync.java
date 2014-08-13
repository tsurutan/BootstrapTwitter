package com.twitter_app.tsuru.twitter.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/08.
 */
//お気に入りの処理を行う非同期タスクです。
public class TwitterCreateFavoriteAsync extends AsyncTask<Void, Void, Void> {
    public Twitter twitter;
    public Context context;
    public twitter4j.Status item;
    public long id;


    public TwitterCreateFavoriteAsync(Context context,
                                      twitter4j.Status item){
        super();
        this.context = context;
        this.item=item;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        twitter = TwitterUtils.getTwitterInstance(context);
        try {
            twitter=TwitterUtils.getTwitterInstance(context);
            id=item.getId();

            twitter.createFavorite(id);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Void result) {

        Toast.makeText(context, R.string.favorited, Toast.LENGTH_SHORT).show();

    }
}
