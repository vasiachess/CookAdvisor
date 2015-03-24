package com.vasiachess.cookadvisor;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private Button btnStart;
    private TextView tvTitle;
    private TextView tvTimer;
    private TextView tvAdvice;
    private Time timeUntilFinish;
    protected CountDownTimer advCountDownTimer;
    protected int advTime = 1;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        btnStart = (Button) rootView.findViewById(R.id.buttonStart);

        tvTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        tvTimer = (TextView) rootView.findViewById(R.id.textViewTimer);
        tvAdvice = (TextView) rootView.findViewById(R.id.textViewAdvice);

        btnStart.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            onClickStart();
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

    private void startTimer() {
        advCountDownTimer = new CountDownTimer(advTime * 60 * 1000, 1000) {
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

        advCountDownTimer.start();
        btnStart.setText(getString(R.string.reset_time));

    }


    private void resetTimer() {
        btnStart.setText(getString(R.string.start));
        if(advCountDownTimer != null) {
            advCountDownTimer.cancel();
        }
    }
}


