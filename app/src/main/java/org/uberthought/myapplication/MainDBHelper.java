package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class MainDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "MainDB.db";
    private static final int DATABASE_VERSION = 6;
    private static Vector<DBChangedListener> mDBChangedListeners = new Vector<>();

    private List<Object> daoList = new ArrayList<>();

    public MainDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    void addDBListener(DBChangedListener listener) {
        mDBChangedListeners.add(listener);
    }

    void removeDBListener(DBChangedListener listener) {
        mDBChangedListeners.remove(listener);
    }

    void OnDBChanged() {
        for (DBChangedListener listener : mDBChangedListeners) {
            listener.dbChanged();
        }
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

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        D castDao;
        Dao<T, ?> dao = null;

        for (Object cachedDao : daoList) {
            if (cachedDao.getClass() == clazz)
                dao = (Dao<T, ?>) cachedDao;
        }
        // special reflection fu is now handled internally by create dao calling the database type
        if (dao == null)
            dao = DaoManager.createDao(getConnectionSource(), clazz);
        castDao = (D) dao;
        return castDao;
    }


    <T> T create(Class<T> clazz) {
        T result = null;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (result == null)
            return null;

        try {
            Dao<T, ?> dao = getDao(clazz);
            dao.create(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    <T> void deleteItems(Class<T> clazz, long[] itemIds) {

        List<Long> itemList = new ArrayList<>();
        for (long itemId : itemIds)
            itemList.add(itemId);

        try {

            // delete them from the database
            Dao<T, Long> dao = getDao(clazz);
            dao.deleteIds(itemList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    <T> long getCount(Class<T> clazz) {
        try {
            return getDao(clazz).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    <T> void update(T data) {
        try {
            Dao<T, ?> dao = getDao((Class<T>) data.getClass());
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Cursor getSimpleRecordCursor(long trackedItemId) throws SQLException {
        AndroidDatabaseResults results = (AndroidDatabaseResults) getDao(SimpleRecord.class)
                .queryBuilder()
                .where()
                .eq("trackedItem_id", trackedItemId)
                .iterator()
                .getRawResults();
        return results.getRawCursor();
    }

    <T> Cursor getCursor(Class<T> clazz) throws SQLException {
        AndroidDatabaseResults results = (AndroidDatabaseResults) getDao(clazz).iterator().getRawResults();
        return results.getRawCursor();
    }

    interface DBChangedListener {
        void dbChanged();
    }

}