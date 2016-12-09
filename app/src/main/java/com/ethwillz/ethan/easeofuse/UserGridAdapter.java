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

public class UserGridAdapter extends RecyclerView.Adapter<UserGridAdapter.UserViewHolder>{
    List<UserInformation> users;
    ProductFilter filter;

    //Constructor for adapter which sets the list of products and initializes the filter
    public UserGridAdapter(ArrayList<UserInformation> people){
        users = people;
    }

    //Viewholder which establishes the UI components of the card view that are going to be populated
    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mDisplayName;
        private ImageView mImage;
        private TextView mUid;
        private TextView mUserName;

        public UserViewHolder(View v){
            super(v);

            final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Taken by Vultures Demo.otf");
            final Typeface two = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");

            mDisplayName = (TextView) v.findViewById(R.id.displayName);
            mImage = (ImageView) v.findViewById(R.id.image);
            mUid = (TextView) v.findViewById(R.id.uid);
            mUserName = (TextView) v.findViewById(R.id.userName);

            mDisplayName.setTypeface(main);
            mUserName.setTypeface(two);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            String uid = mUid.getText().toString();
            Intent i = new Intent(view.getContext(), UserProfile.class);
            i.putExtra("uid", uid);
            view.getContext().startActivity(i);
        }
    }

    @Override
    public UserGridAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = (View)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_cards, parent, false);

        return new UserViewHolder(v);
    }

    //Binds all information to UI element and sets up click listener
    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        holder.mDisplayName.setText(users.get(position).getDisplayName());
        holder.mUserName.setText(users.get(position).getUserName());
        holder.mUid.setText(users.get(position).getUid());

        Picasso.with(holder.mImage.getContext()).load(users.get(position).getImageUrl()).into(holder.mImage);
    }

    //Returns count of products in list
    public int getItemCount(){
        return users.size();
    }

    //Sets visible list to a new list
    public void setList(List<UserInformation> list){
        this.users = list;
    }

    //Filters products based on a query
    public void filterProducts(String query){
        filter.filter(query);
    }
}