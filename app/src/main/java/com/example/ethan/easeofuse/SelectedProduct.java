package com.example.ethan.easeofuse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class SelectedProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product);

        Intent i = getIntent();
        String link = i.getExtras().getString("link");
        String url = i.getExtras().getString("image");

        Picasso.with(this).load(url).into((ImageView) findViewById(R.id.image));

        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView price = (TextView) findViewById(R.id.price);
        TextView recommendation = (TextView) findViewById(R.id.recommendation);

        title.setText(i.getExtras().getString("title"));
        description.setText(i.getExtras().getString("description"));
        price.setText(i.getExtras().getString("price"));
        recommendation.setText(i.getExtras().getString("recommendation"));
    }
}