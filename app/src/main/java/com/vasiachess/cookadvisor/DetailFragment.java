package com.vasiachess.cookadvisor;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.vasiachess.cookadvisor.data.AdviceContract;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    public static Button btnStart;
    private Button btnEdit;
    private Button btnDelete;
    public static TextView tvTitle;
    public ImageView ivIcon;
    public static TextView tvTimer;
    private TextView tvAdvice;
    private final String BUTTON_KEY = "button_state";
    public static String title = "";
    private String advice = "";
    private ShareActionProvider mShareActionProvider;
    private String mAdvice;
    private PendingIntent pIntent;

    boolean bound = false;
    ServiceConnection sConn;
    TimerService timerService;

    public static int advTime = 0;
    private final String LOG_TAG = "MyLog: " + DetailFragment.class.getSimpleName();
    private static final String EDITFRAGMENT_TAG = "EFTAG";
    private static final String ADVICE_SHARE_HASHTAG = " #CookAdvisorApp";



    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, " OnCreateView ");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        btnStart = (Button) rootView.findViewById(R.id.buttonStart);
        btnEdit = (Button) rootView.findViewById(R.id.buttonEdit);
        btnDelete = (Button) rootView.findViewById(R.id.buttonDelete);

        tvTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        ivIcon = (ImageView) rootView.findViewById(R.id.imageViewIcon);
        tvTimer = (TextView) rootView.findViewById(R.id.textViewTimer);
        tvAdvice = (TextView) rootView.findViewById(R.id.textViewAdvice);

        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(Utility.TITLE);
            tvTitle.setText(title);

            ivIcon.setImageResource(Utility.getIconResourceForTitle(title));

            advTime = arguments.getInt(Utility.TIME);
            setTimer();

            advice = arguments.getString(Utility.ADVICE);
            tvAdvice.setText(advice);

            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnStart.setEnabled(true);
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

        return rootView;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (Utility.id == 0) {
			Log.d(LOG_TAG, "Stop service id = " + Utility.id);
			Intent intent = new Intent(getActivity(), TimerService.class);
			getActivity().stopService(intent);
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

	        timerService.startTimer(title, advTime, advice);

        } else {
            btnStart.setText(getString(R.string.start));

            timerService.stopTimer(title);

            setTimer();
        }
    }

    private void onClickEdit() {
        if (MainActivity.mTwoPane) {
            // In two-pane mode, show the edit view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putString(Utility.TITLE, title);
            arguments.putInt(Utility.TIME, advTime);
            arguments.putString(Utility.ADVICE, advice);

            EditFragment fragment = new EditFragment();
            fragment.setArguments(arguments);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.advice_detail_container, fragment, EDITFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtra(Utility.TITLE, title);
            intent.putExtra(Utility.TIME, advTime);
            intent.putExtra(Utility.ADVICE, advice);
            startActivity(intent);
        }


    }

    private void setTimer() {
        tvTitle.setText(title);
        tvTimer.setText(Utility.getTime(advTime));
        tvAdvice.setText(advice);
        ivIcon.setImageResource(Utility.getIconResourceForTitle(title));
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

        mAdvice = String.format("%s - %s - %s", tvTitle.getText(), Utility.getTime(advTime), tvAdvice.getText());

        mShareActionProvider.setShareIntent(createShareAdviceIntent());
    }

    private Intent createShareAdviceIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mAdvice + ADVICE_SHARE_HASHTAG);
        return shareIntent;
    }
}


