package com.twitter_app.tsuru.twitter;

import android.accounts.AccountManager;
import android.content.Context;

import com.twitter_app.tsuru.twitter.authenticator.ApiKeyProvider;
import com.twitter_app.tsuru.twitter.authenticator.BootstrapAuthenticatorActivity;
import com.twitter_app.tsuru.twitter.authenticator.LogoutService;
import com.twitter_app.tsuru.twitter.core.BootstrapService;
import com.twitter_app.tsuru.twitter.core.Constants;
import com.twitter_app.tsuru.twitter.core.PostFromAnyThreadBus;
import com.twitter_app.tsuru.twitter.core.RestAdapterRequestInterceptor;
import com.twitter_app.tsuru.twitter.core.RestErrorHandler;
import com.twitter_app.tsuru.twitter.core.TimerService;
import com.twitter_app.tsuru.twitter.core.UserAgentProvider;
import com.twitter_app.tsuru.twitter.ui.BootstrapTimerActivity;
import com.twitter_app.tsuru.twitter.ui.CheckInsListFragment;
import com.twitter_app.tsuru.twitter.ui.MainActivity;
import com.twitter_app.tsuru.twitter.ui.NavigationDrawerFragment;
import com.twitter_app.tsuru.twitter.ui.NewsActivity;
import com.twitter_app.tsuru.twitter.ui.NewsListFragment;
import com.twitter_app.tsuru.twitter.ui.UserActivity;
import com.twitter_app.tsuru.twitter.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
