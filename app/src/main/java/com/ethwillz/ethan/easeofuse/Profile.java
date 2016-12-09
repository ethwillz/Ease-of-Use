package com.ethwillz.ethan.easeofuse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends Fragment {
    View v;
    TextView displayName;
    ArrayList<ProductInformation> items = new ArrayList<>();
    ArrayList<ProductInformation> products = new ArrayList<>();
    FirebaseUser user;
    RecyclerView savedGrid;
    private ProductGridAdapter mAdapter;
    GridLayoutManager layout;
    AppBarLayout appBarLayout;
    ProductInformation info;
    Button follow;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

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
        TextView savedTitle = (TextView) v.findViewById(R.id.savedTitle);

        displayName.setTypeface(two);
        savedTitle.setTypeface(main);

        if(user != null){
            displayName.setText(user.getDisplayName());
        }
    }
    public void populateGrid(){
        //Gets the saved items for a user
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("saved").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    items.add(getProductInfo(d.getKey()));
                }
                //Sets adapter to the list of products
                mAdapter = new ProductGridAdapter(items);
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
