package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by prichardson on 9/11/16.
 */
public class EntryCursorAdapter extends CursorAdapter {
    public EntryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.entry_cell, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById((R.id.textView2));
        long dateOffset = cursor.getLong(cursor.getColumnIndex(MainReaderContract.MainEntry.COLUMN_NAME_DATE));
        Date date = new Date(dateOffset);
        textView.setText(date.toString());
    }
}
