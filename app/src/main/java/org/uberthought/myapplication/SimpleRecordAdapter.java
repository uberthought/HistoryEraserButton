package org.uberthought.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SimpleRecordAdapter extends RecyclerView.Adapter<SimpleRecordAdapter.ViewHolder> {

    private final List<SimpleRecord> mItems = new ArrayList<>();

    SimpleRecordAdapter(Context context, String trackedItemName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName() + "/" + trackedItemName);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Date date = dataSnapshot.getValue(Date.class);
                mItems.add(new SimpleRecord(date));
                SimpleRecordAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Date date = dataSnapshot.getValue(Date.class);
                mItems.removeIf(trackedItem -> trackedItem.getDate().equals(date));
                mItems.add(new SimpleRecord(date));
                SimpleRecordAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Date date = dataSnapshot.getValue(Date.class);
                mItems.removeIf(trackedItem -> trackedItem.getDate().equals(date));
                SimpleRecordAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SimpleRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simplerecord_cell, parent, false);
        // set the view's size, margins, padding and layout parameters

        return new SimpleRecordAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SimpleRecordAdapter.ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element

        SimpleRecord simpleRecord = mItems.get(position);
        TextView textView = (TextView) holder.mView.findViewById((R.id.textView));
        textView.setText(simpleRecord.getDate().toString());

        CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.checkBox);
        checkBox.setChecked(simpleRecord.isChecked());

        holder.mView.setOnLongClickListener(v -> {
            simpleRecord.setChecked(!simpleRecord.isChecked());
            checkBox.setChecked(simpleRecord.isChecked());
            return true;
        });

        holder.mView.setOnClickListener(v -> {
            boolean anyChecked = false;
            for (SimpleRecord foo : mItems) {
                if (foo.isChecked()) {
                    anyChecked = true;
                    break;
                }
            }
            if (anyChecked) {
                simpleRecord.setChecked(!simpleRecord.isChecked());
                checkBox.setChecked(simpleRecord.isChecked());
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    void addItem(String trackedItemName) {
        Date date = new Date(System.currentTimeMillis());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName() + "/" + trackedItemName);

        reference.push().setValue(date);
    }

    void deleteChecked(String trackedItemName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName() + "/" + trackedItemName);

        List<Date> checkedItems = getCheckedItems();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Date date : checkedItems) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        Date childDate = childSnapshot.getValue(Date.class);
                        if (childDate.equals(date))
                            childSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void clearCheckedItems() {
        for (SimpleRecord simpleRecord : mItems) {
            simpleRecord.setChecked(false);
        }
    }

    private List<Date> getCheckedItems() {
        List<Date> checkItems = new ArrayList<>();

        for (SimpleRecord simpleRecord : mItems) {
            if (simpleRecord.isChecked())
                checkItems.add(simpleRecord.getDate());
        }

        return checkItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }
}
