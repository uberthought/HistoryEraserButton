package org.uberthought.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by prichardson on 9/11/16.
 */
public final class MainReaderContract {

    private MainReaderContract() {}

    public static class MainEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_DATE = "date";
    }

}
