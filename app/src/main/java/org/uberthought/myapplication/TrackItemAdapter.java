package org.uberthought.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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
import java.util.List;

public class TrackItemAdapter extends RecyclerView.Adapter<TrackItemAdapter.ViewHolder> {

    private MainDBHelper mDatabaseHelper;
    private Context mContext;
    private Dao<TrackedItem, Long> mDao;
    private List<Integer> mCheckedItems = new ArrayList<>();

    public TrackItemAdapter(Context context) {
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

        ViewHolder vh = new ViewHolder(view, this);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Integer positionObject = new Integer(position);

        String name = "";
        int count = 0;
        boolean checked = false;
        try {
            TrackedItem trackedItem = mDao.queryForAll().get((int) position);
            name = trackedItem.getName();
            count = trackedItem.getSimpleRecords().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        checked = mCheckedItems.contains(positionObject);

        TextView textView = (TextView) holder.mView.findViewById((R.id.textView2));
        textView.setText(String.format("%s %d", name, count));

        CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.checkBox);
        checkBox.setChecked(checked);

        holder.mView.setOnLongClickListener(v -> {
            if (mCheckedItems.contains(positionObject)) {
                mCheckedItems.remove(positionObject);
                checkBox.setChecked(false);
            }
            else {
                mCheckedItems.add(positionObject);
                checkBox.setChecked(true);
            }


            Toast.makeText(mContext, "Long click position: " + position, Toast.LENGTH_SHORT).show();

            return true;
        });

        holder.mView.setOnClickListener(v -> {
            if (!mCheckedItems.isEmpty()) {
                if (mCheckedItems.contains(positionObject)) {
                    mCheckedItems.remove(positionObject);
                    checkBox.setChecked(false);
                }
                else {
                    mCheckedItems.add(positionObject);
                    checkBox.setChecked(true);
                }
            }

            Toast.makeText(mContext, "Click position: " + position, Toast.LENGTH_SHORT).show();

        });


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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View view, TrackItemAdapter adapter) {
            super(view);
            mView = view;
        }
    }

}
