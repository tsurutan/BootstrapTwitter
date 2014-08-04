package com.twitter_app.tsuru.twitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.Injector;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.authenticator.TwitterOAuthActivity;

import javax.inject.Inject;

import butterknife.InjectView;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * Created by tsuru on 2014/08/02.
 */
public class TwitterProfileActivity extends Activity{

   Twitter twitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
    private void showProfileimg(){
        ImageView profile_img=(ImageView)findViewById(R.id.profile_img);
        try {
            User user = twitter.verifyCredentials();
            Picasso.with(this).load(user.getProfileImageURL()).into(profile_img);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
