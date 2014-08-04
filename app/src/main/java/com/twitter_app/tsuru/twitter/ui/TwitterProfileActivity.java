package com.twitter_app.tsuru.twitter.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/02.
 */
public class TwitterProfileActivity extends Activity{
    Twitter twitter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new TwitterProfileAsync(this).execute();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }


    public class TwitterProfileAsync extends AsyncTask<Void, Void, Void> {

        Context mActivity;
        String user;
        String url;
        TwitterProfileAsync(Context activity){
            super();
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            // TODO 自動生成されたメソッド・スタブ
            super.onPreExecute();
            //下ごしらえ
        }

        // バックグラウンドで実行する処理
        @Override
        protected Void doInBackground(Void... params) {
            //処理どーーんとこいっ！！
            twitter = TwitterUtils.getTwitterInstance(mActivity);
            try {

                twitter=TwitterUtils.getTwitterInstance(mActivity);

                user = twitter.getScreenName();
                url=twitter.users().verifyCredentials().getProfileImageURL();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
        // メインスレッドで実行する処理
        @Override
        protected void onPostExecute(Void result) {

            TextView username=(TextView)findViewById(R.id.largetext);
            username.setText("@"+user);
            ImageView profile=(ImageView)findViewById(R.id.profile_img);
            Picasso.with(mActivity).load(url).into(profile);
        }
    }


}
