package org.uberthought.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateEntryCount();
        UpdateListView();
    }


    public void addToDatabaseOnClick(View view) {
//        Toast.makeText(MainActivity.this, "You Fool!", Toast.LENGTH_LONG).show();

        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
        SQLiteDatabase db = mainReaderDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        long dateOffset = Calendar.getInstance().getTime().getTime();
        values.put(MainReaderContract.MainEntry.COLUMN_NAME_DATE, dateOffset);

        long newRowId = db.insert(MainReaderContract.MainEntry.TABLE_NAME, null, values);

        UpdateEntryCount();
        UpdateListView();
    }

    void UpdateListView()
    {
        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();

        String[] projection = {
                MainReaderContract.MainEntry._ID,
                MainReaderContract.MainEntry.COLUMN_NAME_DATE
        };

        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
        String[] selectionArgs = { "0" };

        String sortOrder = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                MainReaderContract.MainEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        ListView listView = (ListView)findViewById(R.id.listView);
        EntryCursorAdapter entryCursorAdapter = new EntryCursorAdapter(this, cursor);
        listView.setAdapter(entryCursorAdapter);
    }

    private void UpdateEntryCount() {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(Integer.toString(this.entryCount()));
    }

    public void readFromDatabaseOnClick(View view) {

        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();

        String[] projection = {
                MainReaderContract.MainEntry.COLUMN_NAME_DATE
        };

        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
        String[] selectionArgs = { "0" };

        String sortOrder = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                MainReaderContract.MainEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();
        int count = cursor.getCount();

        while (cursor.moveToNext()) {
            long dateOffset = cursor.getLong(cursor.getColumnIndexOrThrow(MainReaderContract.MainEntry.COLUMN_NAME_DATE));
            Date date = new Date(dateOffset);
            Log.d("MainActivity", date.toString());
        }
    }

    int entryCount() {
        MainReaderDBHelper mainReaderDBHelper = new MainReaderDBHelper(this);
        SQLiteDatabase db = mainReaderDBHelper.getReadableDatabase();

        String[] projection = {
                MainReaderContract.MainEntry.COLUMN_NAME_DATE
        };

        String selection = MainReaderContract.MainEntry.COLUMN_NAME_DATE + " != ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = db.query(
                MainReaderContract.MainEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        return cursor.getCount();
    }
}
