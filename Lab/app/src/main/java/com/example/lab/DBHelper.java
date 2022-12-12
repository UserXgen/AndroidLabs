package com.example.lab;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean changePassword(String username, String password, String newPassword){
        int count;
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", newPassword);
        Cursor cursor = myDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        count = cursor.getCount();
        if(count == 1){
            myDB.update("users", contentValues, "username = ? and password = ?", new String[]{username, password});
            myDB.close();
            return true;
        }
        else {
            myDB.close();
            return false;
        }

    }

    public ArrayList<String> getDateList(){

        ArrayList<String> dateList = new ArrayList();
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor userCursor;
        userCursor = myDB.rawQuery("select " + "*" + " from " + "users", null);//Получаем все данные из таблицы TABLE(users)

        if (userCursor.moveToFirst()) {
            while (!userCursor.isAfterLast()) {
                @SuppressLint("Range") String username = userCursor.getString(userCursor.getColumnIndex("username"));
                @SuppressLint("Range") String password = userCursor.getString(userCursor.getColumnIndex("password"));
                dateList.add(username);
                dateList.add(password);
                userCursor.moveToNext();
            }
        }

        userCursor.close();
        return dateList;
    }

    public boolean deleteUser(String login){
        SQLiteDatabase myDB = this.getWritableDatabase();
        return myDB.delete("users", "username = ?", new String[]{login}) > 0;
    }

}
