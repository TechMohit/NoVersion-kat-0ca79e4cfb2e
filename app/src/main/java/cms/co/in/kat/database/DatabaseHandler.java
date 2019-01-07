package cms.co.in.kat.database;

/*
  Created by Happy on 13-04-2017.
 */


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import cms.co.in.kat.activity.DashBoardCaseList;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ANDROID_KAT";

    // Contacts table name
    private static final String TABLE_NAME = "USER";

    private static final String TABLE_APP_DETAILS = "APP_DETAILS";
    private static final String KEY_APP_VERSION = "versionCode";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERID = "user_id";

    private static final String TABLE_API_GETDETAILS = "GET_DETAILS";
    private static final String KEY_RESULT = "json_result";
    private Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        try {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"
                    + KEY_PASSWORD + " TEXT," + KEY_USERID + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);

            String CREATE_APP_DETAILS_TABLE = "CREATE TABLE " + TABLE_APP_DETAILS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_APP_VERSION + " TEXT" + ")";
            db.execSQL(CREATE_APP_DETAILS_TABLE);


            String CREATE_API_GET_DETAILS_TABLE = "CREATE TABLE " + TABLE_API_GETDETAILS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_RESULT + " TEXT" + ")";
            db.execSQL(CREATE_API_GET_DETAILS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_API_GETDETAILS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        onCreate(db);
    }

//    public boolean addContact(String user_name, String password, String user_id) {
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(KEY_USER_NAME, user_name);
//            values.put(KEY_PASSWORD, password);
//            values.put(KEY_USERID, user_id);
//
//            long rowInserted = db.insert(TABLE_NAME, null, values);
//            db.close();
//            if (rowInserted != -1)
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public boolean updateContact(String user_name, String password, String user_id) {
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_USER_NAME, user_name);
//            values.put(KEY_USERID, user_id);
//            values.put(KEY_PASSWORD, password);
//
//            int result = db.update(TABLE_NAME, values, KEY_USER_NAME + " = ?",
//                    new String[]{user_name});
//            if (result > 0)
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//
//    }
//
//    @Nullable
//    public List<String> getContact(String user_name) {
//        try {
//            List<String> list = new ArrayList<>();
//            SQLiteDatabase db = this.getReadableDatabase();
//
//            Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
//                            KEY_USER_NAME, KEY_PASSWORD}, KEY_USER_NAME + "=?",
//                    new String[]{user_name}, null, null, null, null);
//            if (cursor != null)
//                cursor.moveToFirst();
//            list.add(cursor.getString(0));
//            list.add(cursor.getString(1));
//            list.add(cursor.getString(2));
//            list.add(cursor.getString(3));
//
//            db.close();
//            return list;
//        }catch (Exception e){
//
//        }
//        return null ;
//    }

    // Getting All Contacts
    @NonNull
    public List<String> getAllContacts() {
        List<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                contactList.add(cursor.getString(0));
                contactList.add(cursor.getString(1));
                contactList.add(cursor.getString(2));
                contactList.add(cursor.getString(3));

            } while (cursor.moveToNext());
        }

        db.close();
        // return contact list
        return contactList;
    }

    // Deleting single contact
    public void deleteContact(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{user_name});
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    public void getDetailsInsert(Context ctx, String jsonResult) {
        this.context = ctx;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_API_GETDETAILS, null);
        ContentValues cv = new ContentValues();
        cv.put(KEY_RESULT, jsonResult);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String id = String.valueOf(c.getLong(0));
            Log.e("***", "****contact name install update " + database.update(TABLE_API_GETDETAILS, cv, "id = ? ", new String[]{id}));

        } else {
            Log.e("***", "****contact name install inset" + database.insert(TABLE_API_GETDETAILS, null, cv));

        }
        database.close();
    }

    public boolean getDetailsCheck(Context ctx) {
        this.context = ctx;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_API_GETDETAILS, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            try {
                String res = c.getString(1);
                if (res != null && !res.equalsIgnoreCase("")) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            database.close();
            return false;

        } else {
            database.close();

            return true;
        }
    }

    public String getDetailsAll(Context ctx) {
        this.context = ctx;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_API_GETDETAILS, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            try {
                String res = c.getString(1);

                return res;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteTable(Context ctx) {
        try {
            this.context = ctx;

            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(TABLE_API_GETDETAILS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
