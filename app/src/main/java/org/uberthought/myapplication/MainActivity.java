package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CursorAdapter cursorAdapter;
    private MainDBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TrackedItemListFragment trackedItemListFragment = TrackedItemListFragment.newInstance();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentView, trackedItemListFragment)
                .commit();

        trackedItemListFragment.setRowListener(new TrackedItemListFragment.RowListener() {
            @Override
            public void onRowPressed(Long id) {

                SimpleRecordListFragment simpleRecordListFragment = SimpleRecordListFragment.newInstance();

                simpleRecordListFragment.Bind(id);

                getFragmentManager().beginTransaction()
                        .replace(R.id.FragmentView, simpleRecordListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        try {
            Cursor cursor = getDatabaseHelper().getCursor(TrackedItem.class);
            cursorAdapter = new TrackedItemCursorAdapter(this, cursor);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getDatabaseHelper().addDBListener(new MainDBHelper.DBChangedListener() {
            @Override
            public void dbChanged() {
                onDatabaseChange();
            }
        });

        onDatabaseChange();
    }


    public void deleteLastOnClick(View view) {
        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getDao(SimpleRecord.class);
            SimpleRecord lastRecord = simpleRecordDao.queryForFirst(simpleRecordDao.queryBuilder().orderBy("_id", false).prepare());
            simpleRecordDao.delete(lastRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onDatabaseChange();
    }

    public void onDatabaseChange() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.format(Locale.US, "%d", getDatabaseHelper().getCount(TrackedItem.class)));
    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        return databaseHelper;
    }
}
