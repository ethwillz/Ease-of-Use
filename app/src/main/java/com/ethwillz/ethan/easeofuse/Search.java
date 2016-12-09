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

        final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");
        search.setTypeface(main);

        //Sets up some more variables and intializes the views
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        MobileAds.initialize(v.getContext(), "ca-app-pub-5566797500264030~3966962306");
        final AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1BF89AB15C45335B1CA8BCE94927DA8C").build();
        mAdView.loadAd(adRequest);

        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Checks if add_user is authorized to control visibility of add button and banner ad
                if(user != null) {
                    String uid = user.getUid();
                    if (dataSnapshot.child("users").child(uid).child("authorized").getValue().toString().equals("1")) {
                        ImageButton add = (ImageButton) v.findViewById(R.id.add_button);
                        add.setVisibility(View.VISIBLE);
                        mAdView.setVisibility(View.INVISIBLE);
                    }
                }

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
                String text = search.getText().toString().toLowerCase();
                mAdapter.filterProducts(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}