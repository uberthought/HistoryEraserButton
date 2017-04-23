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

public class TrackedItemFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TrackItemAdapter mAdapter;

    public static TrackedItemFragment newInstance() {
        return new TrackedItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trackeditem_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.trackeditem_recyclerview);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
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

            builder.setPositiveButton("OK", (dialog, which) -> mAdapter.addItem(input.getText().toString()));

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete these record")
                    .setCancelable(true)
                    .setNegativeButton("Delete", (dialogInterface, i) -> mAdapter.deleteChecked());
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

    private TrackItemAdapter createAdapter() {
        TrackItemAdapter adapter = new TrackItemAdapter(getContext());
        adapter.SetOnItemTouchListener(position -> {
            TrackedItem trackedItem = mAdapter.getItem(position);

            SimpleRecordFragment simpleRecordFragment = SimpleRecordFragment.newInstance();

            simpleRecordFragment.Bind(trackedItem.getName());

            getFragmentManager().beginTransaction()
                    .replace(R.id.FragmentView, simpleRecordFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return adapter;
    }
}

