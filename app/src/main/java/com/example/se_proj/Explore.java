//Explore Page

package com.example.se_proj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Explore extends AppCompatActivity {

    Button add_btn;

    Button profile;

    TextView textView2;
    String username;

    AdapterExplore customAdapter;

    ListView lv_explorePosts;

    DatabaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        lv_explorePosts = findViewById(R.id.lv_exploreposts);
        dataBaseHelper = new DatabaseHelper(Explore.this);


        add_btn = findViewById(R.id.addbtn3);

        profile = findViewById(R.id.mypostbtn3);

        List<userPosts> data = dataBaseHelper.getPosts();
        customAdapter = new AdapterExplore(this, data, dataBaseHelper);

        lv_explorePosts.setAdapter(customAdapter);


        add_btn.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), AddPost.class);
            startActivity(intent);

        });

        profile.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });


    }
}

class AdapterExplore extends BaseAdapter {
    DatabaseHelper dataBaseHelper;
    SQLiteDatabase db;
    List<userPosts> data;
    Context context;
    LayoutInflater inflater;

    public AdapterExplore(Context context, List<userPosts> data, DatabaseHelper dataBaseHelper) {
        this.context = context;
        this.data = data;
        this.dataBaseHelper = dataBaseHelper;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.exploreposts, null);
        ImageView img = view.findViewById(R.id.imageView3);
        EditText desc = view.findViewById(R.id.description2);
        TextView username = view.findViewById(R.id.username);
        TextView timestamp = view.findViewById(R.id.timestamp);
        ToggleButton likes = view.findViewById(R.id.toggleButton);
        TextView likescount= view.findViewById(R.id.likesCount);
        Uri myImgUri;
        String uriString;

        userPosts item = data.get(position);
        username.setText(item.getUserID());
        desc.setText(item.getDesc());
        timestamp.setText(item.getTimestamp());
        uriString = item.getImg_url();
        myImgUri=Uri.parse(uriString);
        img.setImageURI(myImgUri);

        int rows=getRowCount(item.getId());

        likescount.setText(rows+" likes");





        SharedPreferences prefs = context.getSharedPreferences("com.example.se_proj", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");


        boolean isLiked = isAlreadyLiked(name, item.getId());
        likes.setChecked(isLiked);
        likes.setBackgroundResource(isLiked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAlreadyLiked(name, item.getId())) {

                    String queryString = "INSERT INTO " + DatabaseHelper.TABLE_LIKES +
                            "(" + DatabaseHelper.COLUMN_USERLIKES + ", " + DatabaseHelper.COLUMN_PICID + ") VALUES (?, ?) ";
                    db = dataBaseHelper.getWritableDatabase();
                    db.execSQL(queryString, new Object[]{name, item.getId()});
                } else {

                    String deleteQueryString = "DELETE FROM " + DatabaseHelper.TABLE_LIKES +
                            " WHERE " + DatabaseHelper.COLUMN_USERLIKES + "=? AND " + DatabaseHelper.COLUMN_PICID + "=?";
                    db = dataBaseHelper.getWritableDatabase();
                    db.execSQL(deleteQueryString, new Object[]{name, item.getId()});
                }


                likes.setBackgroundResource(likes.isChecked() ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);


                notifyDataSetChanged();
            }
        });



        return view;
    }
    private boolean isAlreadyLiked(String username, int picId) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_LIKES, null,
                DatabaseHelper.COLUMN_USERLIKES + "=? AND " + DatabaseHelper.COLUMN_PICID + "=?",
                new String[]{username, String.valueOf(picId)},
                null, null, null);

        boolean alreadyLiked = cursor.moveToFirst();
        cursor.close();
        return alreadyLiked;
    }


    public int getRowCount(int picId) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        String countQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_LIKES +
                " WHERE "+DatabaseHelper.COLUMN_PICID +"="+picId;

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        cursor.close();

        return rowCount;
    }
}


