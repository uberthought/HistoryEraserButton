package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseListFragment extends ListFragment {

    BaseCursorAdapter adapter;
    private MainDBHelper databaseHelper;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.isCheckable()) {
                    int checkedCount = getListView().getCheckedItemCount();
                    if (checkedCount == 0)
                        setIsCheckable(false);
                }
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view1, int position, long id) {
                setIsCheckable(true);
                return false;
            }
        });

        getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete these record")
                        .setCancelable(true)
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {

                                    // build a list of items to delete
                                    long[] checkedItems = getListView().getCheckedItemIds();
                                    List<Long> checkedItemList = new ArrayList<>();
                                    for (long checkedItem : checkedItems)
                                        checkedItemList.add(checkedItem);

                                    // delete them from the database
                                    Dao<SimpleRecord, Long> dao = getDatabaseHelper().getSimpleRecordDao();
                                    dao.deleteIds(checkedItemList);

                                    // clear the checkboxes
                                    setIsCheckable(false);

                                    // refresh
                                    onDatabaseChange();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        final Button clearButton = getClearButton();

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear checked items
                getListView().clearChoices();
                getListView().requestLayout();

                // after 1/2 a second, clear the checkboxes
                clearButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // clear the checkboxes
                        setIsCheckable(false);
                    }
                }, 500);
            }
        });

    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return databaseHelper;
    }

    abstract Cursor createCursor();

    void onDatabaseChange() {
        adapter.swapCursor(createCursor());
    }

    private void setIsCheckable(boolean checkable) {
        if (checkable) {
            if (!adapter.isCheckable()) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                adapter.setIsCheckable(true);
                getBottomBar().setVisibility(View.VISIBLE);
                onDatabaseChange();
            }
        } else {
            if (adapter.isCheckable()) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
                adapter.setIsCheckable(false);
                getBottomBar().setVisibility(View.GONE);
                onDatabaseChange();
            }
        }
    }

    LinearLayout getBottomBar() {
        View view = getView();
        if (view != null)
            return (LinearLayout) view.findViewById(R.id.bottomBar);
        return null;
    }

    Button getDeleteButton() {
        View view = getView();
        if (view != null)
            return (Button) view.findViewById(R.id.deleteButton);
        return null;
    }

    Button getClearButton() {
        View view = getView();
        if (view != null)
            return (Button) view.findViewById(R.id.clearButton);
        return null;
    }
}
