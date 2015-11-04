package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.codepath.instagram.services.InstagramIntentService;

import java.util.ArrayList;

public class PostsFragment extends Fragment {
    private ArrayList<InstagramPost> posts;
    private InstagramPostsAdapter adapter;

    MenuItem miActionProgressItem;
    private SwipeRefreshLayout swipeContainer;
    private InstagramClientDatabase database;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(InstagramIntentService.KEY_RESULT_CODE, Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                // Extract the json string from the bundle and save it to SharedPreferences.
                InstagramPosts instagramPosts = (InstagramPosts)intent.getSerializableExtra(InstagramIntentService.KEY_RESULTS);
                posts = (ArrayList<InstagramPost>) instagramPosts.posts;
                adapter.addAll(posts);

                database.emptyAllTables();
                database.addInstagramPosts(posts);
            }
        }
    };

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    public PostsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_posts, container, false);

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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPosts(view);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        database = InstagramClientDatabase.getInstance(view.getContext());

        fetchPosts(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(InstagramIntentService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void fetchPosts(final View view) {
        showProgressBar();
        if (!isNetworkAvailable()) {
            posts = (ArrayList<InstagramPost>) database.getAllInstagramPosts();
            adapter.addAll(posts);
        } else {
            Intent i = new Intent(getActivity(), InstagramIntentService.class);
            getActivity().startService(i);

        }
        swipeContainer.setRefreshing(false);
        hideProgressBar();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar.
        inflater.inflate(R.menu.menu_home, menu);
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
    }

    public void showProgressBar() {
        // Show progress item
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    public void hideProgressBar() {
        // Hide progress item
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }
}
