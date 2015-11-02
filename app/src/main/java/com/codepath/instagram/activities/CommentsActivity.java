package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {
    private ArrayList<InstagramComment> comments;
    private InstagramCommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        comments = new ArrayList<>();
        // Get RecyclerView Reference
        RecyclerView rvComments = (RecyclerView) findViewById(R.id.rvComments);
        // Create Adapter
        adapter = new InstagramCommentsAdapter(comments);

        // Set Adapter
        rvComments.setAdapter(adapter);

        // Set layout
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        String mediaId = getIntent().getStringExtra("mediaId");
        fetchComments(mediaId);

        getSupportActionBar().setTitle(getResources().getString(R.string.comments));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void fetchComments(String mediaId) {
        InstagramClient client = new InstagramClient(this);
        client.getComments(mediaId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    comments.clear();
                    comments.addAll(Utils.decodeCommentsFromJsonResponse(response));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
