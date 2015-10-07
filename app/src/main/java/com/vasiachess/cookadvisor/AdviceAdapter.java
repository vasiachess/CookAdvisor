package com.vasiachess.cookadvisor;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vasiliy on 19.03.2015.
 */
public class AdviceAdapter extends CursorAdapter {

    private Context mContext;

    public static class ViewHolder {

        public final ImageView iconView;
        public final TextView titleView;
        public final TextView timerView;
        public final TextView progressView;


        public ViewHolder(View view) {

            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            titleView = (TextView) view.findViewById(R.id.list_item_title_textview);
            timerView = (TextView) view.findViewById(R.id.list_item_timer_textview);
            progressView = (TextView) view.findViewById(R.id.list_item_progress_textview);
        }
    }

    public AdviceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_advice, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title = cursor.getString(MainFragment.COL_TITLE);
        viewHolder.titleView.setText(title);
        viewHolder.iconView.setImageResource(Utility.getIconResourceForTitle(context, title));

        if (Utility.timers.containsKey(title)) {
            viewHolder.timerView.setText(Utility.getTime(Utility.timers.get(title)));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_grey));
            viewHolder.progressView.setVisibility(View.VISIBLE);
        } else {
	        Integer timer = cursor.getInt(MainFragment.COL_TIME);
            viewHolder.timerView.setText(Utility.getTime(timer));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_white));
            viewHolder.progressView.setVisibility(View.GONE);
        }
    }
}
