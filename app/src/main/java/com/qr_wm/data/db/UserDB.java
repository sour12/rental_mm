package com.qr_wm.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qr_wm.data.DataAPI;
import com.qr_wm.view.MainView;
import com.qr_wm.view.listview.BoardData;
import com.qr_wm.view.listview.UserData;

import androidx.annotation.Nullable;

public class UserDB extends SQLiteOpenHelper {
    String TABLE_NAME = "USER_TABLE";
    /* query rawdata */
    String COL_0 = "ID";
    String COL_1 = "NAME";
    String COL_2 = "PHONE";
    String COL_3 = "EMAIL";
    String COL_4 = "USER_GROUP";

    String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_1 + " TEXT NOT NULL, "
            + COL_2 + " TEXT NOT NULL, "
            + COL_3 + " TEXT NOT NULL, "
            + COL_4 + " TEXT);";

    public UserDB(@Nullable Context context) {
        super(context, "USER.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String name, String phone, String email, String group) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, phone);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, group);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteData(String phone) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "PHONE=?", new String[]{phone});
    }

    public void updateData(String name, String phone, String email, String group) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, phone);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, group);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "PHONE=?", new String[]{phone});
    }

    public Cursor getAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " ORDER BY NAME ASC", null);
    }

    public int getID(String name, String phone) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select id from " + TABLE_NAME + " where NAME=? AND PHONE=?", new String[]{name, phone});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        return id;
    }

    public void updateArrayData(UserDB userDB) {
        Cursor cursor = userDB.getAllData();
        MainView.userList.clear();
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MainView.userList.add(new UserData(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }
    }
}
