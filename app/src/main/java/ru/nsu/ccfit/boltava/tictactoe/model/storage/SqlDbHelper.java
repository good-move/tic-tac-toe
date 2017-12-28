package ru.nsu.ccfit.boltava.tictactoe.model.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexey on 24.12.17.
 */

public class SqlDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Token.db";

    public SqlDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + AccessTokenStorageContract.AccessTokenStorageEntry.TABLE_NAME + " (" +
                    AccessTokenStorageContract.AccessTokenStorageEntry._ID + " INTEGER PRIMARY KEY," +
                    AccessTokenStorageContract.AccessTokenStorageEntry.COLUMN_TOKEN + " TEXT" +
                    ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccessTokenStorageContract.AccessTokenStorageEntry.TABLE_NAME;
}
