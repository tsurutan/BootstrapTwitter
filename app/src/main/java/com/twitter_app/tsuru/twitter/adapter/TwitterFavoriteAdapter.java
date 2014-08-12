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
 * Created by tsuru on 2014/08/12.
 */
public class TwitterFavoriteAdapter extends ArrayAdapter<Status> {

    private LayoutInflater inflater;
    Twitter twitter;
    Context activity;
    TwitterGetId[] favoriteId;

    public TwitterFavoriteAdapter(Context context, TwitterGetId[] favoriteId) {
        super(context, android.R.layout.simple_list_item_1);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.activity=context;
        this.favoriteId=favoriteId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_favorite_tweet, null);
        }
        final Status item = getItem(position);
        favoriteId[position].item=item;
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


        return convertView;
    }

}
