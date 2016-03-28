package com.example.nounou.tp2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


public class MyContentProvider extends ContentProvider {


   //MySQLiteHelper dataBase;
    DatabaseHelper dbHelper;

    public static final Uri CONTENT_URI = Uri
            .parse("content://com.example.ayari.tp1.myContentProvider");

    // Nom de notre base de données
    public static final String CONTENT_PROVIDER_DB_NAME = "mycontentProvider.db";
    // Version de notre base de données
    public static final int CONTENT_PROVIDER_DB_VERSION = 5;
    // Nom de la table de notre base
    public static final String CONTENT_PROVIDER_TABLE_NAME = "client";
    // Le Mime de notre content provider, la premiére partie est toujours identique
    public static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.tutos.android.content.provider.client";




    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    private long getId(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null) {
            try {
                return Long.parseLong(lastPathSegment);
            } catch (NumberFormatException e) {
                Log.e("MyContentProvider", "Number Format Exception : " + e);
            }
        }
        return -1;
    }


    @Override

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (id < 0) {
            return  db.query(MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,
                    projection, selection, selectionArgs, null, null,
                    sortOrder);
        } else {
            return      db.query(MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,
                    projection, SharedIformation.Customers.Customer_ID+ "=" + id, null, null, null,
                    null);
        }
    }



    @Override
    public String getType(Uri uri) {
        return MyContentProvider.CONTENT_PROVIDER_MIME;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(MyContentProvider.CONTENT_PROVIDER_TABLE_NAME, null, values);

            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Failed to insert [%s] for unknown reasons.","ContentProvider", values, uri));
            } else {
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0)
                return db.delete(
                        MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,
                        selection, selectionArgs);
            else
                return db.delete(
                        MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,
                        SharedIformation.Customers.Customer_ID + "=" + id, selectionArgs);
        } finally {
            db.close();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update( MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,values, selection, selectionArgs);
            else
                return db.update(MyContentProvider.CONTENT_PROVIDER_TABLE_NAME,
                        values, SharedIformation.Customers.Customer_ID+ "=" + id, null);
        } finally {
            db.close();
        }
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        // Création à partir du Context, du Nom de la table et du numéro de version
        DatabaseHelper(Context context) {
            super(context, MyContentProvider.CONTENT_PROVIDER_DB_NAME, null, MyContentProvider.CONTENT_PROVIDER_DB_VERSION);
        }

        // Création des tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + MyContentProvider.CONTENT_PROVIDER_TABLE_NAME + " (" + SharedIformation.Customers.Customer_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SharedIformation.Customers.Customer_NAME + " VARCHAR(255)," + SharedIformation.Customers.Customer_SURNAME + " VARCHAR(255)," + SharedIformation.Customers.Customer_DATEOFBORTH + " VARCHAR(255)," + SharedIformation.Customers.Customer_TOWN + " VARCHAR(255)" + ");");
        }

        // Cette méthode sert à gérer la montée de version de notre base
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MyContentProvider.CONTENT_PROVIDER_TABLE_NAME);
            onCreate(db);
        }
    }

}
