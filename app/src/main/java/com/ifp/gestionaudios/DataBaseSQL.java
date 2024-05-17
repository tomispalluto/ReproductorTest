package com.ifp.gestionaudios;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseSQL extends SQLiteOpenHelper {

    public DataBaseSQL(Context context) {
        super(context, "audio", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE media(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS media");
        onCreate(db);
    }
    public int numberOfNotes(){
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        num = (int) DatabaseUtils.queryNumEntries(db, "media");
        return num;
    }
    @SuppressLint("Range")
    public ArrayList<Audio> getAllAudios() {
        ArrayList<Audio> audios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM media ORDER BY id ASC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                String url = cursor.getString(cursor.getColumnIndex("url"));

                // Crear objeto Audio con los datos obtenidos de la base de datos
                Audio audio = new Audio(id, titulo, url);
                audios.add(audio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return audios;
    }

    public void insertarNota(String titulo, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("url", url);
        db.insert("media", null, values);
        db.close();
    }


}
