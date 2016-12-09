package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    List<ProductInformation> products;
    ProductFilter filter;

    //Constructor for adapter which sets the list of products and initializes the filter
    public ProductAdapter(ArrayList<ProductInformation> items){
        products = items;
        filter = new ProductFilter(products, this);
    }

    //Viewholder which establishes the UI components of the card view that are going to be populated
    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;
        private TextView mPrice;
        private TextView mLink;
        private TextView mRecommendation;
        private TextView mUrl;
        private TextView mUser;
        private TextView mImageUrl;
        private TextView mProductID;
        private TextView mUid;

        public ProductViewHolder(View v){
            super(v);

            final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Taken by Vultures Demo.otf");
            final Typeface two = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");

            mTitle = (TextView) v.findViewById(R.id.item_title);
            mDescription = (TextView) v.findViewById(R.id.item_description);
            mImage = (ImageView) v.findViewById(R.id.item_image);
            mImageUrl = (TextView) v.findViewById(R.id.item_image_url);
            mPrice = (TextView) v.findViewById(R.id.item_price);
            mLink = (TextView) v.findViewById(R.id.item_link);
            mRecommendation = (TextView) v.findViewById(R.id.item_recommendation);
            mUrl = (TextView) v.findViewById(R.id.item_url);
            mUser = (TextView) v.findViewById(R.id.item_user);
            mProductID = (TextView) v.findViewById(R.id.item_id);
            mUid = (TextView) v.findViewById(R.id.item_uid);

            mUser.setTypeface(main);
            mTitle.setTypeface(two);
            mDescription.setTypeface(two);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int position = getLayoutPosition();
            String title = mTitle.getText().toString();
            String description = mDescription.getText().toString();
            String price = mPrice.getText().toString();
            String link = mLink.getText().toString();
            String recommendation = mRecommendation.getText().toString();
            String image = mImageUrl.getText().toString();
            String id = mProductID.getText().toString();
            String uid = mUid.getText().toString();
            Intent i = new Intent(view.getContext(), SelectedProduct.class);
            i.putExtra("title", title);
            i.putExtra("description", description);
            i.putExtra("price", price);
            i.putExtra("link", link);
            i.putExtra("recommendation", recommendation);
            i.putExtra("image", image);
            i.putExtra("id", id);
            i.putExtra("uid", uid);
            view.getContext().startActivity(i);
        }
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = (View)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cards, parent, false);

        return new ProductViewHolder(v);
    }

    //Binds all information to UI element and sets up click listener
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        holder.mTitle.setText(products.get(position).getTitle());
        holder.mDescription.setText(products.get(position).getDescription());
        holder.mPrice.setText(products.get(position).getPrice());
        holder.mLink.setText(products.get(position).getLink());
        holder.mRecommendation.setText(products.get(position).getRecommendation());
        holder.mUrl.setText(products.get(position).getImageUrl());
        holder.mUser.setText(products.get(position).getUser());
        holder.mImageUrl.setText(products.get(position).getImageUrl());
        holder.mProductID.setText(products.get(position).getProductID());
        holder.mUid.setText(products.get(position).getUid());

        Picasso.with(holder.itemView.getContext()).load(products.get(position).getImageUrl()).into(holder.mImage);
    }

    //Returns count of products in list
    public int getItemCount(){
        return products.size();
    }

    //Sets visible list to a new list
    public void setList(List<ProductInformation> list){
        this.products = list;
    }

    //Filters products based on a query
    public void filterProducts(String query){
        filter.filter(query);
    }
}


