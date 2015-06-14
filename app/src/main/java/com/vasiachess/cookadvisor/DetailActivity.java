package com.vasiachess.cookadvisor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
//            arguments.putString("id", getIntent().getExtras().getString("id"));
            arguments.putString("title", getIntent().getExtras().getString("title"));
            arguments.putInt("time", getIntent().getExtras().getInt("time"));
            arguments.putString("advice", getIntent().getExtras().getString("advice"));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.advice_detail_container, fragment)
                    .commit();
        }
    }



}
