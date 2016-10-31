package com.ethwillz.ethan.easeofuse;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    View v;
    ImageView pic;
    TextView displayName;
    TextView uid;
    TextView savedTitle;
    GridView savedGrid;
    DatabaseReference mDatabase;
    ArrayList<ProductInformation> items = new ArrayList<>();
    FirebaseUser user;
    ArrayList<String> savedItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        savedGrid = (GridView) view.findViewById(R.id.savedGrid);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        v = getView();
        user = FirebaseAuth.getInstance().getCurrentUser();
        savedGrid.invalidateViews();

        //Populates the recyclerview with the name, description, and photo for all products in the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Adds relevant information about each product to a List
                for (long i = dataSnapshot.child("saved").getChildrenCount()-1; i >= 0; i--) {
                    if (dataSnapshot.child("saved").child("" + i).child("user").getValue().toString().equals(user.getUid())) {
                        System.out.println(dataSnapshot.child("saved").child("" + i).child("product").getValue().toString());
                        savedItems.add(dataSnapshot.child("saved").child("" + i).child("product").getValue().toString());
                    }
                }

                for(int i = 0; i < savedItems.size(); i++){
                    String url = dataSnapshot.child("products").child(savedItems.get(i)).child("downloadUrl").getValue().toString();
                    String title = dataSnapshot.child("products").child(savedItems.get(i)).child("name").getValue().toString();
                    String description = dataSnapshot.child("products").child(savedItems.get(i)).child("description").getValue().toString();
                    String link = dataSnapshot.child("products").child(savedItems.get(i)).child("link").getValue().toString();
                    String price = dataSnapshot.child("products").child(savedItems.get(i)).child("price").getValue().toString();
                    String recommendation = dataSnapshot.child("products").child(savedItems.get(i)).child("recommendation").getValue().toString();
                    String type = dataSnapshot.child("products").child(savedItems.get(i)).child("type").getValue().toString();
                    String style = dataSnapshot.child("products").child(savedItems.get(i)).child("style").getValue().toString();
                    String poster = dataSnapshot.child("products").child(savedItems.get(i)).child("user").getValue().toString();
                    String id = savedItems.get(i);
                    ProductInformation item = new ProductInformation(url, title, description, link, price, recommendation, type, style, poster, id);
                    items.add(item);
                }

                savedGrid.setAdapter(new GridAdapter(v.getContext(), items));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final Typeface eou = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Barrio Santo.ttf");
        pic = (ImageView) v.findViewById(R.id.profilepic);
        displayName = (TextView) v.findViewById(R.id.displayName);
        uid = (TextView) v.findViewById(R.id.uid);
        savedTitle = (TextView) v.findViewById(R.id.savedTitle);

        displayName.setTypeface(eou);
        uid.setTypeface(eou);
        savedTitle.setTypeface(eou);

        if(user != null){
            Picasso.with(v.getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.logo).into(pic);
            displayName.setText(user.getDisplayName());
            uid.setText(user.getUid());
        }

    }

}
