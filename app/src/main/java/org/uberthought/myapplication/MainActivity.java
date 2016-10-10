package org.uberthought.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CursorAdapter cursorAdapter;
    private MainDBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TrackedItemListFragment trackedItemListFragment = TrackedItemListFragment.newInstance();
        transaction.replace(R.id.FragmentView, trackedItemListFragment);
        transaction.commit();

        trackedItemListFragment.setRowListener(new TrackedItemListFragment.RowListener() {
            @Override
            public void onRowPressed(Long id) {

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                SimpleRecordListFragment simpleRecordListFragment = SimpleRecordListFragment.newInstance();
                transaction.replace(R.id.FragmentView, simpleRecordListFragment);
                transaction.commit();

//                Toast.makeText(getBaseContext(), "Pressed " + id.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            Cursor cursor = getDatabaseHelper().getTrackedItemCursor();
            cursorAdapter = new TrackedItemCursorAdapter(this, cursor);

            cursor.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                }
            });

            cursorAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();

                    onDatabaseChange();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addNewOnClick(View view) {

        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getSimpleRecordDao();
            Dao<TrackedItem, Long> trackedItemDao = getDatabaseHelper().getTrackedItemDao();

            Date currDateTime = new Date(System.currentTimeMillis());

//            simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1", UUID.randomUUID().toString()));
            trackedItemDao.create(new TrackedItem("New Item"));

            Cursor cursor = getDatabaseHelper().getTrackedItemCursor();
            cursorAdapter.swapCursor(cursor);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLastOnClick(View view) {
        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getSimpleRecordDao();
            SimpleRecord lastRecord = simpleRecordDao.queryForFirst(simpleRecordDao.queryBuilder().orderBy("_id", false).prepare());
            simpleRecordDao.delete(lastRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onDatabaseChange();
    }

    public void onDatabaseChange() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.format(Locale.US, "%d", getDatabaseHelper().getTrackedItemCount()));
    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        return databaseHelper;
    }
}
