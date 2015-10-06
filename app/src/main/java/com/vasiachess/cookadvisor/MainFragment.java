package com.vasiachess.cookadvisor;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.vasiachess.cookadvisor.data.AdviceContract;

/**
 * Created by vasiliy on 16.03.2015.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static AdviceAdapter adviceAdapter;
    public static final String LOG_TAG = "MyLog: " + MainFragment.class.getSimpleName();
    private ListView mListView;
    private Button btnAdd;
    private BroadcastReceiver br;
    private int mPosition = ListView.INVALID_POSITION;

    private static final int ADVICE_LOADER = 0;

    private static final String[] ADVICE_COLUMNS = {
            AdviceContract.AdviceEntry.TABLE_NAME + "." + AdviceContract.AdviceEntry._ID,
            AdviceContract.AdviceEntry.COLUMN_TITLE,
            AdviceContract.AdviceEntry.COLUMN_TIME,
            AdviceContract.AdviceEntry.COLUMN_ADVICE,
    };

    static final int COL_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_TIME  = 2;
    static final int COL_ADVICE = 3;
    private static final String EDITFRAGMENT_TAG = "EFTAG";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String title, Integer time, String advice);
    }

    public MainFragment() {
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            onClickAdd();
        }
    }

    private void onClickAdd() {

        if (Utility.twoPane) {
            // In two-pane mode, show the edit view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(Utility.TITLE, "");
            arguments.putInt(Utility.TIME, 0);
            arguments.putString(Utility.ADVICE, "");

            EditFragment fragment = new EditFragment();
            fragment.setArguments(arguments);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment, EDITFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtra(Utility.TITLE, "");
            intent.putExtra(Utility.TIME, 0);
            intent.putExtra(Utility.ADVICE, "");
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adviceAdapter = new AdviceAdapter(getActivity(), null, 0);

// Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listViewAdvices);
        btnAdd = (Button) rootView.findViewById(R.id.buttonAdd);
        mListView.setAdapter(adviceAdapter);

        btnAdd.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                 ((Callback) getActivity()).onItemSelected(cursor.getString(COL_TITLE), cursor.getInt(COL_TIME), cursor.getString(COL_ADVICE));

                }

                mPosition = position;
            }
        });

        br = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
	            adviceAdapter.notifyDataSetChanged();
            }
        };

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // create filter for BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Utility.BROADCAST_ACTION);
        // register BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(ADVICE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onNewAdviceAdded() {
        getLoaderManager().restartLoader(ADVICE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Sort order: Ascending, by title.
        String sortOrder = AdviceContract.AdviceEntry.COLUMN_TITLE + " ASC";
        Uri adviceUri = AdviceContract.AdviceEntry.CONTENT_URI;
        return  new CursorLoader(getActivity(),
                adviceUri,
                ADVICE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        adviceAdapter.swapCursor(cursor);

        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adviceAdapter.swapCursor(null);
    }

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(br);
	}


}
