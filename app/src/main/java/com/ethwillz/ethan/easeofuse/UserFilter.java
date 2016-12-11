package com.ethwillz.ethan.easeofuse;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class UserFilter extends Filter{
    private List<UserInformation> users;
    private List<UserInformation> filteredUserList;
    private UserAdapter adapter;

    //Constructor for filter wich sets the original product list and adapter as well as intializes the new list
    public UserFilter(List<UserInformation> people, UserAdapter adapter){
        users = people;
        this.adapter = adapter;
        this.filteredUserList = new ArrayList<>();
    }

    //Performs filtering with one constraint
    @Override
    protected FilterResults performFiltering(CharSequence constraint){
        filteredUserList.clear();
        final FilterResults results = new Filter.FilterResults();

        for(final UserInformation user : users){
            if(user.getUserName().toLowerCase().contains(constraint) || user.getDisplayName().toLowerCase().contains(constraint)){
                filteredUserList.add(user);
            }
        }

        results.values = filteredUserList;
        results.count = filteredUserList.size();
        return results;
    }

    //Actually updates recyclerview with filtered products
    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results){
        adapter.setList(filteredUserList);
        adapter.notifyDataSetChanged();
    }
}
