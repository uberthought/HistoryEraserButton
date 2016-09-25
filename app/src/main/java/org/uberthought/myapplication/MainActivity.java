package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateEntryCount();
        UpdateListView();
    }


    public void addToDatabaseOnClick(View view) {

        try {
            MainDBHelper todoOpenDatabaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
            Dao<SimpleRecord, Long> simpleRecordDao = todoOpenDatabaseHelper.getDao();

            Date currDateTime = new Date(System.currentTimeMillis());
            simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UpdateEntryCount();
        UpdateListView();
    }

    public void readFromDatabaseOnClick(View view) {
    }

    void UpdateListView()
    {
        try {
            List<SimpleRecord> simpleRecords = getAllSimpleRecords();
            SimpleRecord[] simpleRecordArray = simpleRecords.toArray(new SimpleRecord[simpleRecords.size()]);

            ListView listView = (ListView)findViewById(R.id.listView);
            SimpleRecordArrayAdapter entryCursorAdapter = new SimpleRecordArrayAdapter(this, R.layout.entry_cell, simpleRecordArray);
            listView.setAdapter(entryCursorAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void UpdateEntryCount() {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(String.format(Locale.US, "%d", this.entryCount()));
    }

    int entryCount() {
        try {
            List<SimpleRecord> simpleRecords = getAllSimpleRecords();
            return simpleRecords.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<SimpleRecord> getAllSimpleRecords() throws SQLException {
        MainDBHelper todoOpenDatabaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        Dao<SimpleRecord, Long> simpleRecordDao = todoOpenDatabaseHelper.getDao();

        // read
        return simpleRecordDao.queryForAll();
    }
}
