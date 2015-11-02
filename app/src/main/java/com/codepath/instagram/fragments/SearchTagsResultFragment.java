package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchTagResultsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.DividerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchTagsResultFragment extends Fragment {
    private ArrayList<InstagramSearchTag> tags;
    private SearchTagResultsAdapter adapter;

    public static SearchTagsResultFragment newInstance() {
        return new SearchTagsResultFragment();
    }

    public SearchTagsResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_tag_results, container, false);

        tags = new ArrayList<>();

        RecyclerView rvSearchResults = (RecyclerView) view.findViewById(R.id.rvSearchResults);
        RecyclerView.ItemDecoration itemDecoration =  new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL_LIST);
        rvSearchResults.addItemDecoration(itemDecoration);
        // Create Adapter
        adapter = new SearchTagResultsAdapter(tags);

        // Set Adapter
        rvSearchResults.setAdapter(adapter);

        // Set layout
        rvSearchResults.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    public void fetchSearchResults(String searchTerm) {
        MainApplication.getRestClient().getTagSearch(searchTerm, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    tags = (ArrayList) Utils.decodeSearchTagsFromJsonResponse(response);
                    adapter.replaceAll(tags);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }
}
