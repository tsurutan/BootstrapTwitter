

package com.twitter_app.tsuru.twitter.ui;

import android.accounts.OperationCanceledException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.BootstrapServiceProvider;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.authenticator.TwitterOAuthActivity;
import com.twitter_app.tsuru.twitter.core.BootstrapService;
import com.twitter_app.tsuru.twitter.events.NavItemSelectedEvent;
import com.twitter_app.tsuru.twitter.util.Ln;
import com.twitter_app.tsuru.twitter.util.SafeAsyncTask;
import com.twitter_app.tsuru.twitter.util.UIUtils;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Views;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link com.twitter_app.tsuru.twitter.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends ListActivity{

    private TweetAdapter mAdapter;
    private Twitter mTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            mAdapter = new TweetAdapter(this);
            setListAdapter(mAdapter);

            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh://更新ボタンを押したときの処理
                reloadTimeLine();
                showToast("更新しました！");
                return true;
            case R.id.menu_tweet://投稿ボタンを押したときの処理
                Intent intent = new Intent(this, MyTweetActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_home://ホームボタンを押したときの処理
                Intent pro = new Intent(this,TwitterProfileActivity.class);
                startActivity(pro);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {//タイムラインの取得
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
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

    private class TweetAdapter extends ArrayAdapter<twitter4j.Status> {

        private LayoutInflater mInflater;

        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_tweet, null);
            }
            Status item = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getUser().getName());
            TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + item.getUser().getScreenName());
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(item.getText());
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            Picasso.with(this.getContext()).load(item.getUser().getProfileImageURL()).into(icon);//アカウント画像の取得
            return convertView;
        }
    }
}
