package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

class SimpleRecordArrayAdapter extends ArrayAdapter<SimpleRecord> {

    SimpleRecordArrayAdapter(Context context, int resource, SimpleRecord[] objects) {
        super(context, resource, objects);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleRecord record = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_cell, parent, false);

        TextView textView = (TextView) convertView.findViewById((R.id.textView2));
        Date date = record.getDateCreated();
        if (date != null)
            textView.setText(date.toString());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
