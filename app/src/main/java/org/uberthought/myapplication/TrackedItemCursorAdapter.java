package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

class TrackedItemCursorAdapter extends BaseCursorAdapter {

    private MainDBHelper databaseHelper;

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
        long id = cursor.getLong(cursor.getColumnIndex("_id"));

        MainDBHelper mainDBHelper = getDatabaseHelper(context);

        int count = 0;
        try {
            count = mainDBHelper.getSimpleRecordCursor(id).getCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView) view.findViewById((R.id.textView2));
        textView.setText(String.format("%s %d", name, count));

        CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout) view;
        checkableLinearLayout.setCheckable(isCheckable());
    }

    private MainDBHelper getDatabaseHelper(Context context) {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(context, MainDBHelper.class);
        return databaseHelper;
    }

}
