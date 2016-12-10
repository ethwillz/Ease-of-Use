package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main extends Fragment{
    //Sets up all of the various variables needed throughout the class
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ProductInformation> items = new ArrayList<>();
    HashMap<String, Integer> products = new HashMap<>();
    Database.Product savedItems = new Database.Product();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.products_hot, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        View v = getView();

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("saved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Iterator<DataSnapshot> productIDs = d.getChildren().iterator();
                    for(int i = 0; i < d.getChildrenCount(); i++){
                        String productID = productIDs.next().getKey();
                        products.put(productID, 1 + (products.containsKey(productID) ? products.get(productID) : 0));
                    }
                }

                //Sets adapter to the list of products
                mAdapter = new ProductAdapter(new Organize().doInBackground(new TaskParams(products, items)));
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class Organize extends AsyncTask<TaskParams, Void, ArrayList<ProductInformation>>{
        ArrayList<ProductInformation> sorted;
        @Override
        protected ArrayList<ProductInformation> doInBackground(TaskParams... taskParamses) {
            sorted = savedItems.getSavedProducts(taskParamses[0].map);
            sorted = ProductSort.mergeSort(taskParamses[0].map, sorted);
            return sorted;
        }

        protected void onPostExecute(ArrayList<ProductInformation> result){

        }
    }

    public class TaskParams{
        HashMap<String, Integer> map;
        ArrayList<ProductInformation> info;

        public TaskParams(HashMap<String, Integer> map, ArrayList<ProductInformation> info){
            this.map = map;
            this.info = info;
        }
    }
}