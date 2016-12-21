package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        ((EditText) findViewById(R.id.nameBox)).setText(getIntent().getExtras().getString("name"));
        ((EditText) findViewById(R.id.priceBox)).setText(getIntent().getExtras().getString("price"));
        ((EditText) findViewById(R.id.descriptionBox)).setText(getIntent().getExtras().getString("description"));
        ((EditText) findViewById(R.id.recommendationBox)).setText(getIntent().getExtras().getString("recommendation"));

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Walkway Bold.ttf");

        ((Button) findViewById(R.id.save)).setTypeface(main);

        (findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("products").child(getIntent().getStringExtra("productID")).child("name").setValue(((EditText) findViewById(R.id.nameBox)).getText().toString());
                mDatabase.child("products").child(getIntent().getStringExtra("productID")).child("price").setValue(((EditText) findViewById(R.id.priceBox)).getText().toString());
                mDatabase.child("products").child(getIntent().getStringExtra("productID")).child("description").setValue(((EditText) findViewById(R.id.descriptionBox)).getText().toString());
                mDatabase.child("products").child(getIntent().getStringExtra("productID")).child("recommendation").setValue(((EditText) findViewById(R.id.recommendationBox)).getText().toString());

                finish();
            }
        });

        /*
        ((Button) findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("products").child(getIntent().getStringExtra("productID")).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        finish();
                        startActivity(new Intent(getApplication(), MainView.class));
                    }
                });
            }
        });
        */
    }
}
