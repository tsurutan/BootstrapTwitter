package com.twitter_app.tsuru.twitter.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/02.
 */
public class MyTweetActivity extends ActionBarActivity {

    private EditText inputText;
    private Twitter twitter;
    private String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        Intent getName = getIntent();
        screenName = getName.getStringExtra("screenName");
        twitter = TwitterUtils.getTwitterInstance(this);
        inputText = (EditText) findViewById(R.id.input_text);

        //リプライの処理
        if (screenName != null) {
            inputText.setText("@" + screenName);
        }
        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet();
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void tweet() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    twitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast(getString(R.string.tweeted));
                    finish();
                } else {
                    showToast(getString(R.string.missing_tweeting));
                }
            }
        };
        task.execute(inputText.getText().toString());
    }

    private void showToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
