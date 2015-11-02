package com.codepath.instagram.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PostsFragment extends Fragment {
    private ArrayList<InstagramPost> posts;
    private InstagramPostsAdapter adapter;

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    public PostsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        posts = new ArrayList<>();
        // Get RecyclerView Reference
        RecyclerView rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        RecyclerView.ItemDecoration itemDecoration = new
                SimpleVerticalSpacerItemDecoration(12);
        rvPosts.addItemDecoration(itemDecoration);
        // Create Adapter
        adapter = new InstagramPostsAdapter(posts);

        // Set Adapter
        rvPosts.setAdapter(adapter);

        // Set layout
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
        fetchPosts(view);
        return view;
    }

    private void fetchPosts(final View view) {
        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Error connecting to network")
                    .setTitle("Network Error");
            builder.create().show();
            return;
        }

        MainApplication.getRestClient().getUserFeed(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    posts.clear();
                    posts.addAll(Utils.decodePostsFromJsonResponse(response));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Error connecting to network")
                        .setTitle("Network Error");
                builder.create().show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
