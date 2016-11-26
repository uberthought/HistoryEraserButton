package org.uberthought.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SimpleRecordAdapter extends RecyclerView.Adapter<SimpleRecordAdapter.ViewHolder> {

    private MainDBHelper mDatabaseHelper;
    private Context mContext;
    private Dao<SimpleRecord, Long> mDao;
    private List<Long> mCheckedIds = new ArrayList<>();

    SimpleRecordAdapter(Context context) {
        mContext = context;

        try {
            mDao = getDatabaseHelper(mContext).getDao(SimpleRecord.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SimpleRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simplerecord_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        SimpleRecordAdapter.ViewHolder vh = new SimpleRecordAdapter.ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleRecordAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            SimpleRecord simpleRecord = mDao.queryForAll().get((int) position);
            String text = simpleRecord.getDate().toString();
            final Long  id = simpleRecord.getId();
            boolean checked = mCheckedIds.contains(id);

            TextView textView = (TextView) holder.mView.findViewById((R.id.textView2));
            textView.setText(text);

            CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.checkBox);
            checkBox.setChecked(checked);

            holder.mView.setOnLongClickListener(v -> {
                if (mCheckedIds.contains(id)) {
                    mCheckedIds.remove(id);
                    checkBox.setChecked(false);
                }
                else {
                    mCheckedIds.add(id);
                    checkBox.setChecked(true);
                }

                Toast.makeText(mContext, "Long click position: " + position, Toast.LENGTH_SHORT).show();

                return true;
            });

            holder.mView.setOnClickListener(v -> {
                if (!mCheckedIds.isEmpty()) {
                    if (mCheckedIds.contains(id)) {
                        mCheckedIds.remove(id);
                        checkBox.setChecked(false);
                    }
                    else {
                        mCheckedIds.add(id);
                        checkBox.setChecked(true);
                    }
                }

                Toast.makeText(mContext, "Click position: " + position, Toast.LENGTH_SHORT).show();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        try {
            return (int) mDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private MainDBHelper getDatabaseHelper(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = OpenHelperManager.getHelper(context, MainDBHelper.class);
        return mDatabaseHelper;
    }

    public List<Long> getCheckedIds() {
        return mCheckedIds;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }

    /*
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
*/
}
