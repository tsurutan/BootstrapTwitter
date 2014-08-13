package com.twitter_app.tsuru.twitter.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/08.
 */
public class TwitterProfileAsync extends AsyncTask<Void, Void, Void> {
    public Twitter twitter;
    public Context context;
    public String userNameIdStr;
    public String userNameStr;
    public String profileExplainStr;
    public String url;
    public String followerNumber;
    public String followNumber;
    public TextView usernameid;
    public TextView username;
    public TextView profileExplain;
    public ImageView profile;
    public Button follow;
    public Button follower;
    public ProgressDialog prog;


    public TwitterProfileAsync(Context context,
                               TextView usernameid,
                               TextView username,
                               TextView profileExplain,
                               ImageView profile,
                               Button follow,
                               Button follower
                               ){
        super();
        this.context = context;
        this.usernameid=usernameid;
        this.username=username;
        this.profileExplain=profileExplain;
        this.profile=profile;
        this.follow=follow;
        this.follower=follower;
        prog = new ProgressDialog(context);
        prog.setProgressStyle(prog.STYLE_SPINNER);
        prog.setMessage(context.getString(R.string.loading));
        prog.setCancelable(true);
        prog.show();
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        twitter = TwitterUtils.getTwitterInstance(context);

        try {
            twitter=TwitterUtils.getTwitterInstance(context);
            userNameIdStr = twitter.getScreenName();
            userNameStr = twitter.verifyCredentials().getName();
            profileExplainStr = twitter.verifyCredentials().getDescription();
            url=twitter.users().verifyCredentials().getProfileImageURL();
            followNumber = String.valueOf(twitter.users().verifyCredentials().getFriendsCount());
            followerNumber = String.valueOf(twitter.verifyCredentials().getFollowersCount());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Void result) {
        usernameid.setText("@"+ userNameIdStr);
        username.setText(userNameStr);
        profileExplain.setText(profileExplainStr);
        follow.setText(context.getString(R.string.follow)+"："+followNumber);
        follower.setText(context.getString(R.string.follower)+"："+followerNumber);
        Picasso.with(context).load(url).into(profile);
        prog.dismiss();

    }
}


