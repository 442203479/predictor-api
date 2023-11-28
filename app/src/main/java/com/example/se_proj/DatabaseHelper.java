package com.example.se_proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DishDive.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";


    public static final String TABLE_LIKES = "Likes";
    public static final String COLUMN_USERLIKES = "username";
    public static final String COLUMN_PICID = "picture_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTablePosts = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_USER_ID + " TEXT NOT NULL, " +
                COLUMN_TIMESTAMP + " INTEGER NOT NULL" +
                ")";
        db.execSQL(createTablePosts);

        String createTableUsers = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL," +
                "FOREIGN KEY ("+COLUMN_USERNAME+") REFERENCES "+TABLE_POSTS+"("+COLUMN_USER_ID+"))";
        db.execSQL(createTableUsers);


        String createTableLikes = "CREATE TABLE " + TABLE_LIKES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERLIKES + " TEXT NOT NULL, " +
                COLUMN_PICID + " INTEGER NOT NULL, " +
               " FOREIGN KEY ("+COLUMN_USERLIKES+") REFERENCES "+TABLE_POSTS+"("+COLUMN_USER_ID+")," +
                " FOREIGN KEY ("+COLUMN_PICID+") REFERENCES "+TABLE_POSTS+"("+COLUMN_ID+"))";
        db.execSQL(createTableLikes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePosts = "DROP TABLE IF EXISTS " + TABLE_POSTS;
        db.execSQL(dropTablePosts);

        String dropTableUsers = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableUsers);

        String dropTableLikes = "DROP TABLE IF EXISTS " + TABLE_LIKES;
        db.execSQL(dropTableLikes);

        onCreate(db);
    }

    public List<userPosts> getPosts() {
        List<userPosts> returnList = new ArrayList<>();
        // get data from database
        String queryString = "SELECT * FROM " + TABLE_POSTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // loop through cursor results
            do {
                int id = cursor.getInt(0);
                String img_url = cursor.getString(1);
                String desc = cursor.getString(2);
                String UserID = cursor.getString(3);
                String timestamp = cursor.getString(4);
                userPosts myPosts = new userPosts(UserID, img_url, desc, timestamp, id);
                returnList.add(myPosts);
            } while (cursor.moveToNext());
        } else {
            // nothing happens. no one is added.
        }
        // close
        cursor.close();
        db.close();
        return returnList;
    }

    public List<userPosts> getPosts(String username) {
        List<userPosts> returnList = new ArrayList<>();
        // get data from database
        String queryString = "SELECT * FROM " + TABLE_POSTS +
                " WHERE " + COLUMN_USER_ID + "='"+username+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // loop through cursor results
            do {
                int id = cursor.getInt(0);
                String img_url = cursor.getString(1);
                String desc = cursor.getString(2);
                String UserID = cursor.getString(3);
                String timestamp = cursor.getString(4);
                userPosts myPosts = new userPosts(UserID, img_url, desc, timestamp, id);
                returnList.add(myPosts);
            } while (cursor.moveToNext());
        } else {
            // nothing happens. no one is added.
        }
        // close
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the username or email already exists in the database
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_USERNAME + " = ? OR " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, email});
        if (cursor.getCount() > 0) {
            // Username or email already exists, return false to indicate registration failure
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();

        // Insert the new user into the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        long result = db.insert(TABLE_NAME, null, values);

        db.close();

        // Return true if the user was successfully inserted into the database
        return result != -1;
    }


    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.moveToFirst()) {

            cursor.close();
            db.close();
            return true;
        } else {

            cursor.close();
            db.close();
            return false;
        }
    }

}