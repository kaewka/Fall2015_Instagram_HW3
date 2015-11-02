package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchUsersResultFragment.newInstance();
            case 1:
                return SearchTagsResultFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "USERS";
            case 1:
                return "TAGS";
            default:
                return "";
        }
    }
}
