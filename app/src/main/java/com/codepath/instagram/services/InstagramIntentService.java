package com.codepath.instagram.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class InstagramIntentService extends IntentService {
    static final String TAG = "InstagramPostsIntentService";
    public static final String KEY_RESULTS = "KeyResults";
    public static final String KEY_RESULT_CODE = "KeyResultCode";
    public static final String ACTION = "com.codepath.instagram.instagrampostsintentservice";

    private AsyncHttpClient aClient = new SyncHttpClient();
    private InstagramClientDatabase database;

    public InstagramIntentService() {
        super(TAG);
        database = InstagramClientDatabase.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MainApplication.getRestClient().getUserFeedSynchronously(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
                database.emptyAllTables();
                database.addInstagramPosts(posts);

                Intent broadcast = new Intent(ACTION);

                InstagramPosts instagramPosts = new InstagramPosts();
                instagramPosts.posts = posts;
                broadcast.putExtra(KEY_RESULTS, instagramPosts);
                broadcast.putExtra(KEY_RESULT_CODE, Activity.RESULT_OK);
                LocalBroadcastManager.getInstance(InstagramIntentService.this).sendBroadcast(broadcast);
            }
        });
    }
}