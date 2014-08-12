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

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.async.TwitterProfileAsync;

/**
 * Created by tsuru on 2014/08/02.
 */
public class MyTwitterProfileActivity extends Activity implements View.OnClickListener {
    Button follow;
    Button follower;
    Button favorite;
    Button favoriteRanking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView usernameid=(TextView)findViewById(R.id.atto_name);
        TextView username=(TextView)findViewById(R.id.name);
        TextView plofileexplain=(TextView)findViewById(R.id.profile_explain);
        ImageView profile=(ImageView)findViewById(R.id.profile_img);
        follow = (Button)findViewById(R.id.follow);
        follower=(Button)findViewById(R.id.follower);
        favorite=(Button)findViewById(R.id.favorite);
        new TwitterProfileAsync(this, usernameid, username, plofileexplain, profile, follow, follower).execute();
        favorite.setOnClickListener(this);
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
            Intent favoriteInt = new Intent(this, MyFavoriteActivity.class);
            startActivity(favoriteInt);

    }
}
