package com.twitter_app.tsuru.twitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.twitter_app.tsuru.twitter.R;

/**
 * Created by tsuru on 2014/08/12.
 */
public class TwitterFavoriteRankingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_favorite_ranking_activity);
        Intent getItems=getIntent();
        TextView first=(TextView)findViewById(R.id.first);
        TextView second=(TextView)findViewById(R.id.second);
        TextView third=(TextView)findViewById(R.id.third);
        first.setText(getItems.getStringExtra("first")+":　"+getItems.getIntExtra("firstMax",10)+"回");
        second.setText(getItems.getStringExtra("second")+":　"+getItems.getIntExtra("secondMax",10)+"回");
        third.setText(getItems.getStringExtra("third")+":　"+getItems.getIntExtra("thirdMax",10)+"回");
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
                Intent main =new Intent(this,MyFavoriteActivity.class);
                startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }
}
