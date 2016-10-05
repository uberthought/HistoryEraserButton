package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MainDBHelper databaseHelper;
    private SimpleRecordListFragment simpleRecordListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleRecordListFragment = (SimpleRecordListFragment) getFragmentManager().findFragmentById(R.id.simplerecordlist_fragment);

        // force a ui update
        onDatabaseChange();
    }


    public void addNewOnClick(View view) {

        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getSimpleRecordDao();

            Date currDateTime = new Date(System.currentTimeMillis());

            simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1", UUID.randomUUID().toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        simpleRecordListFragment.onDatabaseChange();
        onDatabaseChange();
    }

    public void deleteLastOnClick(View view) {
        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getSimpleRecordDao();
            SimpleRecord lastRecord = simpleRecordDao.queryForFirst(simpleRecordDao.queryBuilder().orderBy("_id", false).prepare());
            simpleRecordDao.delete(lastRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        simpleRecordListFragment.onDatabaseChange();
        onDatabaseChange();
    }

    public void onDatabaseChange() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.format(Locale.US, "%d", getDatabaseHelper().getAllSimpleRecordsCount()));
    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        return databaseHelper;
    }
}
