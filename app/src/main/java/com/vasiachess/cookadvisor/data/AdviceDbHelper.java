package com.vasiachess.cookadvisor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vasiachess.cookadvisor.data.AdviceContract.AdviceEntry;

/**
 * Created by vasiliy on 16.03.2015.
 */
public class AdviceDbHelper extends SQLiteOpenHelper {

    public  AdviceDbHelper (Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public static final String DB_NAME = "db_advices";
    private static final int DATABASE_VERSION = 1;


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_ADVICE_TABLE = "CREATE TABLE " + AdviceEntry.TABLE_NAME + " (" +
                AdviceEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                AdviceEntry.COLUMN_TIME + " INTEGER NOT NULL, " +
                AdviceEntry.COLUMN_ADVICE + " TEXT, " +
                " );";

        db.execSQL(SQL_CREATE_ADVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AdviceEntry.TABLE_NAME);
    }
}
