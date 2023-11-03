//Profile Page
package com.example.se_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button add_btn;

    Button profile;

    Button exp_btn;
    Button btnLogOut;

    TextView myUsername;
    String username;

    userPostsAdapter customAdapter;

    ListView lv_myPostsList;

     DatabaseHelper dataBaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("com.example.se_proj", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");



        lv_myPostsList = findViewById(R.id.lv_myPosts);
        btnLogOut=findViewById(R.id.btnLogOut);
        dataBaseHelper = new DatabaseHelper(MainActivity.this);


        add_btn = findViewById(R.id.addbtn);
        exp_btn = findViewById(R.id.expbtn);

        profile = findViewById(R.id.mypostbtn);
        myUsername = findViewById(R.id.myUsername);

        myUsername.setText(name);

        username=myUsername.getText().toString();



        List<userPosts> data = dataBaseHelper.getPosts(username);
        customAdapter = new userPostsAdapter(this, data, dataBaseHelper);

        lv_myPostsList.setAdapter(customAdapter);


        add_btn.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), AddPost.class);
            startActivity(intent);

        });

        exp_btn.setOnClickListener(v ->

        {


            Intent intent = new Intent(getApplicationContext(), Explore.class);
            startActivity(intent);

        });

        btnLogOut.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });





    }



}

class userPosts
{
    String userID;
    String img_url;

    String desc;

    String timestamp;

    int id;

    public userPosts()
    {

    }
    public userPosts(String userID, String img_url, String desc, String timestamp, int id) {
        this.userID = userID;
        this.img_url = img_url;
        this.desc = desc;
        this.timestamp = timestamp;
        this.id=id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String img_url) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {


        return img_url + '\n' +
                "Description: " + desc + '\n' +
                "Timestamp: " + timestamp + '\n';
    }
}

 class userPostsAdapter extends BaseAdapter {
     DatabaseHelper dataBaseHelper;
     SQLiteDatabase db;
     List<userPosts> data;
     Context context;
     LayoutInflater inflater;

    public userPostsAdapter(Context context, List<userPosts> data, DatabaseHelper dataBaseHelper) {
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


        View view = inflater.inflate(R.layout.userposts, null);

        ImageView img = view.findViewById(R.id.imageView2);
        EditText desc = view.findViewById(R.id.description);
        TextView timestamp = view.findViewById(R.id.timestamp2);
        Button buttonEdit = view.findViewById(R.id.editbtn);
        Button buttonDelete = view.findViewById(R.id.deletebtn);
        Button buttonSave = view.findViewById(R.id.savbtn);
        Uri myImgUri;
        String uriString;

        userPosts item = data.get(position);





        desc.setText(item.getDesc());
        timestamp.setText(item.getTimestamp());
         uriString = item.getImg_url();
         myImgUri=Uri.parse(uriString);
         img.setImageURI(myImgUri);



                buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setVisibility(View.VISIBLE);
                desc.setEnabled(true);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryString = "DELETE FROM " + DatabaseHelper.TABLE_POSTS +
                        " WHERE " + DatabaseHelper.COLUMN_ID + " = " + item.getId();
                db = dataBaseHelper.getWritableDatabase();
                db.execSQL(queryString);

                data.remove(position);
                notifyDataSetChanged();

            }


        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setVisibility(View.INVISIBLE);
                desc.setEnabled(false);

                String newDescription = desc.getText().toString();

                item.setDesc(newDescription);

                String queryString = "UPDATE "+ DatabaseHelper.TABLE_POSTS+
                        " SET "+ DatabaseHelper.COLUMN_DESCRIPTION +"='"+ item.getDesc()+
                        "' WHERE "+DatabaseHelper.COLUMN_ID+" ="+item.getId();
                 db = dataBaseHelper.getWritableDatabase();
                db.execSQL(queryString);

            }
        });

        return view;
    }
}


