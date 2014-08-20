package com.twitter_app.tsuru.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.async.TwitterProfileAsync;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tsuru on 2014/08/02.
 */
public class MyTwitterProfileActivity extends ActionBarActivity implements View.OnClickListener {
    public @InjectView(R.id.follow)Button follow;
    public @InjectView(R.id.follower)Button follower;
    public @InjectView(R.id.favorite)Button favorite;
    public @InjectView(R.id.atto_name)TextView userNameId;
    public @InjectView(R.id.name)TextView userName;
    public @InjectView(R.id.profile_explain)TextView profileExplain;
    public @InjectView(R.id.profile_img)ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);
        new TwitterProfileAsync(this, userNameId, userName, profileExplain, profile, follow, follower).execute();
        favorite.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_tweet:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent favoriteInt = new Intent(this, MyFavoriteActivity.class);
        startActivity(favoriteInt);

    }
}
