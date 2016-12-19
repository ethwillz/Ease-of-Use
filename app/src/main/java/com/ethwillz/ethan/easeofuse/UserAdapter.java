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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    List<UserInformation> users;
    UserFilter filter;

    public UserAdapter(ArrayList<UserInformation> people){
        users = people;
        filter = new UserFilter(users, this);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mDisplayName;
        private ImageView mImage;
        private TextView mUid;
        private TextView mUserName;
        private TextView mImageUrl;

        public UserViewHolder(View v){
            super(v);

            final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Taken by Vultures Demo.otf");
            final Typeface two = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");

            mDisplayName = (TextView) v.findViewById(R.id.displayName);
            mImage = (ImageView) v.findViewById(R.id.image);
            mUid = (TextView) v.findViewById(R.id.uid);
            mUserName = (TextView) v.findViewById(R.id.userName);
            mImageUrl = (TextView) v.findViewById(R.id.imageUrl);

            mDisplayName.setTypeface(main);
            mUserName.setTypeface(two);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String uid = mUid.getText().toString();
            Intent i = new Intent(view.getContext(), UserOtherProfile.class);
            i.putExtra("uid", uid);
            i.putExtra("displayName", mDisplayName.getText().toString());
            i.putExtra("imageUrl", mImageUrl.getText().toString());
            view.getContext().startActivity(i);
        }
    }
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_cards, parent, false);

        return new UserAdapter.UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        holder.mDisplayName.setText(users.get(position).getDisplayName());
        holder.mUserName.setText(users.get(position).getUserName());
        holder.mUid.setText(users.get(position).getUid());
        holder.mImageUrl.setText(users.get(position).getImageUrl());

        Picasso.with(holder.mImage.getContext()).load(users.get(position).getImageUrl()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
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
