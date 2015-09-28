package com.vasiachess.cookadvisor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class EditActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Utility.TITLE, getIntent().getExtras().getString(Utility.TITLE));
            arguments.putInt(Utility.TIME, getIntent().getExtras().getInt(Utility.TIME));
            arguments.putString(Utility.ADVICE, getIntent().getExtras().getString(Utility.ADVICE));

            EditFragment fragment = new EditFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.advice_detail_container, fragment)
                    .commit();
        }
    }
}
