package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        getListView().setOnItemClickListener((adapterView, v, i, l) -> {
            if (!adapter.isCheckable()) {
                if (listener != null)
                    listener.onRowPressed(l);
            }
        });

        View view1 = getView();
        assert view1 != null;
        Button addButton = (Button) view1.findViewById(R.id.addButton);
        assert addButton != null;
        addButton.setOnClickListener((v) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {

                TrackedItem trackedItem = getDatabaseHelper().create(TrackedItem.class);
                trackedItem.setName(input.getText().toString());
                getDatabaseHelper().update(trackedItem);

                onDatabaseChange();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @Override
    Cursor createCursor() {
        try {
            return getDatabaseHelper().getCursor(TrackedItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    void deleteItems(long[] itemIds) {
        getDatabaseHelper().deleteItems(TrackedItem.class, itemIds);
    }

    public void setRowListener(RowListener listener) {
        this.listener = listener;
    }

    public interface RowListener {
        void onRowPressed(Long id);
    }


}