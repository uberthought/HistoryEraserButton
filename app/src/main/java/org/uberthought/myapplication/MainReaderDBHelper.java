package org.uberthought.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prichardson on 9/11/16.
 */
public class MainReaderDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MainReader.db";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MainReaderContract.MainEntry.TABLE_NAME + " (" +
                    MainReaderContract.MainEntry._ID + " INTEGER PRIMARY KEY," +
                    MainReaderContract.MainEntry.COLUMN_NAME_DATE + " INTEGER )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MainReaderContract.MainEntry.TABLE_NAME;

    public MainReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
