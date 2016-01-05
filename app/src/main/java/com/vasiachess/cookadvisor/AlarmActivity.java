package com.vasiachess.cookadvisor;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by root on 31.12.15.
 */
public class AlarmActivity extends Activity implements View.OnClickListener {

	private TextView tvDone;
	private ImageView ivDish;
	private ImageView ivChief;
	private RelativeLayout rlMain;
	private String title = "";
	private int tickId;
	private int time;
	private String advice;
	private boolean rotated;
	MediaPlayer mediaPlayer;
	NotificationManager mNotifyManager;
	NotificationCompat.Builder mBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		if (savedInstanceState != null && savedInstanceState.containsKey(Utility.TITLE)) {
			title = savedInstanceState.getString(Utility.TITLE);
			rotated = true;
		} else {
			title = getIntent().getExtras().getString(Utility.TITLE);
			advice = getIntent().getExtras().getString(Utility.ADVICE);
			time = getIntent().getExtras().getInt(Utility.TIME);
			tickId = getIntent().getExtras().getInt(Utility.TICK_ID);
		}

		tvDone = (TextView) findViewById(R.id.tv_done_AA);
		ivDish = (ImageView) findViewById(R.id.iv_dish_AA);
		ivChief = (ImageView) findViewById(R.id.iv_chief_AA);
		rlMain = (RelativeLayout) findViewById(R.id.rl_alarm_AA);

		tvDone.setText(title + " - " + getString(R.string.done));
		ivDish.setImageResource(Utility.getIconResourceForTitle(getApplicationContext(), title));

		mediaPlayer = new MediaPlayer();
		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.done);

		if (!rotated) {

			mediaPlayer.start();

			int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

			Intent activity = new Intent(AlarmActivity.this, DetailActivity.class).putExtra(Utility.TITLE, title)
					.putExtra(Utility.TIME, time)
					.putExtra(Utility.ADVICE, advice);

			PendingIntent contentIntent = PendingIntent.getActivity(AlarmActivity.this, uniqueInt, activity, PendingIntent.FLAG_ONE_SHOT);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap icon = BitmapFactory.decodeResource(getResources(), Utility.getIconResourceForTitle(getApplicationContext(), title), options);

			mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mBuilder = new NotificationCompat.Builder(this);
			mBuilder.setContentText(title + " - " + getString(R.string.done))
					.setContentIntent(contentIntent)
					.setLargeIcon(icon)
					.setSmallIcon(Utility.getIconResourceForTitle(getApplicationContext(), title))
					.setVibrate(new long[]{100, 1500})
					.setProgress(0, 0, false);
			mNotifyManager.notify(title, tickId, mBuilder.build());
		}

		tvDone.setOnClickListener(this);
		ivDish.setOnClickListener(this);
		ivChief.setOnClickListener(this);
		rlMain.setOnClickListener(this);
	}

	@Override public void onClick(View view) {
			mediaPlayer.stop();
			finish();
	}

	@Override protected void onSaveInstanceState(Bundle outState) {

		outState.putString(Utility.TITLE, title);
		super.onSaveInstanceState(outState);
	}
}
