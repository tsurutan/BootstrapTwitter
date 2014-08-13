package com.twitter_app.tsuru.twitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;

/**
 * Created by tsuru on 2014/08/10.
 */
public class OtherTwitterProfileActivity extends Activity implements View.OnClickListener {
    String name;
    String screenName;
    String text;
    String url;
    String follow;
    String follower;
    long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent getItems=getIntent();
        TextView usernameid=(TextView)findViewById(R.id.atto_name);
        TextView username=(TextView)findViewById(R.id.name);
        TextView plofileexplain=(TextView)findViewById(R.id.profile_explain);
        ImageView profile=(ImageView)findViewById(R.id.profile_img);
        Button followBtn=(Button)findViewById(R.id.follow);
        Button followerBtn=(Button)findViewById(R.id.follower);
        Button favoriteBtn=(Button)findViewById(R.id.favorite);
        name = getItems.getStringExtra("name");
        screenName = getItems.getStringExtra("screenName");
        text = getItems.getStringExtra("text");
        url = getItems.getStringExtra("url");
        follow = String.valueOf(getItems.getIntExtra("follow", 100));
        follower = String.valueOf(getItems.getIntExtra("follower", 100));
        userId = getItems.getLongExtra("userId",100);
        followBtn.setText("フォロー"+follow);
        followerBtn.setText("フォロワー"+follower);
        username.setText(name);
        usernameid.setText("@"+screenName);
        plofileexplain.setText(text);
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
                Intent main =new Intent(this,MainActivity.class);
                startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent otherFavoriteInt = new Intent(this, OtherFavoriteActivity.class);
        otherFavoriteInt.putExtra("userId",userId);
        startActivity(otherFavoriteInt);
    }
}
