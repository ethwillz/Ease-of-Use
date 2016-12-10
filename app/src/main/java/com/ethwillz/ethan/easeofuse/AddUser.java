package com.ethwillz.ethan.easeofuse;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AddUser extends Fragment {
    View v;
    DatabaseReference mDatabase;
    RecyclerView users;
    ArrayList<UserInformation> people = new ArrayList<>();
    ArrayList<UserInformation> popPeople = new ArrayList<>();
    private UserGridAdapter mAdapter;
    private UserAdapter mAdapter2;
    HashMap<String, Integer> top;
    EditText searchUser;
    AppBarLayout appBarLayout;
    TextView title;
    GridLayoutManager layout;
    LinearLayoutManager mLayoutManager;
    Database.User popUsers = new Database.User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_user, container, false);

        users = (RecyclerView) view.findViewById(R.id.userCardView);
        title = (TextView) view.findViewById(R.id.title);
        searchUser = (EditText) view.findViewById(R.id.searchUser);

        appBarLayout = ((AppBarLayout) view.findViewById(R.id.usersAppBar));

        users.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(scrollDy==0&&(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
                {
                    appBarLayout.setExpanded(true);
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        v = getView();
        final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");
        title.setTypeface(main);
        searchUser.setTypeface(main);

        MobileAds.initialize(v.getContext(), "ca-app-pub-5566797500264030~3966962306");
        final AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1BF89AB15C45335B1CA8BCE94927DA8C").build();
        mAdView.loadAd(adRequest);

        populateGrid();

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutManager = new LinearLayoutManager(getActivity());
                users.setLayoutManager(mLayoutManager);
                users.setHasFixedSize(true);
                mAdapter2 = new UserAdapter(popUsers.getAllUsers());
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
                mAdapter2.filterProducts(text);
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
        mDatabase.child("saved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Iterator<DataSnapshot> userIDs = d.getChildren().iterator();
                    for(int i = 0; i < d.getChildrenCount(); i++){
                        String userID = userIDs.next().getValue().toString();
                        top.put(userID, 1 + (top.containsKey(userID) ? top.get(userID) : 0));
                    }
                }

                //Sets adapter to the list of products
                layout = new GridLayoutManager(v.getContext(), 3);
                users.setLayoutManager(layout);
                users.setHasFixedSize(true);
                mAdapter = new UserGridAdapter(new Organize().doInBackground(new TaskParams(top, popPeople)));
                users.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class Organize extends AsyncTask<TaskParams, Void, ArrayList<UserInformation>> {
        ArrayList<UserInformation> sorted = new ArrayList<>();
        @Override
        protected ArrayList<UserInformation> doInBackground(TaskParams... taskParamses) {
            sorted = popUsers.getPopUsers(taskParamses[0].map);
            sorted = UserSort.mergeSort(taskParamses[0].map, sorted);
            System.out.println(sorted);
            return sorted;
        }

        protected void onPostExecute(ArrayList<ProductInformation> result){
        }
    }

    public class TaskParams{
        HashMap<String, Integer> map;
        ArrayList<UserInformation> users = new ArrayList<>();

        public TaskParams(HashMap<String, Integer> map, ArrayList<UserInformation> users){
            this.map = map;
            this.users = users;
        }
    }
}
