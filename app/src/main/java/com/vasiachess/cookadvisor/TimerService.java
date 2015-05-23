package com.vasiachess.cookadvisor;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Vasiliy on 27.03.2015.
 */
public class TimerService extends Service {

    private final String LOG_TAG = TimerService.class.getSimpleName();
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    private int timeUntilFinish;
    private String currentTitle;
    protected CountDownTimer advCountDownTimer;
    MediaPlayer mediaPlayer;
    private int currentId;

    public void onCreate() {
        super.onCreate();
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        startInForeground(intent);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startInForeground( Intent intent ) {

        currentId=Utility.id;

        DetailFragment.tvTimer.setText(Utility.getTime(DetailFragment.advTime));
        currentTitle = DetailFragment.title;

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("CookAdvisor")
                .setContentText(currentTitle + " in progress")
                .setSmallIcon(Utility.getIconResourceForTitle(currentTitle));

        final int maxTime = DetailFragment.advTime * 1000;
        advCountDownTimer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // выводим оставшееся время в текстовой метке

                timeUntilFinish = (int) millisUntilFinished/1000;

                if (currentTitle.equals(DetailFragment.tvTitle.getText().toString())) {
                    DetailFragment.tvTimer.setText(Utility.getTime(timeUntilFinish));
//                    DetailFragment.tvTitle.setText(currentTitle);
                    DetailFragment.btnStart.setText(getString(R.string.reset_time));
//                    DetailFragment.ivIcon.setImageResource(Utility.getIconResourceForTitle(currentTitle));
                } else {
                    DetailFragment.btnStart.setEnabled(false);
                }

                Integer inc = (int) (long) millisUntilFinished;
                mBuilder.setProgress(maxTime, maxTime - inc, false)
                         .setContentText(currentTitle + " in progress - " + Utility.getTime(timeUntilFinish));
                // Displays the progress bar for the first time.
                mNotifyManager.notify(currentId, mBuilder.build());
            }

            @Override
            public void onFinish() {

                mBuilder.setContentText(currentTitle +" Done!")
                        .setProgress(0,0,false);
                mNotifyManager.notify(currentId, mBuilder.build());

                if (currentTitle.equals(DetailFragment.tvTitle.getText().toString())) {
                    DetailFragment.tvTimer.setText("Done!");
                    DetailFragment.btnStart.setText(getString(R.string.start));
                }
                mediaPlayer = new  MediaPlayer();
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.done);
                mediaPlayer.start();
                --Utility.id;
                stopSelf();
            }
        };

        advCountDownTimer.start();
    }

    public void onDestroy() {
        super.onDestroy();
        if(advCountDownTimer != null) {
            advCountDownTimer.cancel();
        }
//        mNotifyManager.cancel(id);
    }

}
