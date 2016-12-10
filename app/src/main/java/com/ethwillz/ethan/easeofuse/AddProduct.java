package com.ethwillz.ethan.easeofuse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    //All required variables for classes below
    private int RESULT_LOAD_IMAGE = 652;
    long numItems;
    Uri downloadUrl;
    UploadTask uploadTask;
    String picturePath;

    DatabaseReference mDatabase;
    String style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates activity and sets view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Barrio Santo.ttf");
        Button enter = (Button) findViewById(R.id.enter);
        enter.setTypeface(main);
        Button upload = (Button) findViewById(R.id.upload);
        upload.setTypeface(main);

        Spinner spinner = (Spinner) findViewById(R.id.types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Adds listener for the database to get the number of products stored which helps in naming scheme of new product
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numItems = dataSnapshot.child("products").getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        }
    }

    public void add(View view){
        //Sets up variables for the various fields the add_user entered information into
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Progress spinner to let add_user know application is working
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Saving Product");
        dialog.show();

        //child is the name in the storage of the image and storage references are set up
        String child = numItems + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ease-of-use-9fa8a.appspot.com");
        StorageReference productRef = storageRef.child(child);
        Uri picture = Uri.fromFile(new File(picturePath));

        //Picture is uploaded from phone using path
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
        //Finds UI elements which the add_user entered information into
        EditText name = (EditText) findViewById(R.id.nameBox);
        EditText link = (EditText) findViewById(R.id.linkBox);
        EditText price = (EditText) findViewById(R.id.priceBox);
        EditText description = (EditText) findViewById(R.id.descriptionBox);
        EditText recommendation = (EditText) findViewById(R.id.recommendationBox);

        RadioButton street = (RadioButton) findViewById(R.id.street);
        RadioButton dress = (RadioButton) findViewById(R.id.classy);
        if (street.isChecked())
            style = "street";
        else
            style = "class";

        Spinner spinner = (Spinner) findViewById(R.id.types);
        String type = spinner.getSelectedItem().toString();

        //Creates map of all information and then adds it to the database
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("add_user", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        newProduct.put("name", name.getText().toString());
        newProduct.put("downloadUrl", downloadUrl.toString());
        newProduct.put("link", link.getText().toString());
        newProduct.put("description", description.getText().toString());
        newProduct.put("price", price.getText().toString());
        newProduct.put("recommendation", recommendation.getText().toString());
        newProduct.put("style", style);
        newProduct.put("type", type);
        mDatabase.child("products").child(numItems + "").setValue(newProduct);

        //Returns to products_hot activity after new product entered into database
        Intent i = new Intent(this, MainView.class);
        startActivity(i);
    }
}
