package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
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
    FilterProducts filter;

    //Constructor for adapter which sets the list of products and initializes the filter
    public ProductAdapter(ArrayList<ProductInformation> items){
        products = items;
        filter = new FilterProducts(products, this);
    }

    //Viewholder which establishes the UI components of the card view that are going to be populated
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;
        private TextView mPrice;
        private TextView mLink;
        private TextView mRecommendation;
        private TextView mUrl;

        public ProductViewHolder(View v){
            super(v);
            mTitle = (TextView) v.findViewById(R.id.item_title);
            mDescription = (TextView) v.findViewById(R.id.item_description);
            mImage = (ImageView) v.findViewById(R.id.item_image);
            mPrice = (TextView) v.findViewById(R.id.item_price);
            mLink = (TextView) v.findViewById(R.id.item_link);
            mRecommendation = (TextView) v.findViewById(R.id.item_recommendation);
            mUrl = (TextView) v.findViewById(R.id.item_url);
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

        Picasso.with(holder.itemView.getContext()).load(products.get(position).getImageUrl()).placeholder(R.drawable.logo).into(holder.mImage);

        //On click gets the position of the view and goes into the detailed view
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                String title = products.get(position).getTitle();
                String description = products.get(position).getDescription();
                String price = products.get(position).getPrice();
                String link = products.get(position).getLink();
                String recommendation = products.get(position).getRecommendation();
                String image = products.get(position).getImageUrl();
                Intent i = new Intent(v.getContext(), SelectedProduct.class);
                i.putExtra("title", title);
                i.putExtra("description", description);
                i.putExtra("price", price);
                i.putExtra("link", link);
                i.putExtra("recommendation", recommendation);
                i.putExtra("image", image);
                v.getContext().startActivity(i);
            }
        });

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


