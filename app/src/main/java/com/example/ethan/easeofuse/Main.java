package com.example.ethan.easeofuse;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GoogleApiClient client;
    DatabaseReference mDatabase;
    ArrayList<ProductInformation> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Sets up some more variables and intializes the views
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Increases efficiency
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
                    }
                }

                for (long i = dataSnapshot.child("products").getChildrenCount()-1; i >= 0; i--) {
                    String url = dataSnapshot.child("products").child(i + "").child("downloadUrl").getValue().toString();
                    String title = dataSnapshot.child("products").child(i + "").child("name").getValue().toString();
                    String description = dataSnapshot.child("products").child(i + "").child("description").getValue().toString();
                    String link = dataSnapshot.child("products").child(i+"").child("link").getValue().toString();
                    String price = dataSnapshot.child("products").child(i+"").child("price").getValue().toString();
                    String recommendation = dataSnapshot.child("products").child(i+"").child("recommendation").getValue().toString();
                    ProductInformation item = new ProductInformation(url, title, description, link, price, recommendation);
                    items.add(item);
                }
                mAdapter = new ProductAdapter(items);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Inflates the app bar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Handles selection of settings in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(this, SignIn.class);
            startActivity(i);
            return true;
        }
        else {
            Intent i = new Intent(this, Filter.class);
            startActivity(i);
            return true;
        }
    }

    public void onAddClick(View view){
        Intent i = new Intent(this, AddProduct.class);
        startActivity(i);
    }

    //Closes app if the back button is pressed from this activity
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

