package com.vasiachess.cookadvisor;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vasiachess.cookadvisor.data.AdviceContract;

/**
 * Created by Vasiliy on 25.03.2015.
 */
public class EditFragment extends Fragment implements View.OnClickListener {

    private Button btnSave;
    private EditText etTitle;
    private EditText etHour;
    private EditText etMin;
    private EditText etSec;
    private EditText etAdvice;
    private final String LOG_TAG = EditFragment.class.getSimpleName();
    private boolean newItem = true;
    private String title = "";
    private Integer mtime = 0;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";



    public EditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

        btnSave = (Button) rootView.findViewById(R.id.buttonSave);

        etTitle = (EditText) rootView.findViewById(R.id.editTextTitle);
        etHour = (EditText) rootView.findViewById(R.id.editTextHour);
        etMin = (EditText) rootView.findViewById(R.id.editTextMin);
        etSec = (EditText) rootView.findViewById(R.id.editTextSec);
        etAdvice = (EditText) rootView.findViewById(R.id.editTextAdvice);

        Bundle arguments = getArguments();
        if (arguments != null) {

            title = arguments.getString(Utility.TITLE);

            if (!title.equals("")) {
            mtime = arguments.getInt(Utility.TIME);
            String advice = arguments.getString(Utility.ADVICE);

            etTitle.setText(title);

            Integer h = mtime / 3600;
            Integer m = (mtime % 3600) / 60;
            Integer s = mtime % 60;

            etHour.setText(h.toString());
            etMin.setText(m.toString());
            etSec.setText(s.toString());

            etAdvice.setText(advice);

            newItem = false;
            } else {newItem = true;}
        }

        btnSave.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            onClickSave();
        }

    }

    private void onClickSave() {

        int sec = Integer.parseInt(etSec.getText().toString());
        int min = Integer.parseInt(etMin.getText().toString());
        int hour = Integer.parseInt(etHour.getText().toString());

        if (etTitle.getText().toString().equals("") || etAdvice.getText().toString().equals("")){
            Toast toast = Toast.makeText(getActivity(),
                    R.string.hint_data,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if ((sec>60)||(min>60)||(hour>24)) {
            Toast toast = Toast.makeText(getActivity(),
                    R.string.hint_time,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {

        ContentValues adviceValues = new ContentValues();

        adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TITLE, etTitle.getText().toString());

        int cookTime = hour*3600 + min*60 + sec;
            Log.d(LOG_TAG, "hour = "+ hour + "Cook Time = " + cookTime);
        adviceValues.put(AdviceContract.AdviceEntry.COLUMN_TIME, cookTime);

        adviceValues.put(AdviceContract.AdviceEntry.COLUMN_ADVICE, etAdvice.getText().toString());

        if (newItem) {
            Uri ins = getActivity().getContentResolver().insert(AdviceContract.AdviceEntry.CONTENT_URI, adviceValues);
            Log.d(LOG_TAG, "inserted - " + title);

        } else {
            int upd = getActivity().getContentResolver().update(AdviceContract.AdviceEntry.CONTENT_URI, adviceValues, "title = ? and time = ?", new String[] { title, String.valueOf(mtime)});
            Log.d(LOG_TAG, "updated - " + title);
        }

            if (MainActivity.mTwoPane) {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                Bundle arguments = new Bundle();

                arguments.putString(Utility.TITLE, etTitle.getText().toString());
                arguments.putInt(Utility.TIME, cookTime);
                arguments.putString(Utility.ADVICE, etAdvice.getText().toString());

                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(arguments);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.advice_detail_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Utility.TITLE, etTitle.getText().toString());
                intent.putExtra(Utility.TIME, cookTime);
                intent.putExtra(Utility.ADVICE, etAdvice.getText().toString());
                startActivity(intent);
            }
        };
    }


}
