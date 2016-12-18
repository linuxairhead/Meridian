package com.lingoville.meridian.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Tenant DB. Manages database creation and version management.
 */
public class TenantsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TenantsDbHelper.class.getSimpleName();

    /*
    *  Name of the database file
    */
    private static final String DATABASE_NAME = "tenants.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link TenantsDbHelper}.
     *
     * @param context of the app
     */
      public TenantsDbHelper(Context context)
      {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the tenants table
        String SQL_CREATE_TENANT_TABLE =
                "CREATE TABLE " + TenantsContract.TenantEntry.Tenants_TABLE_NAME + " ("
                + TenantsContract.TenantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + "  INTEGER NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_FIRSTNAME + " TEXT NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_LASTNAME + " TEXT NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_PHONENUMBER + " INTEGER NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_MOVEIN + " INTEGER NOT NULL, "
                + TenantsContract.TenantEntry.COLUMN_MOVEOUT + " INTEGER NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TENANT_TABLE);

        // Create a String that contains the SQL statement to create the Finance table
        String SQL_CREATE_FINANCE_TABLE =
                "CREATE TABLE " + TenantsContract.TenantEntry.Finance_TABLE_NAME + " ("
                        + TenantsContract.TenantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " INTEGER NOT NULL, "
                        + TenantsContract.TenantEntry.COLUMN_DATE + " TEXT NOT NULL, "
                        + TenantsContract.TenantEntry.COLUMN_TRANSATION_TYPE + " TEXT NOT NULL, "
                        + TenantsContract.TenantEntry.COLUMN_AMOUNT +" INTEGER NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FINANCE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
