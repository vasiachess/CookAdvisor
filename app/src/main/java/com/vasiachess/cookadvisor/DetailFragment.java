package com.vasiachess.cookadvisor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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
public class DetailFragment extends Fragment implements View.OnClickListener {

    private Button btnStart;
    private Button btnEdit;
    private Button btnDelete;
    private TextView tvTitle;
    private TextView tvTimer;
    private TextView tvAdvice;
    private Time timeUntilFinish;

    private String title = "";
    private String advice = "";

    protected CountDownTimer advCountDownTimer;
    protected int advTime = 0;
    private final String LOG_TAG = DetailFragment.class.getSimpleName();



    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        btnStart = (Button) rootView.findViewById(R.id.buttonStart);
        btnEdit = (Button) rootView.findViewById(R.id.buttonEdit);
        btnDelete = (Button) rootView.findViewById(R.id.buttonDelete);

        tvTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        tvTimer = (TextView) rootView.findViewById(R.id.textViewTimer);
        tvAdvice = (TextView) rootView.findViewById(R.id.textViewAdvice);

        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString("title");
            tvTitle.setText(title);

            advTime = arguments.getInt("time");
            setTimer();

            advice = arguments.getString("advice");
            tvAdvice.setText(advice);
        }

        btnStart.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            onClickStart();
        } else if (v == btnEdit) {
            onClickEdit();
        } else if (v == btnDelete) {
            onClickDelete();
        }

    }

    private void onClickDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete " + title + "?");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        int del = getActivity().getContentResolver().delete(AdviceContract.AdviceEntry.CONTENT_URI, "title = ? and time = ?", new String[]{title, String.valueOf(advTime)});
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });

        builder.show();

    }

    private void onClickStart() {
        if (btnStart.getText() == getString(R.string.start)) {
            btnStart.setText(getString(R.string.reset_time));
            startTimer();

            Intent startIntent = new Intent(getActivity(), TimerService.class);
            getActivity().startService(startIntent);

        } else {
            btnStart.setText(getString(R.string.start));
            resetTimer();
        }
    }

    private void onClickEdit() {

        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("time", advTime);
        intent.putExtra("advice", advice);
        startActivity(intent);

    }

    private void setTimer() {

        timeUntilFinish = new Time(advTime*1000);
        tvTimer.setText(timeUntilFinish.toString());

        advCountDownTimer = new CountDownTimer(advTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // выводим оставшееся время в текстовой метке

                timeUntilFinish = new Time(millisUntilFinished);
                tvTimer.setText(timeUntilFinish.toString());
            }

            @Override
            public void onFinish() {

                tvTimer.setText("Done!");
                btnStart.setText(getString(R.string.start));
            }
        };

    }

    private void startTimer() {

        setTimer();
        advCountDownTimer.start();
        btnStart.setText(getString(R.string.reset_time));
    }


    private void resetTimer() {
        btnStart.setText(getString(R.string.start));
        if(advCountDownTimer != null) {
            advCountDownTimer.cancel();
        }
        setTimer();
    }

}


