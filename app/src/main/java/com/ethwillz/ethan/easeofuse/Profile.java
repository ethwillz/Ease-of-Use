package com.ethwillz.ethan.easeofuse;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    View v;
    TextView displayName;
    ArrayList<ProductInformation> items = new ArrayList<>();
    ArrayList<ProductInformation> products = new ArrayList<>();
    FirebaseUser user;
    RecyclerView savedGrid;
    private GridAdapter mAdapter;
    GridLayoutManager layout;
    AppBarLayout appBarLayout;
    ProductInformation info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        RecyclerView savedGrid = (RecyclerView) view.findViewById(R.id.savedGrid);
        appBarLayout = ((AppBarLayout) view.findViewById(R.id.appBar));

        savedGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(scrollDy==0&&(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
                {
                    appBarLayout.setExpanded(true);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database.Product all = new Database.Product();
        products = all.getAllProducts();

        v = getView();
        user = FirebaseAuth.getInstance().getCurrentUser();
        layout = new GridLayoutManager(v.getContext(), 2);
        savedGrid = (RecyclerView) v.findViewById(R.id.savedGrid);
        savedGrid.setLayoutManager(layout);
        savedGrid.setHasFixedSize(true);

        populateGrid();

        final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");
        final Typeface two = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Taken by Vultures Demo.otf");
        displayName = (TextView) v.findViewById(R.id.displayName);
        TextView uid = (TextView) v.findViewById(R.id.uid);
        TextView savedTitle = (TextView) v.findViewById(R.id.savedTitle);

        displayName.setTypeface(two);
        uid.setTypeface(main);
        savedTitle.setTypeface(main);

        if(user != null){
            displayName.setText(user.getDisplayName());
            uid.setText(user.getUid());
        }

        /*
        displayName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(v.getContext(), Account.class);
                startActivity(i);
                return true;
            }
        });
        */
    }
    public void populateGrid(){
        //Populates the recyclerview with the name, description, and photo for all products in the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("saved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    System.out.println(d.child("user").getValue().toString());
                    if(d.child("user").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        items.add(getProductInfo(d.child("product").getValue().toString()));
                    }
                }
                //Sets adapter to the list of products
                mAdapter = new GridAdapter(items);
                savedGrid.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ProductInformation getProductInfo(String productID){
        for(int i = 0; i < products.size(); i++){
            System.out.println(products.get(i).getImageUrl());
            if(products.get(i).getProductID().equals(productID)) {
                return products.get(i);
            }
        }
        return info;
    }

}
