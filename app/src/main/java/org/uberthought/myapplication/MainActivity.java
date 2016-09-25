package org.uberthought.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

            // create
            List<SimpleRecord> simpleRecords;
            Date currDateTime = new Date(System.currentTimeMillis());
            simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1"));

            // read
            simpleRecords = simpleRecordDao.queryForAll();
            Toast.makeText(MainActivity.this, "Count: " + Integer.toString(simpleRecords.size()), Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
//        SQLiteDatabase db = mainReaderDBHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        long dateOffset = Calendar.getInstance().getTime().getTime();
//        values.put(MainReaderContract.MainEntry.COLUMN_NAME_DATE, dateOffset);
//
//        long newRowId = db.insert(MainReaderContract.MainEntry.TABLE_NAME, null, values);
//
//        UpdateEntryCount();
//        UpdateListView();
    }

    public void readFromDatabaseOnClick(View view) {

//        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
//        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();
//
//        String[] projection = {
//                MainReaderContract.MainEntry.COLUMN_NAME_DATE
//        };
//
//        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
//        String[] selectionArgs = { "0" };
//
//        String sortOrder = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " DESC";
//
//        Cursor cursor = db.query(
//                MainReaderContract.MainEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder);
//
//        cursor.moveToFirst();
//        int count = cursor.getCount();
//
//        while (cursor.moveToNext()) {
//            long dateOffset = cursor.getLong(cursor.getColumnIndexOrThrow(MainReaderContract.MainEntry.COLUMN_NAME_DATE));
//            Date date = new Date(dateOffset);
//            Log.d("MainActivity", date.toString());
//        }
    }

    void UpdateListView()
    {
        try {
            MainDBHelper todoOpenDatabaseHelper = OpenHelperManager.getHelper(this, MainDBHelper.class);
            Dao<SimpleRecord, Long> simpleRecordDao = todoOpenDatabaseHelper.getDao();

            // create
            List<SimpleRecord> simpleRecordList;
            Date currDateTime = new Date(System.currentTimeMillis());
                simpleRecordDao.create(new SimpleRecord(currDateTime, "Note 1"));

            // read
            simpleRecordList = simpleRecordDao.queryForAll();
            SimpleRecord[] simpleRecordArray = simpleRecordList.toArray(new SimpleRecord[simpleRecordList.size()]);
            Toast.makeText(MainActivity.this, "Count: " + Integer.toString(simpleRecordList.size()), Toast.LENGTH_LONG).show();


        ListView listView = (ListView)findViewById(R.id.listView);
        SimpleRecordArrayAdapter entryCursorAdapter = new SimpleRecordArrayAdapter(this, R.layout.entry_cell, simpleRecordArray);
        listView.setAdapter(entryCursorAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
//        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();
//
//        String[] projection = {
//                MainReaderContract.MainEntry._ID,
//                MainReaderContract.MainEntry.COLUMN_NAME_DATE
//        };
//
//        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
//        String[] selectionArgs = { "0" };
//
//        String sortOrder = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " DESC";
//
//        Cursor cursor = db.query(
//                MainReaderContract.MainEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder);
//
//        ListView listView = (ListView)findViewById(R.id.listView);
//        SimpleRecordArrayAdapter entryCursorAdapter = new SimpleRecordArrayAdapter(this, cursor);
//        listView.setAdapter(entryCursorAdapter);
    }

    private void UpdateEntryCount() {
//        TextView textView = (TextView)findViewById(R.id.textView);
//        textView.setText(Integer.toString(this.entryCount()));
    }

    int entryCount() {
        return 0;
//        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
//        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();
//
//        String[] projection = {
//                MainReaderContract.MainEntry.COLUMN_NAME_DATE
//        };
//
//        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
//        String[] selectionArgs = { "0" };
//
//        Cursor cursor = db.query(
//                MainReaderContract.MainEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null);
//
//        return cursor.getCount();
    }
}
