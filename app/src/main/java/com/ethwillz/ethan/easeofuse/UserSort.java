package com.ethwillz.ethan.easeofuse;

import java.util.ArrayList;
import java.util.Map;

public class UserSort {

    public static ArrayList<UserInformation> mergeSort(Map<String, Integer> users, ArrayList<UserInformation> keys){

        if(keys.size() <= 1){
            return keys;
        }
        ArrayList<UserInformation> left = new ArrayList<>();
        ArrayList<UserInformation> right = new ArrayList<>();

        int mid = keys.size() / 2;
        for (int i = 0; i < mid; i++) {
            left.add(keys.get(i));
        }
        for (int i = mid; i < keys.size(); i++) {
            right.add(keys.get(i));
        }
        left = mergeSort(users, left);
        right = mergeSort(users, right);
        return merge(users, left, right);
    }

    private static ArrayList<UserInformation> merge(Map<String, Integer> users, ArrayList<UserInformation> left, ArrayList<UserInformation> right){

        ArrayList<UserInformation> sorted = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < left.size() && j < right.size()){
            if(users.get(left.get(i).getUid()).compareTo(users.get(right.get(j).getUid())) > 0){
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
