package org.uberthought.myapplication;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

public class SimpleRecordListFragment extends ListFragment {

    SimpleRecordCursorAdapter adapter;
    boolean mIsChecking;
    private MainDBHelper databaseHelper;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
            adapter = new SimpleRecordCursorAdapter(getContext(), cursor);
            setListAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        getListView().setOnItemLongClickListener(getOnItemLongClickListener());
    }

    public void onDatabaseChange() {
        try {
            Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
            adapter.swapCursor(cursor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return databaseHelper;
    }

    @NonNull
    private AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mIsChecking = !mIsChecking;
                if (mIsChecking) {
                    getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
                    getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
                }
                return false;
            }
        };
//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                listView.setItemChecked(i, true);
//                CheckableLinearLayout checkableLinearLayout = (CheckableLinearLayout)view;
//                checkableLinearLayout.setChecked(true);
//                int checkedCount = listView.getCheckedItemCount();
//                long[] checkedItemIds = listView.getCheckedItemIds();
//                try {
//                    Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
//
//                    cursor.move(i);
//                    final Long id = cursor.getLong(cursor.getColumnIndex("_id"));
//
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adapterView.getContext());
//                    alertDialogBuilder
//                            .setMessage("Are you sure you want to delete this record")
//                            .setCancelable(true)
//                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    try {
//                                        Dao<SimpleRecord, Long> dao = getDatabaseHelper().getDao();
//                                        dao.deleteById(id);
//                                        onDatabaseChange();
//                                    } catch (SQLException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

//            }
//        };
    }
}
