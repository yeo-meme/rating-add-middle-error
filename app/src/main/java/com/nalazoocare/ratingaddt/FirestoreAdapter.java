package com.nalazoocare.ratingaddt;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-05-29
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
extends RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot> {
    private Query mQuery;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();
    private ListenerRegistration mRegistration;
    private static final String TAG = "Firestore Adapter";

    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }


    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }
    protected void onDataChanged() {
    }

    protected DocumentSnapshot getSnapshot(int index) {return mSnapshots.get(index);}

    @Override
    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            return;
        }

        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {

            DocumentSnapshot snapshot = change.getDocument();

            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }

        onDataChanged();
    }

    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }
}
