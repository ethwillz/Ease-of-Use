package com.ethwillz.ethan.easeofuse;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public UserViewHolder(View v){
            super(v);
        }

        @Override
        public void onClick(View view) {

        }
    }
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
