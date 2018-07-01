package com.example.android.journalapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.journalapp.model.Entry;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "journal.db";
    private static final String TABLE_NAME = "entries";

    // entries table column name
    private static final String KEY_ID = "id";
    private static final String KEY_ENTRY_SUBJECT = "entry_subject";
    private static final String KEY_ENTRY_MESSAGE = "entry_message";
    private static final String KEY_ENTRY_DATE = "entry_date";

    String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+
                    KEY_ENTRY_SUBJECT + " TEXT,"+
                    KEY_ENTRY_MESSAGE + " TEXT,"+
                    KEY_ENTRY_DATE + " TEXT "+
                    ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // create db again
        onCreate(db);
    }

    // Carry out the CRUD operations

    // Create an entry
    public void createEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_SUBJECT, entry.getSubject());
        values.put(KEY_ENTRY_MESSAGE, entry.getMessage());
        values.put(KEY_ENTRY_DATE, System.currentTimeMillis());

        db.insert(TABLE_NAME, null, values);
        Log.d("DATA INSERTED", "SUCCESS");
        db.close();
    }

    // Read an Entry
    public Entry readAnEntry(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_ENTRY_SUBJECT,
        KEY_ENTRY_MESSAGE, KEY_ENTRY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Prepare entry object
        Entry entry = new Entry(
                cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_ENTRY_SUBJECT)),
                cursor.getString(cursor.getColumnIndex(KEY_ENTRY_MESSAGE)),
                cursor.getString(cursor.getColumnIndex(KEY_ENTRY_DATE)));

        cursor.close();
        return entry;
    }

    // Read all entries
    public ArrayList<Entry> readAllEntries() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Entry> list = new ArrayList<>();
        String selectAll = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_ENTRY_SUBJECT)),
                        cursor.getString(cursor.getColumnIndex(KEY_ENTRY_MESSAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_ENTRY_DATE)));
                list.add(entry);
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Update a selected entry
    public int updateEntry(Entry entry) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ENTRY_SUBJECT, entry.getSubject());
        values.put(KEY_ENTRY_MESSAGE, entry.getMessage());
        values.put(KEY_ENTRY_DATE, System.currentTimeMillis());

        int updatedRow = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(entry.getId())});
        return updatedRow;
    }

    // TODO ---> Continue working on the project starting from writing the next function

    public void deleteEntry(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getEntriesCount() {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
    // TODO ----> Write a function to upload to Firebase in constant syncronism
}
