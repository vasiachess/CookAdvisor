package com.vasiachess.cookadvisor;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vasiliy on 19.03.2015.
 */
public class AdviceAdapter extends CursorAdapter {

    public static class ViewHolder {

        public final ImageView iconView;
        public final TextView titleView;
        public final TextView timerView;


        public ViewHolder(View view) {

            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            titleView = (TextView) view.findViewById(R.id.list_item_title_textview);
            timerView = (TextView) view.findViewById(R.id.list_item_timer_textview);

        }
    }

    public AdviceAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
