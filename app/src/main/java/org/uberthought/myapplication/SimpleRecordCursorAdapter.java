package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

class SimpleRecordCursorAdapter extends CursorAdapter {

    private boolean mIsCheckable;

    SimpleRecordCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
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

    @Override
    public long getItemId(int position) {
        int count = getCount();
        if (position >= count)
            return 0;
        Cursor cursor = (Cursor) getItem(position);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    boolean isCheckable() {
        return mIsCheckable;
    }

    void setIsCheckable(boolean isCheckable) {
        this.mIsCheckable = isCheckable;
    }
}
