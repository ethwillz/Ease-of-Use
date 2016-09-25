package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Main extends AppCompatActivity {
    //Sets up all of the various variables needed throughout the class
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabase;
    ArrayList<ProductInformation> items = new ArrayList<>();
    static final int FILTER_REQUEST = 743;
    static final int RESULT_GOOD = 879;
    private String savedStyle = "All";
    private String savedType = "All";
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Sets up some more variables and intializes the views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5566797500264030~3966962306");
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1BF89AB15C45335B1CA8BCE94927DA8C").build();
        mAdView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Checks if user is authorized to control visibility of add button
                if(user != null) {
                    String uid = user.getUid();
                    if (dataSnapshot.child("users").child(uid).child("authorized").getValue().toString().equals("1")) {
                        ImageButton add = (ImageButton) findViewById(R.id.add_button);
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
                    ProductInformation item = new ProductInformation(url, title, description, link, price, recommendation, type, style);
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
    }

    //Inflates the app bar menu and establishes that the searchview exists
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return true;
    }

    //Handles selection of settings, filter, and search
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(this, SignIn.class);
            startActivity(i);
            return true;
        }
        //Upon chosing search the searchview has the listener set up which filters the list based on the input
        else if(item.getItemId() == R.id.action_search){
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mAdapter.filterProducts(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.filterProducts(newText);
                    return true;
                }
            });
            return true;
        }
        else {
            return true;
        }
    }

    //Gets results from the filter and pares down the list based on user inputs
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == FILTER_REQUEST){
            if(resultCode == RESULT_GOOD) {
                savedStyle = data.getStringExtra("style");
                savedType = data.getStringExtra("type");
                //mAdapter.filterProducts(savedStyle, savedType);
            }
        }
    }

    //Upon click of the add button the new activity to add a product opens
    public void onAddClick(View view){
        Intent i = new Intent(this, AddProduct.class);
        startActivity(i);
    }

    //Closes app if the back button is pressed from this activity
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}