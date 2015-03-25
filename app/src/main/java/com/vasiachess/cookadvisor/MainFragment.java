package com.vasiachess.cookadvisor;


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
import android.widget.ListView;

import com.vasiachess.cookadvisor.data.AdviceContract;

/**
 * Created by vasiliy on 16.03.2015.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AdviceAdapter mAdviceAdapter;
    public static final String LOG_TAG = MainFragment.class.getSimpleName();
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final int ADVICE_LOADER = 0;

    private static final String[] ADVICE_COLUMNS = {
            AdviceContract.AdviceEntry.TABLE_NAME + "." + AdviceContract.AdviceEntry._ID,
            AdviceContract.AdviceEntry.COLUMN_TITLE,
            AdviceContract.AdviceEntry.COLUMN_TIME,
    };

    static final int COL_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_TIME  = 2;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri adviceUri);
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdviceAdapter = new AdviceAdapter(getActivity(), null, 0);

// Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listViewAdvices);
        mListView.setAdapter(mAdviceAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    ((Callback) getActivity())
                            .onItemSelected(AdviceContract.AdviceEntry.buildAdviceWithTitle(
                                    cursor.getString(COL_TITLE)
                            ));

                }

                mPosition = position;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(ADVICE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onNewAdviceAdded() {
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

        mAdviceAdapter.swapCursor(cursor);

        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdviceAdapter.swapCursor(null);
    }
}
