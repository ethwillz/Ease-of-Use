package com.ethwillz.ethan.easeofuse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUser extends Fragment {
    View v;
    DatabaseReference mDatabase;
    RecyclerView users;
    ArrayList<User> people = new ArrayList<>();
    private UserGridAdapter mAdapter;
    private UserAdapter mAdapter2;
    HashMap<String, Integer> top;
    EditText searchUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_user, container, false);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getView();
        populateGrid();

        users = (RecyclerView) v.findViewById(R.id.users);
        searchUser = (EditText) v.findViewById(R.id.searchUser);
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter2 = new UserAdapter(people);
                users.setAdapter(mAdapter2);
            }
        });
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = searchUser.getText().toString().toLowerCase();
                mAdapter.filterProducts(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void populateGrid(){
        //Populates the recyclerview with the name, description, and photo for all products in the database
        top = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    people.add(new User(d.getKey(), d.child("userName").toString(), d.child("displayName").toString()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
        mDatabase.child("saved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    String poster = d.child("user").getValue().toString();
                    top.put(poster, 1 + (top.containsKey(poster) ? top.get(poster) : 0));
                }
                //Sets adapter to the list of products
                mAdapter = new ProductGridAdapter(new Organize().doInBackground(new TaskParams(top, people)));
                users.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class Organize extends AsyncTask<Main.TaskParams, Void, ArrayList<User>> {
        ArrayList<User> sorted;
        @Override
        protected ArrayList<User> doInBackground(Main.TaskParams... taskParamses) {
            sorted = Sort.mergeSort(taskParamses[0].map, users);
            return sorted;
        }

        protected void onPostExecute(ArrayList<ProductInformation> result){

        }
    }

    public class TaskParams{
        HashMap<String, Integer> map;
        ArrayList<User> users = new ArrayList<>();

        public TaskParams(HashMap<String, Integer> map, ArrayList<User> users){
            this.map = map;
            this.users = users;
        }
    }
}
