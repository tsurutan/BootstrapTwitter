

package com.twitter_app.tsuru.twitter.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.adapter.TweetTimelineAdapter;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.authenticator.TwitterOAuthActivity;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class MainActivity extends ListActivity {

    private TweetTimelineAdapter adapter;
    private Twitter twitter;
    public ProgressDialog progressDialog;
    public static final int pageCountNumber = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //アクセストークンが存在していなかったときの処理
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            adapter = new TweetTimelineAdapter(this);
            setListAdapter(adapter);
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(true);
            progressDialog.show();
            twitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                reloadTimeLine();
                showToast(getString(R.string.renewal));
                return true;
            case R.id.menu_tweet:
                Intent intent = new Intent(this, MyTweetActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_home:
                Intent profile = new Intent(this, MyTwitterProfileActivity.class);
                startActivity(profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {//非同期によるタイムラインの取得
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {


            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    Paging paging = new Paging();
                    //タイムラインの取得数を指定
                    paging.setCount(pageCountNumber);


                    return twitter.getHomeTimeline(paging);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    progressDialog.dismiss();
                    adapter.clear();
                    for (final twitter4j.Status status : result) {
                        adapter.add(status);
                    }
                    getListView().setSelection(0);
                } else {
                    showToast(getString(R.string.missing_timeline));
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
