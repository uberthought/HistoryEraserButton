package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SimpleRecordFragment extends Fragment {

    private String mTrackedItemName;
    private RecyclerView mRecyclerView;
    private SimpleRecordAdapter mAdapter;

    private String TAG = "SimpleRecordAdapter";

    public static SimpleRecordFragment newInstance() {
        return new SimpleRecordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.simplerecord_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.simplerecord_recyclerview);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SimpleRecordAdapter(getContext(), mTrackedItemName);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (mAdapter != null && mAdapter.getItemCount() == 0) {
                    mAdapter = null;
                    getFragmentManager().popBackStack();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = (Button) view.findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener((v) -> mAdapter.addItem(mTrackedItemName));

        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete these record")
                    .setCancelable(true)
                    .setNegativeButton("Delete", (dialogInterface, i) -> mAdapter.deleteChecked(mTrackedItemName));
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
    }

    public void Bind(String name) {
        mTrackedItemName = name;
    }
}
