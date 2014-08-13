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
import com.twitter_app.tsuru.twitter.TwitterRankingGet;
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
    TwitterRankingGet[] rankingItem;
    ProgressDialog prog;
    TwitterGetId[] favoriteId;
    String[] name;
    long userId;
    int position;
    int count;
    int count_i;
    int[] countName;
    int hoji;
    int max=0;
    int maxCount;
    int secondMax=0;
    int secondCount;
    int thirdMax=0;
    int thirdCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteId = new TwitterGetId[200];
        for(position=0;position<200;position++){
            favoriteId[position]=new TwitterGetId();
        }
        adapter = new TwitterFavoriteAdapter(this, favoriteId);
        setListAdapter(adapter);
        prog=new ProgressDialog(this);
        prog.setProgressStyle(prog.STYLE_SPINNER);
        prog.setMessage("読み込み中です");
        prog.setCancelable(true);
        prog.show();
        twitter = TwitterUtils.getTwitterInstance(this);
        Intent getUserId = getIntent();
        userId = getUserId.getLongExtra("userId",100);
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

            case R.id.back_tweet://バックボタンを押したときの処理
                Intent backInt = new Intent(this, OtherTwitterProfileActivity.class);
                startActivity(backInt);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {//非同期によるタイムラインの取得
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {


            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    Paging paging = new Paging();
                    paging.setCount(150);
                    return twitter.getFavorites(userId,paging);
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
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public  void rankingSearch(){//誰を一番お気に入りにしたかの探索

        name = new String[200];
        rankingItem = new TwitterRankingGet[3];
        for(position=0;position<3;position++){
            rankingItem[position]= new TwitterRankingGet();
        }
        for(position=0;position<200;position++){
            name[position] = new String();
        }
        countName = new int[200];
        //重複しているユーザーを除く
        for(position=0,count=0;favoriteId[position].item!=null;position++){
            for (count_i=0;count_i<=position;count_i++){
                if(name[count_i].equals(favoriteId[position].item.getUser().getName())){
                    break;
                }

            }
            if(count_i==position+1){
                name[count] = favoriteId[position].item.getUser().getName();
                hoji=count;
                count++;
            }
        }
        //お気に入りにされているユーザーを数える
        for (count=0;count<hoji;count++){
            for(position=0;favoriteId[position].item!=null;position++){
                if(name[count].equals(favoriteId[position].item.getUser().getName())){
                    countName[count]++;
                }
            }
        }
        //お気に入りにされた人の順位をつける
        for(count=0;count<hoji;count++){
            if(countName[count]>max){
                max=countName[count];
                maxCount=count;
            }
            if(countName[count]>secondMax&&max>=countName[count]&&maxCount!=count){
                secondMax=countName[count];
                secondCount=count;
            }
            if(countName[count]>thirdMax&&secondMax>=countName[count]&&maxCount!=count&&secondCount!=count){
                thirdMax=countName[count];
                thirdCount=count;
            }
        }
        Intent rankingInt=new Intent(this, TwitterFavoriteRankingActivity.class);
        rankingInt.putExtra("first",name[maxCount]);
        rankingInt.putExtra("second",name[secondCount]);
        rankingInt.putExtra("third",name[thirdCount]);
        rankingInt.putExtra("firstMax",max);
        rankingInt.putExtra("secondMax",secondMax);
        rankingInt.putExtra("thirdMax",thirdMax);
        startActivity(rankingInt);
    }

}
