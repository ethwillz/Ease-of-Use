package com.ethwillz.ethan.easeofuse;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
                        String id = d.getKey();
                        System.out.println(id);
                        ProductInformation info = new ProductInformation(url, title, description, link, price, recommendation, type, style, poster, id);
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
}
