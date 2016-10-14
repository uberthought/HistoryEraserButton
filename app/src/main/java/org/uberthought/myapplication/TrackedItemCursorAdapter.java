package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TrackedItemCursorAdapter extends BaseCursorAdapter {

    TrackedItemCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.trackeditem_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex("name"));

        TextView textView = (TextView) view.findViewById((R.id.textView2));
        textView.setText(name);

        CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) view;
        checkableLinearLayout.setCheckable(isCheckable());
    }
}
