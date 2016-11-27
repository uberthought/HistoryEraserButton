package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SimpleRecordFragment extends Fragment {

    private long mTrackedItemId;
    private RecyclerView mRecyclerView;
    private SimpleRecordAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MainDBHelper mDatabaseHelper;


    public static SimpleRecordFragment newInstance() {
        return new SimpleRecordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.simplerecord_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.simplerecord_recyclerview);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SimpleRecordAdapter(getContext(), mTrackedItemId);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = (Button) view.findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener((v) -> {

            try {
                Dao<TrackedItem, Long> trackedItemDao = null;
                trackedItemDao = getDatabaseHelper().getDao(TrackedItem.class);
                TrackedItem trackedItem = trackedItemDao.queryForId(mTrackedItemId);

                Date currDateTime = new Date(System.currentTimeMillis());

                // create the new record
                SimpleRecord simpleRecord = getDatabaseHelper().create(SimpleRecord.class);
                simpleRecord.setDate(currDateTime);
                getDatabaseHelper().update(simpleRecord);

                // add the record to the tracked item
                trackedItem.getSimpleRecords().add(simpleRecord);
                getDatabaseHelper().update(trackedItem);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            getDatabaseHelper().dbChanged();
        });

        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete these record")
                    .setCancelable(true)
                    .setNegativeButton("Delete", (dialogInterface, i) -> {
                        try {
                            // build a list of items to delete
                            List<Long> checkedIds = mAdapter.getCheckedIds();

                            // delete them
                            Dao<SimpleRecord, Long> dao = getDatabaseHelper().getDao(SimpleRecord.class);
                            dao.deleteIds(checkedIds);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // refresh
                        onDatabaseChange();
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        });

        final Button clearButton = (Button) view.findViewById(R.id.clearButton);

        //noinspection ConstantConditions
        clearButton.setOnClickListener(v -> {
            // clear checked items
            mAdapter.clearCheckedItems();
            mRecyclerView.swapAdapter(mAdapter, true);
        });

        getDatabaseHelper().addDBListener(this::onDatabaseChange);
    }

    MainDBHelper getDatabaseHelper() {
        if (mDatabaseHelper == null)
            mDatabaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return mDatabaseHelper;
    }

    void onDatabaseChange() {
        mAdapter = new SimpleRecordAdapter(getContext(), mTrackedItemId);
        mRecyclerView.swapAdapter(mAdapter, true);
    }

    public void Bind(Long id) {
        mTrackedItemId = id;
    }

}
