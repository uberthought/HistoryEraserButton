package org.uberthought.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class TrackedItemFragment extends Fragment {

    private static final String TAG = "TrackedItemFragment";
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

        Button speechInputButton = (Button) rootView.findViewById(R.id.speech_input);
        speechInputButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, 1234);
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this.getContext(), matches.get(0), Toast.LENGTH_LONG).show();
            TrackItemAdapter.addItem(matches.get(0));
        }
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

            builder.setPositiveButton("OK", (dialog, which) -> TrackItemAdapter.addItem(input.getText().toString()));

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

