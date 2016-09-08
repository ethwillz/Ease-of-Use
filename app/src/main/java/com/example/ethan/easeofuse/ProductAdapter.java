package com.example.ethan.easeofuse;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private DatabaseReference mDatabase;
    ArrayList<ProductInformation> products;
    ArrayList<ProductInformation> visibleProducts = new ArrayList<>();

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

    public ProductAdapter(ArrayList<ProductInformation> items){
        products = items;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = (View)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cards, parent, false);

        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        holder.mTitle.setText(products.get(position).getTitle());
        holder.mDescription.setText(products.get(position).getDescription());
        holder.mPrice.setText(products.get(position).getPrice());
        holder.mLink.setText(products.get(position).getLink());
        holder.mRecommendation.setText(products.get(position).getRecommendation());
        holder.mUrl.setText(products.get(position).getImageUrl());

        Picasso.with(holder.itemView.getContext()).load(products.get(position).getImageUrl()).placeholder(R.drawable.logo).into(holder.mImage);

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

    public int getItemCount(){
        return products.size();
    }

    public void flushFilter(){
        visibleProducts.addAll(products);
        notifyDataSetChanged();
    }

    public void setFilter(String style, String type){
        for(ProductInformation product: products){
            if(style.equals("all")){
                if (product.getType().equals(type)) {
                    visibleProducts.add(product);
                }
            }
            else if(type.equals("all")){
                if (product.getStyle().equals(style)) {
                    visibleProducts.add(product);
                }
            }
            else if(style.equals("all") && type.equals("all")){
                visibleProducts.add(product);
            }
            else {
                if (product.getStyle().equals(style) && product.getType().equals(type)) {
                    visibleProducts.add(product);
                }
            }
        }
        products.clear();
        products.addAll(visibleProducts);
        notifyDataSetChanged();
    }

}
