package ru.nsu.ccfit.boltava.tictactoe.model.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorageContract.AccessTokenStorageEntry.*;


public class AccessTokenStorage {

    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;

    public AccessTokenStorage(Context context) {
        writableDb = new SqlDbHelper(context).getWritableDatabase();
        readableDb = new SqlDbHelper(context).getReadableDatabase();
    }

    public String getAccessToken() {
        Cursor cursor = readableDb.query(
                TABLE_NAME,
                new String[]{COLUMN_TOKEN},
                null,
                null,
                null,
                null,
                null);


        if (!cursor.moveToFirst()) {
            throw new RuntimeException("Access token doesn't exist yet");
        }

        String accessToken = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN));
        System.out.println("Getting access token: " + accessToken);
        cursor.close();

        return accessToken;
    }


    public boolean setAccessToken(String accessToken) {
        removeAccessToken();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOKEN, accessToken);

        boolean didInsertSuccessfully = writableDb.insert(TABLE_NAME, null, values) >= 0;

        return didInsertSuccessfully;
    }

    public void removeAccessToken() {
        writableDb.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public boolean hasToken() {
        Cursor cursor = readableDb.query(
                TABLE_NAME,
                new String[]{"count(*)"},
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        boolean hasToken = cursor.getInt(0) > 0;
        if (hasToken) {
            System.out.println("Current token: " + getAccessToken());
        }
        cursor.close();

        return hasToken;
    }
}
