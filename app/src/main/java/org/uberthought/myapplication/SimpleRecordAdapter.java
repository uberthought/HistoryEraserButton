package org.uberthought.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class SimpleRecordAdapter extends RecyclerView.Adapter<SimpleRecordAdapter.ViewHolder> {

    private long mTrackedItemId;

    private MainDBHelper mDatabaseHelper;
    private Context mContext;
    private Dao<SimpleRecord, Long> mDao;
    private List<Long> mCheckedIds = new ArrayList<>();

    SimpleRecordAdapter(Context context, long trackedItemId) {
        mContext = context;
        mTrackedItemId = trackedItemId;

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

        return new SimpleRecordAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleRecordAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            SimpleRecord simpleRecord = getSimpleRecord(position);
            String text = simpleRecord.getDate().toString();
            final Long id = simpleRecord.getId();
            boolean checked = mCheckedIds.contains(id);

            TextView textView = (TextView) holder.mView.findViewById((R.id.textView));
            textView.setText(text);

            CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.checkBox);
            checkBox.setChecked(checked);

            holder.mView.setOnLongClickListener(v -> {
                if (mCheckedIds.contains(id)) {
                    mCheckedIds.remove(id);
                    checkBox.setChecked(false);
                } else {
                    mCheckedIds.add(id);
                    checkBox.setChecked(true);
                }

                return true;
            });

            holder.mView.setOnClickListener(v -> {
                if (!mCheckedIds.isEmpty()) {
                    if (mCheckedIds.contains(id)) {
                        mCheckedIds.remove(id);
                        checkBox.setChecked(false);
                    } else {
                        mCheckedIds.add(id);
                        checkBox.setChecked(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SimpleRecord getSimpleRecord(int position) throws SQLException {
        return mDao.queryBuilder()
                .where()
                .eq("trackedItem_id", mTrackedItemId)
                .query()
                .get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        try {
            return (int) mDao.queryBuilder()
                    .where()
                    .eq("trackedItem_id", mTrackedItemId)
                    .countOf();
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

    void clearCheckedItems() {
        mCheckedIds.clear();
    }

    List<Long> getCheckedIds() {
        return mCheckedIds;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }
}
