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

import butterknife.ButterKnife;
import butterknife.InjectView;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by tsuru on 2014/08/12.
 */
public class TwitterFavoriteAdapter extends ArrayAdapter<Status> {

    private LayoutInflater inflater;
    public Twitter twitter;
    public Context context;
    public TwitterGetId[] favoriteId;



    public TwitterFavoriteAdapter(Context context, TwitterGetId[] favoriteId) {
        super(context, android.R.layout.simple_list_item_1);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.favoriteId = favoriteId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_favorite_tweet, null);
        }
        final Status item = getItem(position);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        final TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
        final TextView text = (TextView) convertView.findViewById(R.id.text);
        final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        favoriteId[position].item = item;
        twitter = TwitterUtils.getTwitterInstance(getContext());

        name.setText(item.getUser().getName());
        screenName.setText("@" + item.getUser().getScreenName());
        text.setText(item.getText());
        Picasso.with(this.getContext()).load(item.getUser().getProfileImageURL()).into(icon);


        return convertView;
    }

}
