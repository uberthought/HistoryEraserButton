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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class TrackItemAdapter extends RecyclerView.Adapter<TrackItemAdapter.ViewHolder> {

    private final List<TrackedItem> mItems = new ArrayList<>();
    private RecyclerItemClickListener onClickListener;
    private String TAG = "TrackItemAdapter";

    TrackItemAdapter(Context context) {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null) {
                mItems.clear();
                TrackItemAdapter.this.notifyDataSetChanged();
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName());

                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.getKey();
                        long count = dataSnapshot.getChildrenCount();
                        mItems.add(new TrackedItem(name, count));
                        TrackItemAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.getKey();
                        long count = dataSnapshot.getChildrenCount();
                        mItems.removeIf(trackedItem -> trackedItem.getName().equals(name));
                        mItems.add(new TrackedItem(name, count));
                        TrackItemAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getKey();
                        mItems.removeIf(trackedItem -> trackedItem.getName().equals(name));
                        TrackItemAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mItems.clear();
                        TrackItemAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    static void addItem(String trackedItemName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName() + "/" + trackedItemName);

        Date currDateTime = new Date(System.currentTimeMillis());

        ref.push().setValue(currDateTime);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trackeditem_cell, parent, false);
        // set the view's size, margins, padding and layout parameters

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element

        TrackedItem trackedItem = mItems.get(position);

        TextView textView = (TextView) holder.mView.findViewById((R.id.textView2));
        textView.setText(String.format(Locale.getDefault(), "%s %d", trackedItem.getName(), trackedItem.getCount()));

        CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.checkBox);
        checkBox.setChecked(trackedItem.isChecked());

        holder.mView.setOnLongClickListener(v -> {
            trackedItem.setChecked(!trackedItem.isChecked());
            checkBox.setChecked(trackedItem.isChecked());

            return true;
        });

        holder.mView.setOnClickListener(v -> {
            boolean anyChecked = false;
            for (TrackedItem foo : mItems) {
                if (foo.isChecked()) {
                    anyChecked = true;
                    break;
                }
            }
            if (anyChecked) {
                trackedItem.setChecked(!trackedItem.isChecked());
                checkBox.setChecked(trackedItem.isChecked());
            } else if (onClickListener != null)
                onClickListener.onClick(position);
        });
    }

    void clearCheckedItems() {
        for (TrackedItem foo : mItems) {
            foo.setChecked(false);
        }
    }

    void SetOnItemTouchListener(RecyclerItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    TrackedItem getItem(int i) {
        return mItems.get(i);
    }

    private List<String> getCheckedItems() {
        List<String> checkItems = new ArrayList<>();

        for (TrackedItem trackedItem : mItems) {
            if (trackedItem.isChecked())
                checkItems.add(trackedItem.getName());
        }

        return checkItems;
    }

    void deleteChecked() {
        List<String> checkedItems = getCheckedItems();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (String checkedItem : checkedItems) {
            DatabaseReference reference = database.getReference(user.getUid() + "/" + TrackedItem.class.getSimpleName() + "/" + checkedItem);
            reference.removeValue();
        }
    }

    interface RecyclerItemClickListener {
        void onClick(int position);
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
