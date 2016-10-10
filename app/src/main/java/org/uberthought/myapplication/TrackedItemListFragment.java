package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;

public class TrackedItemListFragment extends BaseListFragment {

    private RowListener listener = null;

    public static TrackedItemListFragment newInstance() {
        return new TrackedItemListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trackeditemlist_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TrackedItemCursorAdapter(getContext(), createCursor());
        setListAdapter(adapter);

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapter.isCheckable()) {
                    if (listener != null)
                        listener.onRowPressed(l);
                }
            }
        });
    }

    @Override
    Cursor createCursor() {
        try {
            return getDatabaseHelper().getTrackedItemCursor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setRowListener(RowListener listener) {
        this.listener = listener;
    }

    public interface RowListener {
        void onRowPressed(Long id);
    }

}