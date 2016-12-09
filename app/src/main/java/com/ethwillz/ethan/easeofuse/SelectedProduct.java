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
import java.util.Map;

public class SelectedProduct extends AppCompatActivity {
    String link;
    Button buy;
    Button save;
    FirebaseUser user;
    long numItems;
    String id;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        buy = (Button) findViewById(R.id.buy);
        save = (Button) findViewById(R.id.save);

        final Typeface button = Typeface.createFromAsset(getAssets(), "fonts/Barrio Santo.ttf");
        buy.setTypeface(button);
        save.setTypeface(button);

        Intent i = getIntent();
        link = i.getExtras().getString("link");
        String url = i.getExtras().getString("image");

        Picasso.with(this).load(url).into((ImageView) findViewById(R.id.image));

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Walkway Bold.ttf");

        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView price = (TextView) findViewById(R.id.price);
        TextView recommendation = (TextView) findViewById(R.id.recommendation);

        title.setTypeface(main);
        description.setTypeface(main);
        price.setTypeface(main);
        recommendation.setTypeface(main);
        title.setText(i.getExtras().getString("title"));
        description.setText(i.getExtras().getString("description"));
        price.setText(i.getExtras().getString("price"));
        recommendation.setText(i.getExtras().getString("recommendation"));

        buy.setTypeface(main);
        save.setTypeface(main);

        id = i.getExtras().getString("id");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numItems = dataSnapshot.child("saved").getChildrenCount();

                //Adds relevant information about each product to a List
                for (long i = dataSnapshot.child("saved").getChildrenCount()-1; i >= 0; i--) {
                    if (dataSnapshot.child("saved").child("" + i).child("add_user").getValue().toString().equals(user.getUid()) && dataSnapshot.child("saved").child("" + i).child("product").getValue().toString().equals(id)) {
                        save.setText("Saved");
                        save.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Adds listener for the database to get the number of products stored which helps in naming scheme of new product

                mDatabase = FirebaseDatabase.getInstance().getReference();
                Map<String, String> upload = new HashMap<>();
                upload.put("product", id);
                upload.put("add_user", user.getUid());
                mDatabase.child("saved").child(numItems + "").setValue(upload);

                save.setText(R.string.saved);
                save.setBackgroundResource(R.color.purple);
            }
        });
    }
}