package com.vasiachess.cookadvisor;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vasiachess.cookadvisor.data.AdviceContract;
import com.startad.lib.SADView;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static Button btnStart;
    private ImageView btnEdit;
    private ImageView btnDelete;
    private NumberProgressBar progressBar;
	private TextView tvTitle;
    public ImageView ivIcon;
	private TextView tvTimer;
    private TextView tvAdvice;
    private final String BUTTON_KEY = "button_state";
	private String mTitle = "";
    private String mAdvice = "";
    private ShareActionProvider mShareActionProvider;
    private String mShareAdvice;
    private BroadcastReceiver br;
    boolean bound = false;
    ServiceConnection sConn;
    TimerService timerService;
    private Tracker mTracker;
	protected SADView sadView;

    public static int advTime = 0;
    private final String LOG_TAG = "MyLog: " + DetailFragment.class.getSimpleName();
    private static final String EDITFRAGMENT_TAG = "EFTAG";
    private static final String ADVICE_SHARE_HASHTAG = " #CookAdvisorApp";



    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the shared Tracker instance.
        Application application = (Application) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, " OnCreateView ");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        btnStart = (Button) rootView.findViewById(R.id.buttonStart);
        btnEdit = (ImageView) rootView.findViewById(R.id.buttonEdit);
        btnDelete = (ImageView) rootView.findViewById(R.id.buttonDelete);
	    progressBar = (NumberProgressBar) rootView.findViewById(R.id.progressBar);

        tvTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        ivIcon = (ImageView) rootView.findViewById(R.id.imageViewIcon);
        tvTimer = (TextView) rootView.findViewById(R.id.textViewTimer);
        tvAdvice = (TextView) rootView.findViewById(R.id.textViewAdvice);
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);

        Bundle arguments = getArguments();
        if (arguments != null) {
	        btnEdit.setEnabled(true);
	        btnDelete.setEnabled(true);
	        btnStart.setEnabled(true);

	        mTitle = arguments.getString(Utility.TITLE);
	        advTime = arguments.getInt(Utility.TIME);

	        int time;
	        if (Utility.timers.containsKey(mTitle)) {
		        time = Utility.timers.get(mTitle);
		        btnStart.setText(getString(R.string.reset_time));
		        setProgressBar(advTime, time);
	        } else {
		        time = advTime;
	        }
	        mAdvice = arguments.getString(Utility.ADVICE);
	        setTimer(mTitle, time, mAdvice);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(BUTTON_KEY)) {
            // The listview probably hasn't even been populated yet. Actually perform the
            // swapout in onLoadFinished.
            btnStart.setText(savedInstanceState.getString(BUTTON_KEY));
        }

        btnStart.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

	    sConn = new ServiceConnection() {
		    public void onServiceConnected(ComponentName name, IBinder binder) {
			    Log.d(LOG_TAG, "onServiceConnected");
			    timerService = ((TimerService.MyBinder) binder).getService();
			    bound = true;
		    }

		    public void onServiceDisconnected(ComponentName name) {
			    Log.d(LOG_TAG, "onServiceDisconnected");
			    bound = false;
		    }
	    };

	    getActivity().bindService(new Intent(getActivity(), TimerService.class), sConn, 0);

	    if (Utility.id == 0) {
		    Log.d(LOG_TAG, "Start service id = " + Utility.id);
		    Intent intent = new Intent(getActivity(), TimerService.class);
		    getActivity().startService(intent);
	    }

        br = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra(Utility.TITLE);
                int time = intent.getIntExtra(Utility.TIME, 0);
                int timeUntilFinish = intent.getIntExtra(Utility.TIME_UNTIL_FINISH, 0);

	            if (title.equals(tvTitle.getText().toString())) {

		            setProgressBar(time, timeUntilFinish);

                    if (timeUntilFinish == 0) {
	                    setTimer(mTitle, advTime, mAdvice);
                        btnStart.setText(getString(R.string.start));

                        View customDialog = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_custom, null);
                        Utility.showDoneDialog(getActivity(), customDialog, mTitle);

                    } else {
                        tvTimer.setText(Utility.getTime(timeUntilFinish));
                        btnStart.setText(getString(R.string.reset_time));
                    }
	            }
            }
        };

