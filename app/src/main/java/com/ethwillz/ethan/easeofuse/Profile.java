package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    View v;
    TextView following;
    TextView followers;
    TextView displayName;
    ImageView profilePic;
    ArrayList<ProductInformation> items = new ArrayList<>();
    ArrayList<ProductInformation> products = new ArrayList<>();
    FirebaseUser user;
    RecyclerView savedGrid;
    private ProductGridAdapter mAdapter;
    GridLayoutManager layout;
    AppBarLayout appBarLayout;
    ProductInformation info;
    DatabaseReference mDatabase;
    Uri downloadUrl;
    String picturePath;
    UploadTask uploadTask;
    private final int RESULT_LOAD_IMAGE = 652;
    private final int RESULT_CROP_IMAGE = 489;
    File tempFile;
    Uri tempUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

        RecyclerView savedGrid = (RecyclerView) view.findViewById(R.id.savedGrid);
        appBarLayout = ((AppBarLayout) view.findViewById(R.id.appBar));

        savedGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        profilePic = (ImageView) view.findViewById(R.id.profilePic);
        profilePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                /*
                i.putExtra("crop", "true");
                i.putExtra("aspectX", 1);
                i.putExtra("aspectY", 1);
                i.putExtra("outputX", 1000);
                i.putExtra("outputY", 1000);
                i.putExtra("noFaceDetection", true);
                i.putExtra("return-data", true);
                */
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                return true;
            }
        });

        displayName = (TextView) view.findViewById(R.id.displayName);
        displayName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                /*
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                */

                getActivity().finishAffinity();

                return true;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database.Product all = new Database.Product();
        products = all.getAllProducts();

        v = getView();
        user = FirebaseAuth.getInstance().getCurrentUser();
        savedGrid = (RecyclerView) v.findViewById(R.id.savedGrid);
        followers = (TextView) v.findViewById(R.id.followers);
        following = (TextView) v.findViewById(R.id.following);

        populateGrid();

        final Typeface main = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Walkway Bold.ttf");
        final Typeface two = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Taken by Vultures Demo.otf");
        TextView savedTitle = (TextView) v.findViewById(R.id.savedTitle);

        displayName.setTypeface(two);
        savedTitle.setTypeface(main);
        followers.setTypeface(main);
        following.setTypeface(main);

        mDatabase.child("following").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following.setText("Following " + dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("followers").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followers.setText(dataSnapshot.getChildrenCount() + " followers");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(profilePic.getContext()).load(dataSnapshot.child("imageUrl").getValue().toString()).into(profilePic);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if(user != null){
            displayName.setText(user.getDisplayName());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            //Gets the data for the image
            Uri selectedImage = data.getData();

            try {
                tempFile = File.createTempFile("crop", "png", Environment.getExternalStorageDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempUri = Uri.fromFile(tempFile);
            this.getContext().grantUriPermission("com.android.camera",tempUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setData(selectedImage);
            intent.putExtra("outputX", 1000);
            intent.putExtra("outputY", 1000);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("output", tempUri);
            intent.putExtra("outputFormat", "PNG");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(intent, RESULT_CROP_IMAGE);
        }
        if(requestCode == RESULT_CROP_IMAGE && resultCode == RESULT_OK && null != data){
            /*
            Uri selected = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            //Sets up a cursor which queries for the filepath of the image
            Cursor cursor = getContext().getContentResolver().query(selected, filePathColumn, null, null, null);
            cursor.moveToFirst();

            //File path is determined from cursor
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            */

            //child is the name in the storage of the image and storage references are set up
            String child = user.getUid() + ".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://ease-of-use-9fa8a.appspot.com/ProfilePics");
            StorageReference productRef = storageRef.child(child);
            //Uri picture = Uri.fromFile(new File(path));

            //Picture is uploaded from phone using path
            uploadTask = productRef.putFile(tempUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    System.out.println(downloadUrl);
                    pictureSuccess(downloadUrl);
                }
            });
        }
    }

    public void pictureSuccess(Uri downloadUrl){
        System.out.println("---------------------------");
        mDatabase.child("users").child(user.getUid()).child("imageUrl").setValue(downloadUrl.toString());
        System.out.println("In database");
        tempFile.delete();
    }

    public void populateGrid(){
        //Gets the saved items for a user
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("saved").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    items.add(getProductInfo(d.getKey()));
                }
                //Sets adapter to the list of products
                layout = new GridLayoutManager(v.getContext(), 2);
                savedGrid.setLayoutManager(layout);
                savedGrid.setHasFixedSize(true);
                mAdapter = new ProductGridAdapter(items);
                savedGrid.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ProductInformation getProductInfo(String productID){
        for(int i = 0; i < products.size(); i++){
            System.out.println(products.get(i).getImageUrl());
            if(products.get(i).getProductID().equals(productID)) {
                return products.get(i);
            }
        }
        return info;
    }

}
