package com.ethwillz.ethan.easeofuse;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;

//Extends Filter class to filter the recyclerview based on different criteria
public class ProductFilter extends Filter {
    private List<ProductInformation> productList;
    private List<ProductInformation> filteredProductList;
    private ProductAdapter adapter;

    //Constructor for filter wich sets the original product list and adapter as well as intializes the new list
    public ProductFilter(List<ProductInformation> productList, ProductAdapter adapter){
        this.productList = productList;
        this.adapter = adapter;
        this.filteredProductList = new ArrayList<>();
    }

    //Performs filtering with one constraint
    @Override
    protected FilterResults performFiltering(CharSequence constraint){
        filteredProductList.clear();
        final FilterResults results = new FilterResults();

        for(final ProductInformation item : productList){
            if(item.getTitle().toLowerCase().contains(constraint) || item.getStyle().toLowerCase().contains(constraint) ||
            item.getType().toLowerCase().contains(constraint) || item.getDescription().toLowerCase().contains(constraint)){
                filteredProductList.add(item);
            }
        }

        results.values = filteredProductList;
        results.count = filteredProductList.size();
        return results;
    }

    //Actually updates recyclerview with filtered products
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results){
        adapter.setList(filteredProductList);
        adapter.notifyDataSetChanged();
    }


}
