package com.ethwillz.ethan.easeofuse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

class Sort {
    public static void mergeSort(Map<String, Integer> products, ArrayList<String> keys, Comparator<String> keyComp){

        if(keys.size() <= 1){
            return;
        }
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();

        int mid = keys.size() / 2;
        for (int i = 0; i < mid; i++) {
           left.add(keys.get(i));
        }
        for (int i = mid; i < keys.size(); i++) {
            right.add(keys.get(i));
        }
        mergeSort(products, left, keyComp);
        mergeSort(products, right, keyComp);
        merge(products, left, right, keyComp);
    }

    private static ArrayList<String> merge(Map<String, Integer> products, ArrayList<String> left, ArrayList<String> right, Comparator<String> keycomp){

        ArrayList<String> sorted = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < left.size() && j < right.size()){
            if(products.get(left.get(i)) < products.get(right.get(j))){
                sorted.add(left.get(i));
                i++;
            }
            else{
                sorted.add(right.get(j));
                j++;
            }
        }
        return sorted;
    }
}
