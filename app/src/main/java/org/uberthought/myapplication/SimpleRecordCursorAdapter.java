package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

class SimpleRecordCursorAdapter extends BaseCursorAdapter {

    SimpleRecordCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.entry_cell, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Long dateOffset = cursor.getLong(cursor.getColumnIndex("dateOffset"));
        Date date = new Date(dateOffset);

        TextView textView = (TextView) view.findViewById((R.id.textView2));
        textView.setText(date.toString());

        CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) view;
        checkableLinearLayout.setCheckable(isCheckable());
    }

}
