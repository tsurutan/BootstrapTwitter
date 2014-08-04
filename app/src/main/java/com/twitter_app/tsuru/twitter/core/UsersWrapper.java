package com.twitter_app.tsuru.twitter.core;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersWrapper {

    private List<User> results;

    public List<User> getResults() {
        return results;
    }
}