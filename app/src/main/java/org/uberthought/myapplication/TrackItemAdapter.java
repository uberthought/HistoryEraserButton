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
import java.util.Locale;

class TrackItemAdapter extends RecyclerView.Adapter<TrackItemAdapter.ViewHolder> {

    private RecyclerItemClickListener onClickListener;
    private MainDBHelper mDatabaseHelper;
    private Context mContext;
    private Dao<TrackedItem, Long> mDao;
    private List<Long> mCheckedIds = new ArrayList<>();

    TrackItemAdapter(Context context) {
        mContext = context;

        try {
            mDao = getDatabaseHelper(mContext).getDao(TrackedItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trackeditem_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            TrackedItem trackedItem = mDao.queryForAll().get(position);
            String name = trackedItem.getName();
            int count = trackedItem.getSimpleRecords().size();
            final Long  id = trackedItem.getId();
            boolean checked = mCheckedIds.contains(id);

            TextView textView = (TextView) holder.mView.findViewById((R.id.textView2));
            textView.setText(String.format(Locale.getDefault(), "%s %d", name, count));

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
                } else if (onClickListener != null)
                    onClickListener.onClick(position);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void clearCheckedItems() {
        mCheckedIds.clear();
    }

    void SetOnItemTouchListener(RecyclerItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

    List<Long> getCheckedIds() {
        return mCheckedIds;
    }

    @Override
    public long getItemId(int position) {
        try {
            TrackedItem trackedItem = mDao.queryForAll().get(position);
            return trackedItem.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    interface RecyclerItemClickListener {
        void onClick(int position);
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
