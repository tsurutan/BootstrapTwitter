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


/**
 * Created by tsuru on 2014/08/07.
 */
public class TweetTimelineAdapter extends ArrayAdapter<Status> {

    private LayoutInflater inflater;
    public Twitter twitter;
    public Context context;
    public int countFavorite;
    public int countRetweet;
    public String favoriteNumberGet;
    public String retweetNumberGet;
    public long id;
    public TwitterGetId retweetGetId;


    public TweetTimelineAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        retweetGetId = new TwitterGetId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_tweet, null);
        }
        twitter = TwitterUtils.getTwitterInstance(getContext());
        final Status item = getItem(position);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        final TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
        final TextView favoriteNumber = (TextView) convertView.findViewById(R.id.favorite_number);
        final TextView retweetNumber = (TextView) convertView.findViewById(R.id.retweet_number);
        final TextView text = (TextView) convertView.findViewById(R.id.text);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        final ImageView favoriteBtn = (ImageView) convertView.findViewById(R.id.favorite);
        final ImageView replyBtn = (ImageView) convertView.findViewById(R.id.reply);
        final ImageView profileImg = (ImageView) convertView.findViewById(R.id.icon);
        final ImageView retweetBtn = (ImageView) convertView.findViewById(R.id.retweet);

        name.setText(item.getUser().getName());
        screenName.setText("@" + item.getUser().getScreenName());
        text.setText(item.getText());
        Picasso.with(this.getContext()).load(item.getUser().getProfileImageURL()).into(icon);


        favoriteNumber.setText(String.valueOf(item.getFavoriteCount()));


        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countFavorite == 0) {

                    alertDialogFavorite(favoriteBtn, item, favoriteNumber);

                } else {
                    favoriteBtn.setImageResource(R.drawable.favorite_action);
                    countFavorite = 0;
                    favoriteNumberGet = String.valueOf(item.getFavoriteCount());
                    favoriteNumber.setText(favoriteNumberGet);
                    new TwitterDestroyFavoriteAsync(context, item).execute();
                }
            }
        });

        retweetNumberGet = String.valueOf(item.getRetweetCount());
        retweetNumber.setText(retweetNumberGet);

        retweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countRetweet == 0) {

                    alertDialogRetweet(retweetBtn, item, retweetNumber);

                } else {
                    retweetBtn.setImageResource(R.drawable.retweet_action);
                    countRetweet = 0;
                    new TwitterDestroyRetweetAsync(context, item, retweetGetId.Id).execute();
                    retweetNumberGet = String.valueOf(item.getRetweetCount());
                    if (item.getRetweetCount() != 0) {
                        retweetNumber.setText(retweetNumberGet);
                    }
                }

            }
        });

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tweetInt = new Intent(context, MyTweetActivity.class);
                tweetInt.putExtra("screenName", item.getUser().getScreenName());
                context.startActivity(tweetInt);
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileInt = new Intent(context, OtherTwitterProfileActivity.class);
                profileInt.putExtra("name", item.getUser().getName());
                profileInt.putExtra("screenName", item.getUser().getScreenName());
                profileInt.putExtra("text", item.getUser().getDescription());
                profileInt.putExtra("url", item.getUser().getProfileImageURL());
                profileInt.putExtra("follow", item.getUser().getFriendsCount());
                profileInt.putExtra("follower", item.getUser().getFollowersCount());
                profileInt.putExtra("userId", item.getUser().getId());
                context.startActivity(profileInt);
            }
        });

        return convertView;
    }

    public void alertDialogFavorite(final ImageView favoriteBtn, final Status item, final TextView favoriteNumber){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.do_favorite);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoriteBtn.setImageResource(R.drawable.favorite_action_clicked);
                        countFavorite = 1;
                        favoriteNumberGet = String.valueOf(item.getFavoriteCount() + 1);
                        favoriteNumber.setText(favoriteNumberGet);
                        new TwitterCreateFavoriteAsync(context, item).execute();
                    }
                }
        );
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void alertDialogRetweet(final ImageView retweetBtn, final Status item, final TextView retweetNumber){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.do_retweet);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retweetBtn.setImageResource(R.drawable.retweet_action_clicked);
                        countRetweet = 1;
                        new TwitterCreateRetweetAsync(context, item, retweetGetId).execute();
                        id = retweetGetId.Id;
                        retweetNumberGet = String.valueOf(item.getRetweetCount() + 1);
                        retweetNumber.setText(retweetNumberGet);

                    }
                }
        );
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}

