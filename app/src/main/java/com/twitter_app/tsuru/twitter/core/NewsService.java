package com.twitter_app.tsuru.twitter.core;

import retrofit.http.GET;


/**
 * Interface for defining the news service to communicate with Parse.com
 */
public interface NewsService {

    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getNews();

}
