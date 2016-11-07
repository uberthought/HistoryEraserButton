package org.uberthought.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by prichardson on 11/7/16.
 */

public class TrackedItemFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TrackItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

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

        mAdapter = new TrackItemAdapter(getContext());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}

