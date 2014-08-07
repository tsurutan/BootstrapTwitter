package com.twitter_app.tsuru.twitter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import twitter4j.Status;

/**
 * Created by tsuru on 2014/08/07.
 */
public class TweetTimelineAdapter extends ArrayAdapter<Status> {

    private LayoutInflater inflater;

    public TweetTimelineAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_tweet, null);
        }
        final Status item = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(item.getUser().getName());
        TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
        screenName.setText("@" + item.getUser().getScreenName());
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(item.getText());
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        Picasso.with(this.getContext()).load(item.getUser().getProfileImageURL()).into(icon);//アカウント画像の取得
        final Long userId= item.getId();
        return convertView;
    }
}

