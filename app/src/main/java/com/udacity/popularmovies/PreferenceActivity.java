package com.udacity.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.popularmovies.fragments.SettingsFragment;

public class PreferenceActivity extends AppCompatActivity {

    private static final String TAG = PreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
