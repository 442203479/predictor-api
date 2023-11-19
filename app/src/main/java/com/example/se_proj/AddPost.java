package com.example.se_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPost extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    Button profile;

    Button exp_btn;


    TextView img_url;
    EditText user_desc;

    ImageView img_view;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        SharedPreferences prefs = getSharedPreferences("com.example.se_proj", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");

        profile = findViewById(R.id.mypostbtn2);
        exp_btn = findViewById(R.id.expbtn2);




        exp_btn.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });


        profile.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });
    }


    public void onPostButtonClick(View view) {
        // Check if an image has been selected
        if (selectedImageUri != null) {
            img_url = findViewById(R.id.img_url);
            user_desc = findViewById(R.id.user_description);


            SharedPreferences prefs = getSharedPreferences("com.example.se_proj", Context.MODE_PRIVATE);
            String name = prefs.getString("name", "");

            String imageUrl = img_url.getText().toString();
            String description = user_desc.getText().toString();
            String myUsername = name;

            // Insert data into the database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
            String date = sdf.format(new Date());
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl);
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
            values.put(DatabaseHelper.COLUMN_USER_ID, myUsername);
            values.put(DatabaseHelper.COLUMN_TIMESTAMP, date);

            long newRowId = db.insert(DatabaseHelper.TABLE_POSTS, null, values);

            db.close();

            // Provide user feedback, such as a success message
            Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();


            //after posting, redirect user to their profile to see the new post
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);



        } else {
            // Inform the user to select an image before posting
            Toast.makeText(this, "Please select an image before posting.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onImagePickerButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            selectedImageUri = data.getData();
            img_url=findViewById(R.id.img_url);
            img_view=findViewById(R.id.imageView);
            img_url.setText(selectedImageUri.toString());
            img_view.setImageURI(selectedImageUri);


        }
    }


}