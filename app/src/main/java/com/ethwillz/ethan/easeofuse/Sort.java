package com.ethwillz.ethan.easeofuse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

class Sort {
    public static ArrayList<ProductInformation> mergeSort(Map<String, Integer> products, ArrayList<ProductInformation> keys){

        if(keys.size() <= 1){
            return keys;
        }
        ArrayList<ProductInformation> left = new ArrayList<>();
        ArrayList<ProductInformation> right = new ArrayList<>();

        int mid = keys.size() / 2;
        for (int i = 0; i < mid; i++) {
            left.add(keys.get(i));
        }
        for (int i = mid; i < keys.size(); i++) {
            right.add(keys.get(i));
        }
        mergeSort(products, left);
        mergeSort(products, right);
        return merge(products, left, right);
    }

    private static ArrayList<ProductInformation> merge(Map<String, Integer> products, ArrayList<ProductInformation> left, ArrayList<ProductInformation> right){

        ArrayList<ProductInformation> sorted = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < left.size() && j < right.size()){
            if(products.get(left.get(i).getProductID()).compareTo(products.get(right.get(j).getProductID())) > 0){
                sorted.add(left.get(i));
                i++;
            }
            else{
                sorted.add(right.get(j));
                j++;
            }
        }

        while(i < left.size()){
            sorted.add(left.get(i));
            i++;
        }

        while(j < right.size()){
            sorted.add(right.get(j));
            j++;
        }
        return sorted;
    }
}
