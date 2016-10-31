package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends Fragment {
    //Sets up all of the various variables needed throughout the class
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabase;
    ArrayList<ProductInformation> items = new ArrayList<>();
    View v;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search, container, false);
        search = (EditText) view.findViewById(R.id.searchbar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        v = getView();

        //Sets up some more variables and intializes the views
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Adds relevant information about each product to a List
                for (long i = dataSnapshot.child("products").getChildrenCount()-1; i >= 0; i--) {
                    String url = dataSnapshot.child("products").child(i + "").child("downloadUrl").getValue().toString();
                    String title = dataSnapshot.child("products").child(i + "").child("name").getValue().toString();
                    String description = dataSnapshot.child("products").child(i + "").child("description").getValue().toString();
                    String link = dataSnapshot.child("products").child(i+"").child("link").getValue().toString();
                    String price = dataSnapshot.child("products").child(i+"").child("price").getValue().toString();
                    String recommendation = dataSnapshot.child("products").child(i+"").child("recommendation").getValue().toString();
                    String type = dataSnapshot.child("products").child(i+"").child("type").getValue().toString();
                    String style = dataSnapshot.child("products").child(i+"").child("style").getValue().toString();
                    String poster = dataSnapshot.child("products").child(i+"").child("user").getValue().toString();
                    String id = i + "";
                    ProductInformation item = new ProductInformation(url, title, description, link, price, recommendation, type, style, poster, id);
                    items.add(item);
                }
                //Sets adapter to the list of products
                mAdapter = new ProductAdapter(items);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        search.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = search.getText().toString();
                mAdapter.filterProducts(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}