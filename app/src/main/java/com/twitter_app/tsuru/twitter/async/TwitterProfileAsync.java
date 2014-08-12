package com.twitter_app.tsuru.twitter.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/08.
 */
public class TwitterProfileAsync extends AsyncTask<Void, Void, Void> {
    Twitter twitter;
    Context activity;
    String userNameIdStr;
    String userNameStr;
    String profileExplainStr;
    String url;
    String followerNumber;
    String followNumber;
    TextView usernameid;
    TextView username;
    TextView profileExplain;
    ImageView profile;
    Button follow;
    Button follower;
    ProgressDialog prog;


    public TwitterProfileAsync(Context activity,
                               TextView usernameid,
                               TextView username,
                               TextView profileExplain,
                               ImageView profile,
                               Button follow,
                               Button follower
                               ){
        super();
        this.activity = activity;
        this.usernameid=usernameid;
        this.username=username;
        this.profileExplain=profileExplain;
        this.profile=profile;
        this.follow=follow;
        this.follower=follower;

        prog = new ProgressDialog(activity);
        prog.setProgressStyle(prog.STYLE_SPINNER);
        prog.setMessage("読み込み中です");
        prog.setCancelable(true);
        prog.show();
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        twitter = TwitterUtils.getTwitterInstance(activity);
        try {
            twitter=TwitterUtils.getTwitterInstance(activity);
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
        follow.setText("フォロー："+followNumber);
        follower.setText("フォロワー："+followerNumber);
        Picasso.with(activity).load(url).into(profile);
        prog.dismiss();

    }
}


