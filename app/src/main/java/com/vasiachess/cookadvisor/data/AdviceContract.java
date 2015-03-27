package com.vasiachess.cookadvisor.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class AdviceContract {

    public static final String CONTENT_AUTHORITY = "com.vasiachess.cookadvisor";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ADVICE = "advice";

    public static final class AdviceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADVICE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADVICE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADVICE;

        public static final String TABLE_NAME = "advices";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ADVICE = "advice_text";

        public static Uri buildAdviceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAdviceWithTitle(String title) {

            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_TITLE, title).build();
        }
    }

}
