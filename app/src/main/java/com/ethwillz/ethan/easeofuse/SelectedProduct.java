package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SelectedProduct extends AppCompatActivity {
    FirebaseUser user;
    String id;
    DatabaseReference mDatabase;
    String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        Picasso.with(this).load(getIntent().getExtras().getString("image")).into((ImageView) findViewById(R.id.image));

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Walkway Bold.ttf");

        ((TextView) findViewById(R.id.title)).setTypeface(main);
        ((TextView) findViewById(R.id.title)).setText(getIntent().getExtras().getString("title"));
        ((TextView) findViewById(R.id.description)).setTypeface(main);
        ((TextView) findViewById(R.id.description)).setText(getIntent().getExtras().getString("description"));
        ((TextView) findViewById(R.id.price)).setTypeface(main);
        ((TextView) findViewById(R.id.price)).setText(getIntent().getExtras().getString("price"));
        ((TextView) findViewById(R.id.recommendation)).setTypeface(main);
        ((TextView) findViewById(R.id.recommendation)).setText(getIntent().getExtras().getString("recommendation"));

        ((Button) findViewById(R.id.buy)).setTypeface(main);
        ((Button) findViewById(R.id.save)).setTypeface(main);

        id = getIntent().getExtras().getString("id");

        mDatabase.child("saved").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> savedIDs = dataSnapshot.getChildren().iterator();

                for(int i = 0; i < dataSnapshot.getChildrenCount(); i++){
                    if(savedIDs.next().getKey().equals(id)){
                        ((Button) findViewById(R.id.save)).setText("Saved");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("uid").getValue().toString().equals(user.getUid())){
                        ((Button) findViewById(R.id.save)).setText("Edit");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        (findViewById(R.id.buy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getExtras().getString("link")));
                startActivity(i);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ((Button) findViewById(R.id.save)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Adds listener for the database to get the number of products stored which helps in naming scheme of new product
                if(((Button) findViewById(R.id.save)).getText().toString().equals("Save")){
                    ((Button) findViewById(R.id.save)).setText("Saved");
                    mDatabase.child("saved").child(user.getUid()).child(getIntent().getStringExtra("id")).setValue(getIntent().getStringExtra("uid"));
                }
                else if(((Button) findViewById(R.id.save)).getText().toString().equals("Saved")){
                    ((Button) findViewById(R.id.save)).setText("Save");
                    mDatabase.child("saved").child(user.getUid()).child(getIntent().getStringExtra("id")).removeValue();
                }
                else{
                    Intent i = new Intent(getApplicationContext(), EditProduct.class);
                    i.putExtra("productID", id);
                    i.putExtra("name", ((TextView) findViewById(R.id.title)).getText().toString());
                    i.putExtra("price", ((TextView) findViewById(R.id.price)).getText().toString());
                    i.putExtra("description", ((TextView) findViewById(R.id.description)).getText().toString());
                    i.putExtra("recommendation", ((TextView) findViewById(R.id.recommendation)).getText().toString());
                    finish();
                    startActivity(i);
                }
            }
        });
    }
}