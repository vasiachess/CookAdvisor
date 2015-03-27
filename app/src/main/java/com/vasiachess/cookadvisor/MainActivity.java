package com.vasiachess.cookadvisor;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.vasiachess.cookadvisor.data.AdviceContract;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_NAME = "first_start";
    final String IS_FIRST_START = "IsFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstStart = sp.getBoolean(IS_FIRST_START, true);

        if (isFirstStart) {
            String[] mTitle = { "Title1", "Title2", "Title3", "Title4" };
            Integer[] mTime = { 60, 120, 180, 200 };
            String[] mAdvice = { "Advice1", "Advice2", "Advice3", "Advice4"  };

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

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String title, Integer time, String advice) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("advice", advice);
        startActivity(intent);
    }

}
