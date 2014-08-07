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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new TwitterProfileAsync(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        Context activity;
        String userNameIdStr;
        String userNameStr;
        String profileExplainStr;
        String url;
        TwitterProfileAsync(Context activity){
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // バックグラウンドで実行する処理
        @Override
        protected Void doInBackground(Void... params) {
            twitter = TwitterUtils.getTwitterInstance(activity);
            try {
                twitter=TwitterUtils.getTwitterInstance(activity);
                userNameIdStr = twitter.getScreenName();
                userNameStr = twitter.verifyCredentials().getName();
                profileExplainStr = twitter.verifyCredentials().getDescription();
                url=twitter.users().verifyCredentials().getProfileImageURL();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
        // メインスレッドで実行する処理
        @Override
        protected void onPostExecute(Void result) {
            TextView usernameid=(TextView)findViewById(R.id.atto_name);
            usernameid.setText("@"+ userNameIdStr);
            TextView username=(TextView)findViewById(R.id.name);
            username.setText(userNameStr);
            TextView plofileexplain=(TextView)findViewById(R.id.profile_explain);
            plofileexplain.setText(profileExplainStr);
            ImageView profile=(ImageView)findViewById(R.id.profile_img);
            Picasso.with(activity).load(url).into(profile);
        }
    }


}
