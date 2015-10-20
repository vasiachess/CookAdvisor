package com.vasiachess.cookadvisor;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

/**
 * Created by root on 07.10.15.
 */
public class Application extends android.app.Application{
	private Tracker mTracker;
	public static String sDefSystemLanguage;

	@Override
	public void onCreate() {
		super.onCreate();

		sDefSystemLanguage = Locale.getDefault().getLanguage();
	}

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			mTracker = analytics.newTracker(R.xml.global_tracker);
		}
		return mTracker;
	}

}
