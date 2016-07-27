package com.example.ethan.easeofuse;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder extends RecyclerView.ViewHolder{
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
