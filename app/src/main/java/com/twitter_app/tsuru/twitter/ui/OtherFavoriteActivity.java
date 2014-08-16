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
import com.twitter_app.tsuru.twitter.TwitterGetId;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.adapter.TwitterFavoriteAdapter;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/13.
 */
public class OtherFavoriteActivity extends ListActivity {

    private TwitterFavoriteAdapter adapter;
    private Twitter twitter;
    public ProgressDialog prog;
    public TwitterGetId[] favoriteId;
    public String[] name;
    public long userId;
    public int count;
    public int count_i;
    public int[] countName;
    public int hoji;
    public static int pageCountNumber = 150;
    public static int getIdNumber = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteId = new TwitterGetId[getIdNumber];
        for (int position = 0; position < getIdNumber; position++) {
            favoriteId[position] = new TwitterGetId();
        }
        adapter = new TwitterFavoriteAdapter(this, favoriteId);
        twitter = TwitterUtils.getTwitterInstance(this);
        setListAdapter(adapter);

        prog = new ProgressDialog(this);
        prog.setProgressStyle(prog.STYLE_SPINNER);
        prog.setMessage(getString(R.string.loading));
        prog.setCancelable(true);
        prog.show();

        Intent getUserId = getIntent();
        userId = getUserId.getLongExtra("userId", 100);

        reloadTimeLine();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ranking_twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ranking_tweet:
                rankingSearch();
                return true;

            case R.id.back_tweet:
                Intent backInt = new Intent(this, OtherTwitterProfileActivity.class);
                startActivity(backInt);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //非同期によるタイムラインの取得
    private void reloadTimeLine() {
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {


            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    Paging paging = new Paging();
                    paging.setCount(pageCountNumber);
                    return twitter.getFavorites(userId, paging);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    prog.dismiss();
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

    //誰を一番お気に入りにしたかの探索
    public void rankingSearch() {

        name = new String[getIdNumber];
        for (int position = 0; position < getIdNumber; position++) {
            name[position] = new String();
        }
        countName = new int[getIdNumber];
        //重複しているユーザーを除く
        for (int position = 0, count = 0; favoriteId[position].item != null; position++) {
            for (count_i = 0; count_i <= position; count_i++) {
                if (name[count_i].equals(favoriteId[position].item.getUser().getName())) {
                    break;
                }

            }
            if (count_i == position + 1) {
                name[count] = favoriteId[position].item.getUser().getName();
                hoji = count;
                count++;
            }
        }
        //お気に入りにされているユーザーを数える
        for (count = 0; count < hoji; count++) {
            for (int position = 0; favoriteId[position].item != null; position++) {
                if (name[count].equals(favoriteId[position].item.getUser().getName())) {
                    countName[count]++;
                }
            }
        }
        //お気に入りにされた人の順位をつける
        for (count = 0; count < hoji; count++) {
            if (countName[count] > favoriteId[0].max) {
                favoriteId[0].max = countName[count];
                favoriteId[0].count = count;
            }
            if (countName[count] > favoriteId[1].max && favoriteId[0].max >= countName[count] && favoriteId[0].count != count) {
                favoriteId[1].max = countName[count];
                favoriteId[1].count = count;
            }
            if (countName[count] > favoriteId[2].max && favoriteId[1].max >= countName[count] && favoriteId[0].count != count && favoriteId[1].count != count) {
                favoriteId[2].max = countName[count];
                favoriteId[2].count = count;
            }
        }
        Intent rankingInt = new Intent(this, TwitterFavoriteRankingActivity.class);
        rankingInt.putExtra("first", name[favoriteId[0].count]);
        rankingInt.putExtra("second", name[favoriteId[1].count]);
        rankingInt.putExtra("third", name[favoriteId[2].count]);
        rankingInt.putExtra("firstMax", favoriteId[0].max);
        rankingInt.putExtra("secondMax", favoriteId[1].max);
        rankingInt.putExtra("thirdMax", favoriteId[2].max);
        startActivity(rankingInt);
    }

}
