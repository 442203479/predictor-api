//Explore Page

package com.example.se_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


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
        customAdapter = new AdapterExplore(this, data);

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
    List<userPosts> data;
    Context context;
    LayoutInflater inflater;

    public AdapterExplore(Context context, List<userPosts> data) {
        this.context = context;
        this.data = data;
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
        Uri myImgUri;
        String uriString;

        userPosts item = data.get(position);
        username.setText(item.getUserID());
        desc.setText(item.getDesc());
        timestamp.setText(item.getTimestamp());
        uriString = item.getImg_url();
        myImgUri=Uri.parse(uriString);



        img.setImageURI(myImgUri);



        return view;
    }
}


