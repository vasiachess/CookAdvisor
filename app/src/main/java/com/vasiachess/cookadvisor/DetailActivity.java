package com.vasiachess.cookadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Utility.TITLE, getIntent().getExtras().getString(Utility.TITLE));
            arguments.putInt(Utility.TIME, getIntent().getExtras().getInt(Utility.TIME));
            arguments.putString(Utility.ADVICE, getIntent().getExtras().getString(Utility.ADVICE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment)
					.addToBackStack(null)
	                .commit();
        }
    }

	@Override
	public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
	}
}
