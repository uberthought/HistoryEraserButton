package org.uberthought.myapplication;

import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class SimpleRecordListFragment extends ListFragment {

    private MainDBHelper databaseHelper;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.simplerecordlist_view, container, false);
//
//        ListView listView = (ListView)view.findViewById(R.id.listView);
//
//
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                try {
////                    Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
////
////                    cursor.move(i);
////                    final Long id = cursor.getLong(cursor.getColumnIndex("_id"));
////
////                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adapterView.getContext());
////                    alertDialogBuilder
////                            .setMessage("Click Delete to remove record")
////                            .setCancelable(true)
////                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                                @Override
////                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                                    try {
////                                        Dao<SimpleRecord, Long> dao = getDatabaseHelper().getDao();
////                                        dao.deleteById(id);
////                                    } catch (SQLException e) {
////                                        e.printStackTrace();
////                                    }
////                                }
////
////                                @Override
////                                public void onNothingSelected(AdapterView<?> adapterView) {
////
////                                }
////                            });
////                    AlertDialog alertDialog = alertDialogBuilder.create();
////                    alertDialog.show();
////
////                } catch (SQLException e) {
////                    e.printStackTrace();
////                }
////
////                UpdateListView(view);
////
////            }
////        });
//
//        UpdateListView(view);
//
//        return view;
//    }
//
//    void UpdateListView()
//    {
//        try {
//            Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
//
//            ListView listView = (ListView)getView();
//            SimpleRecordCursorAdapter adapter = new SimpleRecordCursorAdapter(getContext(), cursor);
//            listView.setAdapter(adapter);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return databaseHelper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();
            SimpleRecordCursorAdapter adapter = new SimpleRecordCursorAdapter(getContext(), cursor);
            setListAdapter(adapter);

            ListView listView = (ListView) view.findViewById(android.R.id.list);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        Cursor cursor = getDatabaseHelper().getSimpleRecordCursor();

                        cursor.move(i);
                        final Long id = cursor.getLong(cursor.getColumnIndex("_id"));

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adapterView.getContext());
                        alertDialogBuilder
                                .setMessage("Are you sure you want to delete this record")
                                .setCancelable(true)
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
                                            Dao<SimpleRecord, Long> dao = getDatabaseHelper().getDao();
                                            dao.deleteById(id);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
