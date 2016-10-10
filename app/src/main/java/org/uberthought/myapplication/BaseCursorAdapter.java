package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

abstract class BaseCursorAdapter extends CursorAdapter {
    private boolean mIsCheckable;

    BaseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
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
