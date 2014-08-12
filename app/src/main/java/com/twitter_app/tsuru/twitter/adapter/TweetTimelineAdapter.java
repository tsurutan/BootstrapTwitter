package com.twitter_app.tsuru.twitter.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterGetId;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.async.TwitterCreateFavoriteAsync;
import com.twitter_app.tsuru.twitter.async.TwitterCreateRetweetAsync;
import com.twitter_app.tsuru.twitter.async.TwitterDestroyFavoriteAsync;
import com.twitter_app.tsuru.twitter.async.TwitterDestroyRetweetAsync;
import com.twitter_app.tsuru.twitter.ui.MyTweetActivity;
import com.twitter_app.tsuru.twitter.ui.OtherTwitterProfileActivity;

import twitter4j.Status;
import twitter4j.Twitter;

import static android.app.AlertDialog.*;
import static android.support.v4.content.ContextCompat.startActivities;

/**
 * Created by tsuru on 2014/08/07.
 */
public class TweetTimelineAdapter extends ArrayAdapter<Status> {

    private LayoutInflater inflater;
    Twitter twitter;
    Context activity;
    int countFavorite;
    int countRetweet;
    String favoriteNumberGet;
    String retweetNumberGet;
    long id;
    TwitterGetId retweetGetId;
    int test;

    public TweetTimelineAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.activity=context;
        retweetGetId=new TwitterGetId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_tweet, null);
        }
            final Status item = getItem(position);
            test = position;
            twitter = TwitterUtils.getTwitterInstance(getContext());
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getUser().getName());
            final TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + item.getUser().getScreenName());
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(item.getText());
            final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            //アカウント画像の取得
            Picasso.with(this.getContext()).load(item.getUser().getProfileImageURL()).into(icon);
            final ImageView favoriteBtn = (ImageView) convertView.findViewById(R.id.favorite);
            final TextView favoriteNumber = (TextView) convertView.findViewById(R.id.favorite_number);
            favoriteNumberGet= String.valueOf(item.getFavoriteCount());
            if (item.getFavoriteCount()!=0){
                favoriteNumber.setText(favoriteNumberGet);
            }
            //お気に入りボタンを押したときの処理
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countFavorite == 0) {
                        Builder alertDialogBuilder = new Builder(activity);
                        alertDialogBuilder.setMessage("お気に入りにしますか？");
                        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                        alertDialogBuilder.setPositiveButton("はい",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        favoriteBtn.setImageResource(R.drawable.favorite_action_clicked);
                                        countFavorite = 1;
                                        favoriteNumberGet= String.valueOf(item.getFavoriteCount()+1);
                                        favoriteNumber.setText(favoriteNumberGet);
                                        new TwitterCreateFavoriteAsync(activity, item).execute();
                                    }
                                });
                        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                        alertDialogBuilder.setNegativeButton("いいえ",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        // アラートダイアログのキャンセルが可能かどうかを設定します
                        alertDialogBuilder.setCancelable(true);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // アラートダイアログを表示します
                        alertDialog.show();

                    } else {
                        favoriteBtn.setImageResource(R.drawable.favorite_action);
                        countFavorite = 0;
                        favoriteNumberGet= String.valueOf(item.getFavoriteCount());
                        favoriteNumber.setText(favoriteNumberGet);
                        new TwitterDestroyFavoriteAsync(activity, item).execute();
                    }
                }
            });
            final ImageView retweetBtn = (ImageView) convertView.findViewById(R.id.retweet);
            //リツイート数を表示
            final TextView retweetNumber =(TextView) convertView.findViewById(R.id.retweet_number);
            retweetNumberGet= String.valueOf(item.getRetweetCount());
            if(item.getRetweetCount()!=0) {
                retweetNumber.setText(retweetNumberGet);
            }
            //リツイートボタンを押したときの処理
            retweetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countRetweet == 0) {
                        Builder alertDialogBuilder = new Builder(activity);
                        alertDialogBuilder.setMessage("リツイートしますか？");
                        alertDialogBuilder.setPositiveButton("はい",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        retweetBtn.setImageResource(R.drawable.retweet_action_clicked);
                                        countRetweet = 1;
                                        new TwitterCreateRetweetAsync(activity, item, retweetGetId).execute();
                                        id=retweetGetId.Id;
                                        retweetNumberGet= String.valueOf(item.getRetweetCount()+1);
                                        retweetNumber.setText(retweetNumberGet);

                                    }
                                });
                        alertDialogBuilder.setNegativeButton("いいえ",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialogBuilder.setCancelable(true);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        retweetBtn.setImageResource(R.drawable.retweet_action);
                        countRetweet = 0;
                        new TwitterDestroyRetweetAsync(activity, item, retweetGetId.Id).execute();
                        retweetNumberGet= String.valueOf(item.getRetweetCount());
                        if(item.getRetweetCount()!=0){
                            retweetNumber.setText(retweetNumberGet);
                        }
                    }

                }
            });
            final ImageView replyBtn =(ImageView) convertView.findViewById(R.id.reply);
            //リプライボタンを押したときの処理
            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tweetInt = new Intent(activity,MyTweetActivity.class);
                    tweetInt.putExtra("screenName", item.getUser().getScreenName());
                    activity.startActivity(tweetInt);
                }
            });
            final ImageView profileImg =(ImageView) convertView.findViewById(R.id.icon);
            //ユーザーのプロフィール画像を押したときの処理
            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileInt = new Intent(activity, OtherTwitterProfileActivity.class);
                    profileInt.putExtra("name",item.getUser().getName());
                    profileInt.putExtra("screenName",item.getUser().getScreenName());
                    profileInt.putExtra("text",item.getUser().getDescription());
                    profileInt.putExtra("url",item.getUser().getProfileImageURL());
                    profileInt.putExtra("follow",item.getUser().getFriendsCount());
                    profileInt.putExtra("follower",item.getUser().getFollowersCount());
                    activity.startActivity(profileInt);
                }
            });
            final Long userId = item.getId();

        return convertView;
    }

}

