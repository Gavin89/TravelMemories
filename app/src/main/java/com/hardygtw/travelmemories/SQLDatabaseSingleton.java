package com.hardygtw.travelmemories;

import android.content.Context;

public class SQLDatabaseSingleton {

    private static DatabaseHelper sInstance;

    private SQLDatabaseSingleton() {}

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
}
