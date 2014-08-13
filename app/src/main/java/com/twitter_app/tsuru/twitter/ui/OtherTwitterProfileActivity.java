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

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tsuru on 2014/08/10.
 */
public class OtherTwitterProfileActivity extends ActionBarActivity implements View.OnClickListener {
    public String screenName;
    public String name;
    public String text;
    public String url;
    public String follow;
    public String follower;
    public long userId;

    public @InjectView(R.id.follow)Button followBtn;
    public @InjectView(R.id.follower)Button followerBtn;
    public @InjectView(R.id.favorite)Button favoriteBtn;
    public @InjectView(R.id.atto_name)TextView userNameId;
    public @InjectView(R.id.name)TextView userName;
    public @InjectView(R.id.profile_explain)TextView profileExplain;
    public @InjectView(R.id.profile_img)ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        Intent getItems = getIntent();
        name = getItems.getStringExtra("name");
        screenName = getItems.getStringExtra("screenName");
        text = getItems.getStringExtra("text");
        url = getItems.getStringExtra("url");
        follow = String.valueOf(getItems.getIntExtra("follow", 100));
        follower = String.valueOf(getItems.getIntExtra("follower", 100));
        userId = getItems.getLongExtra("userId", 100);
        followBtn.setText(getString(R.string.follow) + follow);
        followerBtn.setText(getString(R.string.follower) + follower);
        userName.setText(name);
        userNameId.setText("@" + screenName);
        profileExplain.setText(text);
        Picasso.with(this).load(url).into(profile);
        favoriteBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {//バックボタンを押したときの処理
            case R.id.back_tweet:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent otherFavoriteInt = new Intent(this, OtherFavoriteActivity.class);
        otherFavoriteInt.putExtra("userId", userId);
        startActivity(otherFavoriteInt);
    }
}
