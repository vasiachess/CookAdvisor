package com.vasiachess.cookadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vasiachess.cookadvisor.data.AdviceContract;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_NAME = "first_start";
    final String IS_FIRST_START = "IsFS";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstStart = sp.getBoolean(IS_FIRST_START, true);
        Log.d(LOG_TAG, " OnCreate " );

        if (isFirstStart) {
            String[] mTitle = { getResources().getString(R.string.pasta),
                    getResources().getString(R.string.egg),
                    getResources().getString(R.string.sausage),
                    getResources().getString(R.string.white_rice) };
            Integer[] mTime = { 600, 180, 120, 900 };
            String[] mAdvice = { getResources().getString(R.string.advice_pasta),
                    getResources().getString(R.string.advice_egg),
                    getResources().getString(R.string.advice_sausage),
                    getResources().getString(R.string.advice_rice) };

            ContentValues adviceValues = new ContentValues();

            try {
            for (int j = 0; j < mTitle.length; j++) {
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TITLE, mTitle[j]);
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TIME, mTime[j]);
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_ADVICE, mAdvice[j]);
                Uri ins = this.getContentResolver().insert(AdviceContract.AdviceEntry.CONTENT_URI,adviceValues);
                Log.d(LOG_TAG, String.valueOf(j) + " - " + mTitle[j]);
            }

            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            isFirstStart = false;
            SharedPreferences sPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean(IS_FIRST_START, isFirstStart);
            editor.commit();
        }

            if (findViewById(R.id.advice_detail_container) != null) {
                // The detail container view will be present only in the large-screen layouts
                // (res/layout-sw600dp). If this view is present, then the activity should be
                // in two-pane mode.
                Utility.twoPane = true;
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.advice_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                            .commit();
                }
            } else {
                Utility.twoPane = false;
                getSupportActionBar().setElevation(0f);
            }
    }

    @Override
    public void onItemSelected(String title, Integer time, String advice) {

        if (Utility.twoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(Utility.TITLE, title);
            arguments.putInt(Utility.TIME, time);
            arguments.putString(Utility.ADVICE, advice);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Utility.TITLE, title);
            intent.putExtra(Utility.TIME, time);
            intent.putExtra(Utility.ADVICE, advice);
            startActivity(intent);
        }
    }
}
