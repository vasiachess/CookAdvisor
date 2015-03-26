package com.vasiachess.cookadvisor;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vasiachess.cookadvisor.data.AdviceContract;

import java.sql.Time;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    private Uri mUri;
    private Button btnStart;
    private Button btnEdit;
    private TextView tvTitle;
    private TextView tvTimer;
    private TextView tvAdvice;
    private Time timeUntilFinish;
    protected CountDownTimer advCountDownTimer;
    protected int advTime = 0;

    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            AdviceContract.AdviceEntry.TABLE_NAME + "." + AdviceContract.AdviceEntry._ID,
            AdviceContract.AdviceEntry.COLUMN_TITLE,
            AdviceContract.AdviceEntry.COLUMN_TIME,
            AdviceContract.AdviceEntry.COLUMN_ADVICE,
    };

    static final int COL_ID = 0;
    static final int COL_TITLE = 1;
    static final int COL_TIME  = 2;
    static final int COL_ADVICE = 3;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        btnStart = (Button) rootView.findViewById(R.id.buttonStart);
        btnEdit = (Button) rootView.findViewById(R.id.buttonEdit);

        tvTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        tvTimer = (TextView) rootView.findViewById(R.id.textViewTimer);
        tvAdvice = (TextView) rootView.findViewById(R.id.textViewAdvice);

        btnStart.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            onClickStart();
        } else if (v == btnEdit) {
            onClickEdit();
        }

    }

    private void onClickStart() {
        if (btnStart.getText() == getString(R.string.start)) {
            btnStart.setText(getString(R.string.reset_time));
            startTimer();
        } else {
            btnStart.setText(getString(R.string.start));
            resetTimer();
        }
    }

    private void onClickEdit() {

        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("title", tvTitle.getText().toString());
        intent.putExtra("time", advTime);
        intent.putExtra("advice", tvAdvice.getText().toString());
        startActivity(intent);

    }

    private void startTimer() {

        advCountDownTimer.start();
        btnStart.setText(getString(R.string.reset_time));

    }


    private void resetTimer() {
        btnStart.setText(getString(R.string.start));
        if(advCountDownTimer != null) {
            advCountDownTimer.cancel();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            String title = data.getString(COL_TITLE);
            tvTitle.setText(title);

            advTime = data.getInt(COL_TIME);
            timeUntilFinish = new Time(advTime*1000);
            String time = timeUntilFinish.toString();
            tvTimer.setText(time);

            advCountDownTimer = new CountDownTimer(advTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // выводим оставшееся время в текстовой метке

                    timeUntilFinish = new Time(millisUntilFinished);
                    String time = timeUntilFinish.toString();
                    tvTimer.setText(time);
                }

                @Override
                public void onFinish() {

                    tvTimer.setText("Done!");
                    btnStart.setText(getString(R.string.start));
                }
            };




            String advice = data.getString(COL_ADVICE);
            tvAdvice.setText(advice);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}


