package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

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

        Button addButton = (Button) getView().findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dao<TrackedItem, Long> trackedItemDao = getDatabaseHelper().getTrackedItemDao();

                    trackedItemDao.create(new TrackedItem("New Item"));

                    onDatabaseChange();
                } catch (SQLException e) {
                    e.printStackTrace();
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