package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class TrackedItemFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TrackItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MainDBHelper mDatabaseHelper;

    public static TrackedItemFragment newInstance() {
        return new TrackedItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trackeditem_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.trackeditem_recyclerview);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set CustomAdapter as the adapter for RecyclerView.
        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = (Button) view.findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener((v) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {

                TrackedItem.createOrIncrement(input.getText().toString(), getDatabaseHelper());

                getDatabaseHelper().dbChanged();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
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
                            Dao<TrackedItem, Long> dao = getDatabaseHelper().getDao(TrackedItem.class);
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

            // after 1/2 a second, clear the checkboxes
            //noinspection ConstantConditions
            clearButton.postDelayed(() -> {
                // clear the checkboxes
            }, 500);
        });

        getDatabaseHelper().addDBListener(this::onDatabaseChange);
    }


    MainDBHelper getDatabaseHelper() {
        if (mDatabaseHelper == null)
            mDatabaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return mDatabaseHelper;
    }

    void onDatabaseChange() {
        mAdapter = createAdapter();
        mRecyclerView.swapAdapter(mAdapter, true);
    }

    private TrackItemAdapter createAdapter() {
        TrackItemAdapter adapter = new TrackItemAdapter(getContext());
        adapter.SetOnItemTouchListener(position -> {
            long trackedItemId = adapter.getItemId(position);

            SimpleRecordFragment simpleRecordFragment = SimpleRecordFragment.newInstance();

            simpleRecordFragment.Bind(trackedItemId);

            getFragmentManager().beginTransaction()
                    .replace(R.id.FragmentView, simpleRecordFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return adapter;
    }
}

