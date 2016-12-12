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
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates activity and sets view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Walkway Bold.ttf");
        TextView nameText = (TextView) findViewById(R.id.nameText);
        TextView linkText = (TextView) findViewById(R.id.linkText);
        TextView priceText = (TextView) findViewById(R.id.priceText);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionText);
        TextView recommendationText = (TextView) findViewById(R.id.recommendationText);
        EditText nameBox = (EditText) findViewById(R.id.nameBox);
        EditText linkBox = (EditText) findViewById(R.id.linkBox);
        EditText descriptionBox = (EditText) findViewById(R.id.priceBox);
        EditText recommendationBox = (EditText) findViewById(R.id.recommendationBox);
        Button enter = (Button) findViewById(R.id.enter);
        Button upload = (Button) findViewById(R.id.upload);
        nameText.setTypeface(main);
        linkText.setTypeface(main);
        priceText.setTypeface(main);
        descriptionText.setTypeface(main);
        recommendationText.setTypeface(main);
        nameBox.setTypeface(main);
        linkBox.setTypeface(main);
        descriptionBox.setTypeface(main);
        recommendationBox.setTypeface(main);
        enter.setTypeface(main);
        upload.setTypeface(main);

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
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            //Gets the data for the image
            Uri selectedImage = data.getData();
            this.grantUriPermission("com.android.camera",selectedImage,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
        this.grantUriPermission("com.android.camera",picture,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

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

        //Creates map of all information and then adds it to the database
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("user", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        newProduct.put("name", name.getText().toString());
        newProduct.put("downloadUrl", downloadUrl.toString());
        newProduct.put("link", link.getText().toString());
        newProduct.put("description", description.getText().toString());
        newProduct.put("price", price.getText().toString());
        newProduct.put("recommendation", recommendation.getText().toString());
        newProduct.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.child("products").child(numItems + "").setValue(newProduct);

        //Returns to products_hot activity after new product entered into database
        Intent i = new Intent(this, MainView.class);
        startActivity(i);
    }
}
