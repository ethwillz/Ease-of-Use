package com.example.ethan.easeofuse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    //All required variables for classes below
    private int RESULT_LOAD_IMAGE = 652;
    private StorageReference picture;
    long numItems;
    Uri downloadUrl;
    String picturePath;
    UploadTask uploadTask;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates activity and sets view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        //Adds listener for the database to get the number of products stored whcih helps in naming scheme of new product
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numItems = dataSnapshot.child("products").getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    //Opens up users gallery
    public void addPic(View view){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            //Gets the data for the image
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            //Sets up a cursor which queries for the filepath of the image
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            //File path is determined from cursor
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //Textview notifies the user that an image has been successfully chosen
            TextView success = (TextView) findViewById(R.id.successText);
            success.setVisibility(View.VISIBLE);

        }
    }

    public void add(View view){
        //Sets up variables for the various fields the user entered information into
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //child is the name in the storage of the image and storage references are set up
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Saving Product");
        dialog.show();
        String child = numItems + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ease-of-use-9fa8a.appspot.com");
        StorageReference productRef = storageRef.child(child);
        Uri picture = Uri.fromFile(new File(picturePath));
        uploadTask = productRef.putFile(picture);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                pictureSuccess(downloadUrl);
            }
        });
    }

    public void pictureSuccess(Uri downloadUrl){
        EditText name = (EditText) findViewById(R.id.nameBox);
        name.setTextColor(Color.parseColor("#ffffff"));
        EditText link = (EditText) findViewById(R.id.linkBox);
        EditText price = (EditText) findViewById(R.id.priceBox);
        EditText description = (EditText) findViewById(R.id.descriptionBox);
        EditText recommendation = (EditText) findViewById(R.id.recommendationBox);

        String url = downloadUrl.toString();
        //Creates map of all information and then adds it to the database
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("name", name.getText().toString());
        newProduct.put("downloadUrl", url);
        newProduct.put("link", link.getText().toString());
        newProduct.put("description", description.getText().toString());
        newProduct.put("price", price.getText().toString());
        newProduct.put("recommendation", recommendation.getText().toString());
        mDatabase.child("products").child(numItems + "").setValue(newProduct);

        Intent i = new Intent(this, Main.class);
        startActivity(i);
    }
}
