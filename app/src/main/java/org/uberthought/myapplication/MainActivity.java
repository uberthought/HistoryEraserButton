package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MainDBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();

                    cursor.move(i);
                    Long id = cursor.getLong(cursor.getColumnIndex("_id"));

                    Dao<SimpleRecord, Long> dao = getDatabaseHelper().getDao();
                    dao.deleteById(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                UpdateEntryCount();
                UpdateListView();

            }
        });

        UpdateEntryCount();
        UpdateListView();
    }


    public void addNewOnClick(View view) {

        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getDao();

            Date currDateTime = new Date(System.currentTimeMillis());

            simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UpdateEntryCount();
        UpdateListView();
    }

    public void deleteLastOnClick(View view) {
        try {
            Dao<SimpleRecord, Long> simpleRecordDao = getDatabaseHelper().getDao();
            SimpleRecord lastRecord = simpleRecordDao.queryForFirst(simpleRecordDao.queryBuilder().orderBy("_id", false).prepare());
            simpleRecordDao.delete(lastRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UpdateEntryCount();
        UpdateListView();
    }

    void UpdateListView()
    {
        try {
            Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();

            ListView listView = (ListView)findViewById(R.id.listView);
            SimpleRecordCursorAdapter adapter = new SimpleRecordCursorAdapter(this, cursor);
            listView.setAdapter(adapter);
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
            List<SimpleRecord> simpleRecords = getDatabaseHelper().getAllSimpleRecords();
            return simpleRecords.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
        return databaseHelper;
    }
}
