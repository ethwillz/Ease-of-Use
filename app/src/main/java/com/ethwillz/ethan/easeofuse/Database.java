package com.ethwillz.ethan.easeofuse;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database {
    static class Product{
        private ArrayList<ProductInformation> items = new ArrayList<>();
        DatabaseReference mDatabase;

        public Product() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String url = d.child("downloadUrl").getValue().toString();
                        String title = d.child("name").getValue().toString();
                        String description = d.child("description").getValue().toString();
                        String link = d.child("link").getValue().toString();
                        String price = d.child("price").getValue().toString();
                        String recommendation = d.child("recommendation").getValue().toString();
                        String type = d.child("type").getValue().toString();
                        String style = d.child("style").getValue().toString();
                        String poster = d.child("user").getValue().toString();
                        String uid = d.child("uid").getValue().toString();
                        String id = d.getKey();
                        ProductInformation info = new ProductInformation(url, title, description, link, price, recommendation, type, style, poster, id, uid);
                        items.add(info);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        public ArrayList<ProductInformation> getAllProducts(){
            return items;
        }

        public ArrayList<ProductInformation> getSavedProducts(HashMap<String, Integer> map){
            ArrayList<ProductInformation> saved = new ArrayList<>();
            for(int i = 0; i < items.size(); i++){
                if(map.containsKey(items.get(i).getProductID())){
                    saved.add(items.get(i));
                }
            }
            return saved;
        }
    }

    static class User{
        private ArrayList<UserInformation> users = new ArrayList<>();
        DatabaseReference mDatabase;

        public User(){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Iterator<DataSnapshot> userIDs = dataSnapshot.getChildren().iterator();
                        for(int i = 0; i < d.getChildrenCount(); i++) {
                            DataSnapshot user = userIDs.next();
                            String userName = user.child("userName").getValue().toString();
                            String imageUrl = user.child("imageUrl").getValue().toString();
                            String displayName = user.child("displayName").getValue().toString();
                            users.add(new UserInformation(d.getKey(), userName, imageUrl, displayName));
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        public ArrayList<UserInformation> getAllUsers(){
            return users;
        }

        public ArrayList<UserInformation> getPopUsers(HashMap<String, Integer> top){
            ArrayList<UserInformation> popular = new ArrayList<>();
            for(int i = 0; i < users.size(); i++){;
                if(top.containsKey(users.get(i).getUid())){
                    popular.add(users.get(i));
                }
            }
            return popular;
        }
    }
}
