package org.uberthought.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;

public class SimpleRecordListFragment extends BaseListFragment {

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
    }

    @Override
    Cursor createCursor() {
        try {
            return getDatabaseHelper().getSimpleRecordCursor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //                int checkedCount = getListView().getCheckedItemCount();
//                long[] checkedItemIds = getListView().getCheckedItemIds();
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
//                                        Dao<SimpleRecord, Long> dao = getDatabaseHelper().getSimpleRecordDao();
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
