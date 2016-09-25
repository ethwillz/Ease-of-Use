package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SelectedProduct extends AppCompatActivity {
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_product);

        Button buy = (Button) findViewById(R.id.buy);
        final Typeface button = Typeface.createFromAsset(getAssets(), "fonts/Barrio Santo.ttf");
        buy.setTypeface(button);

        Intent i = getIntent();
        link = i.getExtras().getString("link");
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

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });
    }
}