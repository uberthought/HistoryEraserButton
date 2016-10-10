package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.UUID;

class MainDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "MainDB.db";
    private static final int DATABASE_VERSION = 5;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<SimpleRecord, Long> simpleRecordDao;
    private Dao<TrackedItem, Long> trackedItemDao;

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
            TableUtils.createTable(connectionSource, TrackedItem.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        /**
         * Recreates the database when onUpgrade is called by the framework
         */
        try {
            TableUtils.dropTable(connectionSource, SimpleRecord.class, true);
            TableUtils.dropTable(connectionSource, TrackedItem.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onCreate(database, connectionSource);
    }


    Dao<SimpleRecord, Long> getSimpleRecordDao() throws SQLException {
        if (simpleRecordDao == null) {
            simpleRecordDao = getDao(SimpleRecord.class);
        }
        return simpleRecordDao;
    }

    Dao<TrackedItem, Long> getTrackedItemDao() throws SQLException {
        if (trackedItemDao == null) {
            trackedItemDao = getDao(TrackedItem.class);
        }
        return trackedItemDao;
    }

    long getAllSimpleRecordsCount() {
        try {
            return getSimpleRecordDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    long getTrackedItemCount() {
        try {
            return getTrackedItemDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    Cursor getSimpleRecordCursor() throws SQLException {
        AndroidDatabaseResults results = (AndroidDatabaseResults) getSimpleRecordDao().iterator().getRawResults();
        return results.getRawCursor();
    }

    Cursor getTrackedItemCursor() throws SQLException {
        AndroidDatabaseResults results = (AndroidDatabaseResults) getTrackedItemDao().iterator().getRawResults();
        return results.getRawCursor();
    }

    Cursor getSimpleRecordCursor(UUID trackedItemUuid) throws SQLException {
        QueryBuilder<SimpleRecord, Long> queryBuilder = getSimpleRecordDao().queryBuilder();
        queryBuilder.where().eq("trackedItemUuid", trackedItemUuid.toString());
        CloseableIterator<SimpleRecord> closeableIterable = getSimpleRecordDao().iterator(queryBuilder.prepare());
        AndroidDatabaseResults results = (AndroidDatabaseResults) closeableIterable.getRawResults();
        return results.getRawCursor();
    }
}