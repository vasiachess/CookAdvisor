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
            arguments.putString("title", getIntent().getExtras().getString("title"));
            arguments.putInt("time", getIntent().getExtras().getInt("time"));
            arguments.putString("advice", getIntent().getExtras().getString("advice"));

            EditFragment fragment = new EditFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.advice_detail_container, fragment)
                    .commit();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

}