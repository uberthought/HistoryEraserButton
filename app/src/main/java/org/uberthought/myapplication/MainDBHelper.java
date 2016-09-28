package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class MainDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "MainDB.db";
    private static final int DATABASE_VERSION = 3;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<SimpleRecord, Long> simpleRecordDao;

    public MainDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the mainDB database table
             */
            TableUtils.createTable(connectionSource, SimpleRecord.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource, SimpleRecord.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Dao<SimpleRecord, Long> getDao() throws SQLException {
        if (simpleRecordDao == null) {
            simpleRecordDao = getDao(SimpleRecord.class);
        }
        return simpleRecordDao;
    }

    List<SimpleRecord> getAllSimpleRecords() {
        try {
            return getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    long getAllSimpleRecordsCount() {
        try {
            return getDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    Cursor getSimpleRecordCursor() throws SQLException {
        AndroidDatabaseResults results = (AndroidDatabaseResults) getDao().iterator().getRawResults();
        return results.getRawCursor();
    }
}