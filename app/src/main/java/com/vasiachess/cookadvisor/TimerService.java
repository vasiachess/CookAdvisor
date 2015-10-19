package com.vasiachess.cookadvisor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vasiliy on 27.03.2015.
 */
public class TimerService extends Service {

    private final String LOG_TAG = "MyLog: " + TimerService.class.getSimpleName();
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    protected CountDownTimer advCountDownTimer;
    MediaPlayer mediaPlayer;
    private int currentId = 0;
    private int id = 0;
    private ArrayList<CountDownTimer> countDownTimers = new ArrayList<>();
    private String[] titles = new String[20];
    private HashMap<String, Integer> timerIds = new HashMap<>();

    MyBinder binder = new MyBinder();

    public void onCreate() {
        super.onCreate();
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onDestroy() {
        super.onDestroy();
        if(advCountDownTimer != null) {
            advCountDownTimer.cancel();
        }
    }

    class MyBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

	public void stopTimer(String timerName) {
		currentId = timerIds.get(timerName);
		countDownTimers.get(currentId).cancel();
		mNotifyManager.cancel(timerName, currentId);
		Utility.id--;
	}

	public void startTimer(String timerName, final int time, final String advice) {
		Utility.id++;
		titles[id] = timerName;
		timerIds.put(timerName, id);

		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("CookAdvisor")
				.setSmallIcon(Utility.getIconResourceForTitle(getApplicationContext(), timerName))
				.setContentText(timerName + " in progress");

		final int maxTime = time * 1000;
		final int tickId = id;
		advCountDownTimer = new CountDownTimer(maxTime, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

				int timeUntilFinish = (int) millisUntilFinished/1000;

				Utility.setItemCurrentTime(titles[tickId], timeUntilFinish);

				Intent intent = new Intent(Utility.BROADCAST_ACTION).putExtra(Utility.TITLE, titles[tickId])
						.putExtra(Utility.TIME_UNTIL_FINISH, timeUntilFinish)
						.putExtra(Utility.TIME, time)
						.putExtra(Utility.ADVICE, advice);
				sendBroadcast(intent);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap icon = BitmapFactory.decodeResource(getResources(), Utility.getIconResourceForTitle(getApplicationContext(), titles[tickId]), options);

				Integer inc = (int) (long) millisUntilFinished;

				Intent activity = new Intent(TimerService.this, DetailActivity.class).putExtra(Utility.TITLE, titles[tickId])
						.putExtra(Utility.TIME, time)
						.putExtra(Utility.ADVICE, advice);

				int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

				PendingIntent contentIntent = PendingIntent.getActivity(TimerService.this, uniqueInt, activity, PendingIntent.FLAG_ONE_SHOT);

				mBuilder.setProgress(maxTime, maxTime - inc, false)
						.setContentIntent(contentIntent)
						.setLargeIcon(icon)
						.setVibrate(null)
						.setSmallIcon(Utility.getIconResourceForTitle(getApplicationContext(), titles[tickId]))
						.setContentText(titles[tickId] + " " + getApplicationContext().getResources().getString(R.string.is_preparing) + " - " + Utility.getTime(timeUntilFinish));

				mNotifyManager.notify(titles[tickId], tickId, mBuilder.build());
			}

			@Override
			public void onFinish() {

				Utility.setItemCurrentTime(titles[tickId], 0);

				mediaPlayer = new  MediaPlayer();
				mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.done);
				mediaPlayer.start();

				Intent intent = new Intent(Utility.BROADCAST_ACTION).putExtra(Utility.TITLE, titles[tickId])
						.putExtra(Utility.TIME_UNTIL_FINISH, 0)
						.putExtra(Utility.TIME, time)
						.putExtra(Utility.ADVICE, advice);
				sendBroadcast(intent);

				Intent activity = new Intent(TimerService.this, DetailActivity.class).putExtra(Utility.TITLE, titles[tickId])
								.putExtra(Utility.TIME, 0)
								.putExtra(Utility.ADVICE, advice);

				int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

				PendingIntent contentIntent = PendingIntent.getActivity(TimerService.this, uniqueInt, activity, PendingIntent.FLAG_ONE_SHOT);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap icon = BitmapFactory.decodeResource(getResources(), Utility.getIconResourceForTitle(getApplicationContext(), titles[tickId]), options);

				mBuilder.setContentText(titles[tickId] + " " + getApplicationContext().getResources().getString(R.string.done))
						.setContentIntent(contentIntent)
						.setLargeIcon(icon)
						.setSmallIcon(Utility.getIconResourceForTitle(getApplicationContext(), titles[tickId]))
						.setVibrate(new long[]{100, 1500})
						.setProgress(0, 0, false);
				mNotifyManager.notify(titles[tickId], tickId, mBuilder.build());

				Utility.id--;
				Utility.removeCurrentTimer(titles[tickId]);
			}
		};

		advCountDownTimer.start();
		countDownTimers.add(advCountDownTimer);
		id++;
	}
}
