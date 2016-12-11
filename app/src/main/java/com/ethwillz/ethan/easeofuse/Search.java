package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
    ArrayList<String> following = new ArrayList<>();
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products_main_feed, container, false);
        search = (EditText) view.findViewById(R.id.searchbar);

        ImageButton add = (ImageButton) view.findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddProduct.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        v = getView();

        user= FirebaseAuth.getInstance().getCurrentUser();
        final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");
        search.setTypeface(main);

        //Sets up some more variables and intializes the views
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        following.add(user.getUid());
        mDatabase.child("following").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    following.add(d.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Adds relevant information about each product to a List
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (following.contains(d.child("uid").getValue().toString())) {
                        String url = d.child("downloadUrl").getValue().toString();
                        String title = d.child("name").getValue().toString();
                        String description = d.child("description").getValue().toString();
                        String link = d.child("link").getValue().toString();
                        String price = d.child("price").getValue().toString();
                        String recommendation = d.child("recommendation").getValue().toString();
                        String poster = d.child("user").getValue().toString();
                        String uid = d.child("uid").getValue().toString();
                        String id = d.getKey();
                        ProductInformation item = new ProductInformation(url, title, description, link, price, recommendation, poster, id, uid);
                        items.add(0, item);
                    }
                    //Sets adapter to the list of products
                    mAdapter = new ProductAdapter(items);
                    mRecyclerView.setAdapter(mAdapter);
                }
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
                String text = search.getText().toString().toLowerCase();
                mAdapter.filterProducts(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}