package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;

public class SimpleRecordListFragment extends BaseListFragment {

    long mTrackedItemId;

    public static SimpleRecordListFragment newInstance() {
        return new SimpleRecordListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simplerecordlist_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SimpleRecordCursorAdapter(getContext(), createCursor());
        setListAdapter(adapter);

        Button addButton = (Button) getView().findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dao<TrackedItem, Long> trackedItemDao = getDatabaseHelper().getDao(TrackedItem.class);
                    TrackedItem trackedItem = trackedItemDao.queryForId(mTrackedItemId);

                    Date currDateTime = new Date(System.currentTimeMillis());

                    // create the new record
                    SimpleRecord simpleRecord = getDatabaseHelper().create(SimpleRecord.class);
                    simpleRecord.setDate(currDateTime);
                    getDatabaseHelper().update(simpleRecord);

                    // add the record to the tracked item
                    trackedItem.getSimpleRecords().add(simpleRecord);
                    getDatabaseHelper().update(trackedItem);

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
            return getDatabaseHelper().getSimpleRecordCursor(mTrackedItemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    void deleteItems(long[] itemIds) {
        getDatabaseHelper().deleteItems(SimpleRecord.class, itemIds);
    }

    public void Bind(Long id) {
        mTrackedItemId = id;
    }

}