//        Don't use while testing
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

	    // StartAd Create the adView
	    this.sadView = new SADView(getActivity(), Utility.APPLICATION_ID);

	    // Lookup your LinearLayout assuming it's been given
	    // the attribute android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.startAdLayout);

	    // Add the adView to it
	    layout.addView(this.sadView);

	    //Load ad for currently active language in app
	    if (Application.sDefSystemLanguage.toLowerCase().contains("ru")||
			    Application.sDefSystemLanguage.toLowerCase().contains("uk")) {
		    this.sadView.loadAd(SADView.LANGUAGE_RU);
	    } else {
		    this.sadView.loadAd(SADView.LANGUAGE_EN);
	    }

        return rootView;
    }

	@Override
	public void onResume() {
		super.onResume();

		if (!Utility.timers.containsKey(mTitle)) {
			setTimer(mTitle, advTime, mAdvice);
			btnStart.setText(getString(R.string.start));
			progressBar.setVisibility(View.GONE);
		}

        // create filter for BroadcastReceiver
		IntentFilter intFilt = new IntentFilter(Utility.BROADCAST_ACTION);
        // register BroadcastReceiver
		getActivity().registerReceiver(br, intFilt);

        Log.i("GoogleAnalytics", "Setting screen name: " + getClass().getName());
        mTracker.setScreenName("Image~" + getClass().getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(br);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

        getActivity().unbindService(sConn);

        if (Utility.id == 0) {
			Intent intent = new Intent(getActivity(), TimerService.class);
			getActivity().stopService(intent);
		}

		if (this.sadView != null) {
			this.sadView.destroy();
		}
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

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Delete Advice")
                .build());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getResources().getString(R.string.Delete) + " " + mTitle + "?");
        builder.setCancelable(false);
        builder.setPositiveButton(getActivity().getResources().getString(R.string.Delete),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        int del = getActivity().getContentResolver().delete(AdviceContract.AdviceEntry.CONTENT_URI, "title = ? and time = ?", new String[]{mTitle, String.valueOf(advTime)});
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(getActivity().getResources().getString(R.string.Cancel),
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

        if (btnStart.getText().toString().equals(getString(R.string.start))) {
	        btnStart.setText(getString(R.string.reset_time));
	        timerService.startTimer(mTitle, advTime, mAdvice);
        } else {
            btnStart.setText(getString(R.string.start));
	        progressBar.setVisibility(View.INVISIBLE);
            timerService.stopTimer(mTitle);
	        Utility.removeCurrentTimer(mTitle);
            setTimer(mTitle, advTime, mAdvice);
            MainFragment.adviceAdapter.notifyDataSetChanged();
        }

        mTracker.send(new HitBuilders.EventBuilder()
		        .setCategory("Action")
		        .setAction("Start timer")
		        .build());
    }

    private void onClickEdit() {

        mTracker.send(new HitBuilders.EventBuilder()
		        .setCategory("Action")
		        .setAction("Edit advice")
		        .build());

        if (Utility.twoPane) {
            // In two-pane mode, show the edit view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(Utility.TITLE, mTitle);
            arguments.putInt(Utility.TIME, advTime);
            arguments.putString(Utility.ADVICE, mAdvice);

            EditFragment fragment = new EditFragment();
            fragment.setArguments(arguments);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment, EDITFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtra(Utility.TITLE,mTitle);
            intent.putExtra(Utility.TIME, advTime);
            intent.putExtra(Utility.ADVICE, mAdvice);
            startActivity(intent);
        }
    }

    private void setTimer(String title, int time, String advice) {
        tvTitle.setText(title);
        tvTimer.setText(Utility.getTime(time));
        tvAdvice.setText(advice);
        ivIcon.setImageResource(Utility.getIconResourceForTitle(getActivity(), title));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected button state needs to be saved.

            outState.putString(BUTTON_KEY, btnStart.getText().toString());
	    super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
// Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);
// Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
// Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareAdvice = String.format("%s - %s - %s", tvTitle.getText(), Utility.getTime(advTime), tvAdvice.getText());

        mShareActionProvider.setShareIntent(createShareAdviceIntent());
    }

    private Intent createShareAdviceIntent() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share Advice")
                .build());

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareAdvice + ADVICE_SHARE_HASHTAG);
        return shareIntent;
    }

	private void setProgressBar(int maxTime, int currentTime) {
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setMax(maxTime);
		progressBar.setProgress(maxTime-currentTime);
	}
}


