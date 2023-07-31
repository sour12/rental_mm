package com.qr_wm.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qr_wm.data.DataAPI;
import com.qr_wm.view.MainView;
import com.qr_wm.view.listview.BoardData;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BoardDB extends SQLiteOpenHelper {
    public static String PREFIX_BOARD = "BOARD";
    String TABLE_NAME = "BOARD_TABLE";

    /* query rawdata */
    String COL_0 = "ID";
    String COL_1 = "NAME";
    String COL_2 = "NUMBER";
    String COL_3 = "STATUS";
    String COL_4 = "USER";
    String COL_5 = "C_DATA";
    String COL_6 = "M_DATA";
    String COL_7 = "DESCRIPTION";

    String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_1 + " TEXT NOT NULL, "
            + COL_2 + " TEXT NOT NULL, "
            + COL_3 + " TEXT NOT NULL, "
            + COL_4 + " TEXT NOT NULL, "
            + COL_5 + " TEXT NOT NULL, "
            + COL_6 + " TEXT NOT NULL, "
            + COL_7 + " TEXT);";

    public BoardDB(@Nullable Context context) {
        super(context, "BOARD.db", null, 1);
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

    public void insertData(String name, String number, String status, String user, String cdate, String mdate, String desc) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, number);
        contentValues.put(COL_3, status);
        contentValues.put(COL_4, user);
        contentValues.put(COL_5, cdate);
        contentValues.put(COL_6, mdate);
        contentValues.put(COL_7, desc);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteData(String name, String number) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "NAME=? AND NUMBER=?", new String[]{name, number});
    }

    public void updateData(String name, String number, String status, String user, String cdate, String mdate, String desc) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, number);
        contentValues.put(COL_3, status);
        contentValues.put(COL_4, user);
        contentValues.put(COL_5, cdate);
        contentValues.put(COL_6, mdate);
        contentValues.put(COL_7, desc);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "NAME=? AND NUMBER=?", new String[]{name, number});
    }

    public Cursor getAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " ORDER BY NAME ASC, NUMBER ASC", null);
    }

    public ArrayList<String> getData(String name, String number) {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where NAME=? AND NUMBER=?", new String[]{name, number});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
            arrayList.add("");
        } else {
            arrayList.add(cursor.getString(0));
            arrayList.add(cursor.getString(1));
            arrayList.add(cursor.getString(2));
            arrayList.add(cursor.getString(3));
            arrayList.add(cursor.getString(4));
            arrayList.add(cursor.getString(5));
            arrayList.add(cursor.getString(6));
            arrayList.add(cursor.getString(7));
        }

        return arrayList;
    }

    public int getID(String name, String number) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select id from " + TABLE_NAME + " where NAME=? AND NUMBER=?", new String[]{name, number});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        return id;
    }

    public String getStatus(String name, String number) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where NAME=? AND NUMBER=?", new String[]{name, number});
        cursor.moveToFirst();
        return cursor.getString(3);
    }

    public String getUser(String name, String number) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where NAME=? AND NUMBER=?", new String[]{name, number});
        cursor.moveToFirst();
        return cursor.getString(4);
    }

    public void updateArrayData(BoardDB boardDB) {
        Cursor cursor = boardDB.getAllData();
        MainView.boardList.clear();
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MainView.boardList.add(new BoardData(DataAPI.getSetName(cursor.getString(1), cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
        }
    }
}
