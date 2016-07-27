package com.example.ethan.easeofuse;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private DatabaseReference mDatabase;
    ArrayList<ProductInformation> products;

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mImageView;

        public ProductViewHolder(View v){
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.item_titleTextView);
            mDescriptionTextView = (TextView) v.findViewById(R.id.item_descriptionTextView);
            mImageView = (ImageView) v.findViewById(R.id.item_imageView);
        }
    }

    public ProductAdapter(ArrayList<ProductInformation> items){
        products = items;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_children, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ProductAdapter.ProductViewHolder vh = new ProductAdapter.ProductViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        holder.mTitleTextView.setText(products.get(position).getTitle());
        holder.mDescriptionTextView.setText(products.get(position).getDescription());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference httpsReference = storage.getReferenceFromUrl(products.get(position).getImageUrl());

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                holder.mImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });

    }

    public int getItemCount(){
        return products.size();
    }

}
