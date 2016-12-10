package com.ethwillz.ethan.easeofuse;

import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    TextView userDisplayName;
    ArrayList<ProductInformation> items = new ArrayList<>();
    ArrayList<ProductInformation> products = new ArrayList<>();
    FirebaseUser user;
    RecyclerView savedGrid;
    private ProductGridAdapter mAdapter;
    GridLayoutManager layout;
    AppBarLayout appBarLayout;
    ProductInformation info;
    String uid;
    String displayName;
    String imageUrl;
    DatabaseReference mDatabase;
    Button follow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_other_profile);

        uid = getIntent().getStringExtra("uid");
        displayName = getIntent().getStringExtra("displayName");
        imageUrl = getIntent().getStringExtra("imageUrl");


        Database.Product all = new Database.Product();
        products = all.getAllProducts();
        savedGrid = (RecyclerView) findViewById(R.id.productGrid);
        appBarLayout = ((AppBarLayout) findViewById(R.id.appBar));
        user = FirebaseAuth.getInstance().getCurrentUser();
        layout = new GridLayoutManager(this, 2);
        savedGrid.setLayoutManager(layout);
        savedGrid.setHasFixedSize(true);
        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);

        populateGrid();

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Walkway Bold.ttf");
        final Typeface two = Typeface.createFromAsset(getAssets(), "fonts/Taken by Vultures Demo.otf");
        userDisplayName = (TextView) findViewById(R.id.user_display_name);
        TextView savedTitle = (TextView) findViewById(R.id.savedTitle);

        Picasso.with(this).load(imageUrl).into(profilePic);

        follow = (Button) findViewById(R.id.follow);
        follow.setTypeface(main);
        userDisplayName.setTypeface(two);
        savedTitle.setTypeface(main);
        userDisplayName.setText(displayName);

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

        mDatabase.child("following").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.getKey().equals(uid)){
                        follow.setText("Following");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //On click of follower button adds this user to logged in user's followers and adds this user to logged in user's following
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(follow.getText().toString().equals("Follow")) {
                    follow.setText("Following");
                    mDatabase.child("following").child(user.getUid()).child(uid).setValue(displayName);
                    mDatabase.child("followers").child(uid).child(user.getUid()).setValue(user.getDisplayName());
                }
                else{
                    follow.setText("Follow");
                    mDatabase.child("following").child(user.getUid()).child(uid).removeValue();
                    mDatabase.child("followers").child(uid).child(user.getUid()).removeValue();
                }
            }
        });
    }

    public void populateGrid(){
        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.child("uid").getValue().toString().equals(uid)){
                        items.add(0, getProductInfo(d.getKey()));
                    }
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
            if(products.get(i).getProductID().equals(productID)) {
                return products.get(i);
            }
        }
        return info;
    }
}
