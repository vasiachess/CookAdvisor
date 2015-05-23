package com.vasiachess.cookadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.vasiachess.cookadvisor.data.AdviceContract;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_NAME = "first_start";
    final String IS_FIRST_START = "IsFS";
    public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstStart = sp.getBoolean(IS_FIRST_START, true);
        Log.d(LOG_TAG, " OnCreate " );
        Utility.id = 0;

        if (isFirstStart) {
            String[] mTitle = { "Pasta", "Egg", "Sausage", "White Rice" };
            Integer[] mTime = { 600, 180, 120, 900 };
            String[] mAdvice = { "Put pasta in the warm water. Add some oil and salt. Pasta will be delicious!",
                    "Put egg in the warm water. When egg is done, put it in cold water. Then it will be easy to clean from the shell",
                    "Put sausage in the warm water. When sausage is done, put it in cold water for a second.",
                    "The ratio of water to rice is 2 to 1. Put rice in the cold water."  };

            ContentValues adviceValues = new ContentValues();
//            ContentValues[] cvArray = new ContentValues[mTitle.length];
            try {
            for (int j = 0; j < mTitle.length; j++) {
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TITLE, mTitle[j]);
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TIME, mTime[j]);
                adviceValues.put(AdviceContract.AdviceEntry.COLUMN_ADVICE, mAdvice[j]);
                Uri ins = this.getContentResolver().insert(AdviceContract.AdviceEntry.CONTENT_URI,adviceValues);
                Log.d(LOG_TAG, String.valueOf(j) + " - " + mTitle[j]);
            }

//            int inserted = 0;
//                inserted = this.getContentResolver().bulkInsert(AdviceContract.AdviceEntry.CONTENT_URI, cvArray);
//                Log.d(LOG_TAG, "inserted - " + String.valueOf(inserted) + " items");
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

//    Cursor cursor = this.getContentResolver().query(AdviceContract.AdviceEntry.CONTENT_URI, null,null, null, null);
//    cursor.moveToFirst();
//    for(int i = 0; i< cursor.getCount(); i++){
//        String title = cursor.getString(MainFragment.COL_TITLE);
//        Log.d(LOG_TAG, "inserted - " + title);
//        cursor.moveToNext();
//    }


//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainFragment())
//                    .commit();


            if (findViewById(R.id.advice_detail_container) != null) {
                // The detail container view will be present only in the large-screen layouts
                // (res/layout-sw600dp). If this view is present, then the activity should be
                // in two-pane mode.
                mTwoPane = true;
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.advice_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                            .commit();
                }
            } else {
                mTwoPane = false;
                getSupportActionBar().setElevation(0f);
            }


    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onItemSelected(String title, Integer time, String advice) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString("title", title);
            arguments.putInt("time", time);
            arguments.putString("advice", advice);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("time", time);
            intent.putExtra("advice", advice);
            startActivity(intent);
        }
    }

}
