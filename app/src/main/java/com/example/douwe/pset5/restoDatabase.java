package com.example.douwe.pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by douwe on 11/28/17.
 */

public class restoDatabase extends SQLiteOpenHelper {
    static restoDatabase instance;
    public restoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + "orders");
        onCreate(db);
    }

    public static restoDatabase getInstance(Context context) {
        if (instance == null){
            instance = new restoDatabase(context, "orders", null, 2);
        }
        return instance;
    }

    public void insert(String title, int price){
        ContentValues values = new ContentValues() ;
        values.put("price", price);
        values.put("name", title);
        values.put("amount", 1);

        SQLiteDatabase db = this.getWritableDatabase();
        String name = title;
        Cursor c = selectAll();
        if (c.moveToFirst()){
            do {
                // Passing values
                if (c.getString(c.getColumnIndex("name")).contains(title)){
                    System.out.println("found boi" + c.getString(c.getColumnIndex("name")) + c.getColumnIndex("name"));
                    System.out.println("found amnt" + Integer.toString(c.getInt(c.getColumnIndex("amount"))));

                    values.put("amount", c.getInt(c.getColumnIndex("amount")) + 1);
                    //db.update("orders", values,  "_id=?", new String[] { String.valueOf(c.getColumnIndex("_id")) });

                    update(c.getInt(c.getColumnIndex("_id")), (c.getInt(c.getColumnIndex("amount")) + 1));
                    return;
                } else {
                    System.out.println("passed: " + Integer.toString(c.getColumnIndex("name")));
                }
                // Do something Here with values
            } while(c.moveToNext());
        }

        db.insert("orders", null, values);
    }

    public Cursor selectAll(){
        SQLiteDatabase db =  this.getWritableDatabase();
        Cursor l0l = db.rawQuery("SELECT rowid _id,* FROM orders", null);
        return l0l;

    }

    public void clear(Context context) {
        SQLiteDatabase db =  this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);

    }

    public void update(Integer id, Integer new_status){
        System.out.println("i update with: " + Integer.toString(new_status));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues() ;
        values.put("amount", new_status);

        db.update("orders", values,  "_id=?", new String[] { String.valueOf(id) });
    }

}
