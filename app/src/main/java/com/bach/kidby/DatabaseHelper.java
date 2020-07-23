package com.bach.kidby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.IOException;
import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gameDatabase";
    public static final String CONTACTS_TABLE_NAME = "gameDetails";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE "+ CONTACTS_TABLE_NAME + "("
                            + CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + CONTACTS_COLUMN_NAME + " TEXT,"
                            + CONTACTS_COLUMN_PHOTO + " TEXT"
                            + ")"
            );
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String name, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHOTO, photo);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Game> array_list = new ArrayList<Game>();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + CONTACTS_TABLE_NAME + " ORDER BY " + CONTACTS_COLUMN_NAME + " DESC", null );
        if (cursor.moveToFirst()) {
            do {
                Game g = new Game();
                g.setName(cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME)));
                g.setPhoto(cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_PHOTO)));
                array_list.add(g);
            } while (cursor.moveToNext());
        }

        db.close();

        return array_list;
    }

    public void deleteContact(Game Game) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_NAME + " = ?",
                new String[]{String.valueOf(Game.getName())});
        db.close();
    }
}
