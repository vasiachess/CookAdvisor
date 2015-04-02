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

import java.sql.Time;

/**
 * Created by Vasiliy on 27.03.2015.
 */
public class TimerService extends Service {

    private final String LOG_TAG = TimerService.class.getSimpleName();
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    private Time timeUntilFinish;
    protected CountDownTimer advCountDownTimer;
    MediaPlayer mediaPlayer;
    int id = 1;

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

        timeUntilFinish = new Time(DetailFragment.advTime*1000);
        DetailFragment.tvTimer.setText(timeUntilFinish.toString());

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("CookAdvisor")
                .setContentText( DetailFragment.title + " in progress")
                .setSmallIcon(Utility.getIconResourceForTitle(DetailFragment.title));

        final int maxTime = DetailFragment.advTime * 1000;
        advCountDownTimer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // выводим оставшееся время в текстовой метке

                timeUntilFinish = new Time(millisUntilFinished);
                DetailFragment.tvTimer.setText(timeUntilFinish.toString());
                Integer inc = (int) (long) millisUntilFinished;
                mBuilder.setProgress(maxTime, maxTime - inc, false)
                         .setContentText(DetailFragment.title + " in progress - " + timeUntilFinish.toString());
                // Displays the progress bar for the first time.
                mNotifyManager.notify(id, mBuilder.build());
            }

            @Override
            public void onFinish() {

                mBuilder.setContentText(DetailFragment.title +" Done!")
                        .setProgress(0,0,false);
                mNotifyManager.notify(id, mBuilder.build());
                DetailFragment.tvTimer.setText("Done!");
                DetailFragment.btnStart.setText(getString(R.string.start));
                mediaPlayer = new  MediaPlayer();
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.done);
                mediaPlayer.start();
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
