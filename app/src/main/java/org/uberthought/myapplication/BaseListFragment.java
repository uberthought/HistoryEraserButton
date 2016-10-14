package org.uberthought.myapplication;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public abstract class BaseListFragment extends ListFragment {

    BaseCursorAdapter adapter;
    private MainDBHelper databaseHelper;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        getListView().setOnItemClickListener((adapterView, view12, i, l) -> {
            if (adapter.isCheckable()) {
                int checkedCount = getListView().getCheckedItemCount();
                if (checkedCount == 0)
                    setIsCheckable(false);
            }
        });

        getListView().setOnItemLongClickListener((parent, view1, position, id) -> {
            setIsCheckable(true);
            return false;
        });

        //noinspection ConstantConditions
        getDeleteButton().setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete these record")
                    .setCancelable(true)
                    .setNegativeButton("Delete", (dialogInterface, i) -> {
                        // build a list of items to delete
                        long[] checkedItems = getListView().getCheckedItemIds();

                        deleteItems(checkedItems);

                        // clear the checkboxes
                        setIsCheckable(false);

                        // refresh
                        onDatabaseChange();
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        });

        final Button clearButton = getClearButton();

        //noinspection ConstantConditions
        clearButton.setOnClickListener(v -> {
            // clear checked items
            getListView().clearChoices();
            getListView().requestLayout();

            // after 1/2 a second, clear the checkboxes
            //noinspection ConstantConditions
            clearButton.postDelayed(() -> {
                // clear the checkboxes
                setIsCheckable(false);
            }, 500);
        });

    }

    MainDBHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getContext(), MainDBHelper.class);
        return databaseHelper;
    }

    abstract Cursor createCursor();

    abstract void deleteItems(long[] itemIds);

    void onDatabaseChange() {
        adapter.swapCursor(createCursor());
        getDatabaseHelper().OnDBChanged();
    }

    private void setIsCheckable(boolean checkable) {
        if (checkable) {
            if (!adapter.isCheckable()) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                adapter.setIsCheckable(true);
                //noinspection ConstantConditions
                getBottomBar().setVisibility(View.VISIBLE);
                onDatabaseChange();
            }
        } else {
            if (adapter.isCheckable()) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
                adapter.setIsCheckable(false);
                //noinspection ConstantConditions
                getBottomBar().setVisibility(View.GONE);
                onDatabaseChange();
            }
        }
    }

    private LinearLayout getBottomBar() {
        View view = getView();
        if (view != null)
            return (LinearLayout) view.findViewById(R.id.bottomBar);
        return null;
    }

    private Button getDeleteButton() {
        View view = getView();
        if (view != null)
            return (Button) view.findViewById(R.id.deleteButton);
        return null;
    }

    private Button getClearButton() {
        View view = getView();
        if (view != null)
            return (Button) view.findViewById(R.id.clearButton);
        return null;
    }
}
