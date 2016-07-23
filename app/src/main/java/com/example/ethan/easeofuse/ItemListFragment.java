package com.example.ethan.easeofuse;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ItemListFragment extends ListFragment {

    private DatabaseReference mDatabase;
    Query mPostReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> items = new ArrayList<>();
                for (int i = 0; i < dataSnapshot.child("products").getChildrenCount(); i++) {
                    String url = dataSnapshot.child("products").child(i + "").child("downloadUrl").getValue().toString();
                    String title = dataSnapshot.child("products").child(i + "").child("name").getValue().toString();
                    String description = dataSnapshot.child("products").child(i + "").child("description").getValue().toString();
                    Item item = new Item(url, title, description);
                    items.add(item);
                }
                setListAdapter(new ItemAdapter(getActivity(), items));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Yo", "loadPost:onCancelled", databaseError.toException());
            }
        });

        return v;
    }
}