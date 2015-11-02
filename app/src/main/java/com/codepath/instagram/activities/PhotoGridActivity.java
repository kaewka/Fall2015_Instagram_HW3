package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PhotoGridFragment;

public class PhotoGridActivity extends AppCompatActivity {

    private static final String TAG = "PhotoGridActivity";

    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        String searchTag = getIntent().getStringExtra(EXTRA_TAG);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frPhotoGrid, PhotoGridFragment.newInstance(userId, searchTag));

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
