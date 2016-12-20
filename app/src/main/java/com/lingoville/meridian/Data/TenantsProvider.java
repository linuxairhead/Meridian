package com.lingoville.meridian.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.util.IllegalFormatException;

/**
 * {@link ContentProvider} for Meridian app.
 */
public class TenantsProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = TenantsProvider.class.getSimpleName();

    private TenantsDbHelper mDbHelper;
    private static final int Tenants = 100;
    private static final int Tenants_ID = 101;

    private static final int Finance = 200;
    private static final int Finance_ID = 201;

    private static final int RoomInfo = 300;
    private static final int RoomInfo_Vacant = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_Tenant, Tenants );
        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_Tenant+"/#", Tenants_ID );

        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_Finance, Finance );
        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_Finance+"/#", Finance_ID );

        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_RoomInfo, RoomInfo );
        sUriMatcher.addURI(TenantsContract.CONTENT_AUTHORITY, TenantsContract.PATH_RoomInfo+"/#", RoomInfo_Vacant );
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {

        Log.d(LOG_TAG, "onCreate");

        mDbHelper = new TenantsDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Log.d(LOG_TAG, " query :" + uri);

        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Log.d(LOG_TAG, " query :" + uri +"match is" + match);

        switch (match) {
            /*
            * This will perform a query on the Tenants table
            */
            case Tenants:
                cursor = db.query(TenantsContract.TenantEntry.Tenants_TABLE_NAME, projection, selection, selectionArgs,
                        null,   null,   sortOrder);
                if (cursor == null)
                    Log.d(LOG_TAG, " query >" + uri+"cursor is null");
                break;

            /*
            * This will perform a query on the Tenants table where the _id equals
            */
            case Tenants_ID:
                selection = TenantsContract.TenantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(TenantsContract.TenantEntry.Tenants_TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            /*
            * This will perform a query on the Finance table
            */
            case Finance:
                cursor = db.query(TenantsContract.TenantEntry.Finance_TABLE_NAME, projection, selection, selectionArgs,
                        null,   null,   sortOrder);
                break;

            /*
            * This will perform a query on the Finance table where the _id equals
            */
            case Finance_ID:
                selection = TenantsContract.TenantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(TenantsContract.TenantEntry.Finance_TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            /*
            * This will perform a query on the RoomInfo table
            */
            case RoomInfo:
                cursor = db.query(TenantsContract.TenantEntry.ROOM_TABLE_NAME, projection, selection, selectionArgs,
                        null,   null,   sortOrder);
                break;

            case RoomInfo_Vacant:
                selection = TenantsContract.TenantEntry.COLUMN_Vancant + "=?";
                selectionArgs = new String[] { "false" };
                cursor = db.query(TenantsContract.TenantEntry.ROOM_TABLE_NAME, projection, selection, selectionArgs,
                        null,   null,   sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        /*
         * Set notification URI on the Cursor, so we know what content URI the Cursor was created for.
         * If the data at this URI changes, then we know we need to update the cursor.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) throws IllegalArgumentException{
        Log.d(LOG_TAG, " insert >" + uri+"<");
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Tenants:
                return insertTenant(uri, contentValues);
            case Finance:
                return insertFinance(uri, contentValues);
            case RoomInfo:
                return insertRoom(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertRoom(Uri uri, ContentValues values){

        Log.d(LOG_TAG, " roomTableInit");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int roomNum = values.getAsInteger(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER);
        if(roomNum == 0) {
            throw new IllegalArgumentException("Room need proper room number");
        }

        boolean vacancy = values.getAsBoolean(TenantsContract.TenantEntry.COLUMN_Vancant);

        // insert the content value to the database
       long id = db.insert(TenantsContract.TenantEntry.ROOM_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

         /*
         *  Notify all listener that the data has changed for the pet content URI
         */
            getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertTenant(Uri uri, ContentValues values)
    {
        Log.d(LOG_TAG, " insertTenant >" + uri+"<");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String roomNum = values.getAsString(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER);
        if(roomNum == null) {
            throw new IllegalArgumentException("Tenant requires a room number" );
        }

        String fName = values.getAsString(TenantsContract.TenantEntry.COLUMN_FIRSTNAME);
        if(fName == null) {
            throw new IllegalArgumentException("Tenant requires a first name");
        }

        String lName = values.getAsString(TenantsContract.TenantEntry.COLUMN_LASTNAME);
        if(lName == null) {
            throw new IllegalArgumentException("Tenant requires a last name");
        }

        String phone = values.getAsString(TenantsContract.TenantEntry.COLUMN_PHONENUMBER);
        if(phone == null || !Patterns.PHONE.matcher(phone).matches() )
        {
            throw new IllegalArgumentException("Tenant requires a phone number");
        }

        String email = values.getAsString(TenantsContract.TenantEntry.COLUMN_EMAIL);
        if(email == null ||  !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Tenant requires a email");
        }

        if (values.containsKey(TenantsContract.TenantEntry.COLUMN_MOVEIN) &&
                values.containsKey(TenantsContract.TenantEntry.COLUMN_MOVEOUT)) {

            String movein = values.getAsString(TenantsContract.TenantEntry.COLUMN_MOVEIN);
            String moveout = values.getAsString(TenantsContract.TenantEntry.COLUMN_MOVEOUT);

            if( (movein == null || moveout == null)){// && (Double.parseDouble(movein) > Double.parseDouble(moveout))) {
                throw new IllegalArgumentException("Tenant requires valied move in and out date");
            }
        }
        // insert the content value to the database
        long id = db.insert(TenantsContract.TenantEntry.Tenants_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        /*
         *  Notify all listener that the data has changed for the pet content URI
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertFinance(Uri uri, ContentValues values)
    {
        Log.d(LOG_TAG, " insertFinance >" + uri+"<");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String roomNum = values.getAsString(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER);
        if(roomNum == null) {
            throw new IllegalArgumentException("Tenant requires a room number" );
        }

        String date = values.getAsString(TenantsContract.TenantEntry.COLUMN_DATE);
        if(date == null) {
            throw new IllegalArgumentException("Tenant requires a date for transaction" );
        }

        String type = values.getAsString(TenantsContract.TenantEntry.COLUMN_TRANSACTION_TYPE);
        if(date == null) {
            throw new IllegalArgumentException("Tenant requires a type of transaction" );
        }

        String amount = values.getAsString(TenantsContract.TenantEntry.COLUMN_AMOUNT);
        if(amount == null) {
            throw new IllegalArgumentException("Tenant requires a amount for transaction" );
        }

        // insert the content value to the database
        long id = db.insert(TenantsContract.TenantEntry.Finance_TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        /*
         *  Notify all listener that the data has changed for the pet content URI
         */
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        Log.d(LOG_TAG, " update :" + uri);

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RoomInfo:
                return updateRoom(uri, contentValues, selection, selectionArgs);
            case Tenants:
                return updateTenant(uri, contentValues, selection, selectionArgs);
            case Tenants_ID:
                selection = TenantsContract.TenantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateTenant(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
            /*
             *  private method for update room information
             */
            private int updateRoom(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
                Log.d(LOG_TAG, " updateRoom :" + uri);

                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_Vancant)) {
                    String roomNum = values.getAsString(TenantsContract.TenantEntry.COLUMN_Vancant);
                    if (roomNum == null) {
                        throw new IllegalArgumentException("Tenant requires a room number");
                    }
                }
                // If there are no values to update, then don't try to update the database
                if (values.size() == 0) {
                    return 0;
                }

                // Otherwise, get writeable database to update the data
                SQLiteDatabase database = mDbHelper.getWritableDatabase();

                // Returns the number of database rows affected by the update statement
                int returnVal =  database.update(TenantsContract.TenantEntry.ROOM_TABLE_NAME, values, selection, selectionArgs);
                 /*
                 *  Notify all listener that the data has changed for the room info content URI
                 */
                getContext().getContentResolver().notifyChange(uri, null);

                return returnVal;
            }


            /*
             *  private method for update tenant infomation
             */
            private int updateTenant(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

                Log.d(LOG_TAG, " updateTenant :" + uri);

                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER)) {
                    String roomNum = values.getAsString(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER);
                    if (roomNum == null) {
                        throw new IllegalArgumentException("Tenant requires a room number");
                    }
                }

                 if (values.containsKey(TenantsContract.TenantEntry.COLUMN_FIRSTNAME)) {
                     String name = values.getAsString(TenantsContract.TenantEntry.COLUMN_FIRSTNAME);
                     if (name == null) {
                         throw new IllegalArgumentException("Tenant requires a first name");
                     }
                 }

                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_LASTNAME)) {
                    String name = values.getAsString(TenantsContract.TenantEntry.COLUMN_LASTNAME);
                    if (name == null) {
                        throw new IllegalArgumentException("Tenant requires a last name");
                    }
                }

                // Check that the phone value is valid pattern or not
                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_PHONENUMBER)) {
                     String phone = values.getAsString(TenantsContract.TenantEntry.COLUMN_PHONENUMBER);
                     if (phone == null && !Patterns.PHONE.matcher(phone).matches()) {
                        throw new IllegalArgumentException("Tenant requires valid phone number");
                     }
                }

                // Check that the email value is valid pattern or not
                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_EMAIL)) {
                     String email = values.getAsString(TenantsContract.TenantEntry.COLUMN_EMAIL);
                    if (email != null && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        throw new IllegalArgumentException("Tenant requires valid email address");
                    }
                }

                if (values.containsKey(TenantsContract.TenantEntry.COLUMN_MOVEIN) &&
                    values.containsKey(TenantsContract.TenantEntry.COLUMN_MOVEOUT)) {

                    double movein = values.getAsDouble(TenantsContract.TenantEntry.COLUMN_MOVEIN);
                    double moveout = values.getAsDouble(TenantsContract.TenantEntry.COLUMN_MOVEOUT);

                    if( (movein != 0 || moveout != 0) && (movein > moveout) ) {
                        throw new IllegalArgumentException("Tenant requires valied move in and out date");
                    }
                }

                // If there are no values to update, then don't try to update the database
                if (values.size() == 0) {
                    return 0;
                }

                // Otherwise, get writeable database to update the data
                SQLiteDatabase database = mDbHelper.getWritableDatabase();

                // Returns the number of database rows affected by the update statement
                 int returnVal =  database.update(TenantsContract.TenantEntry.Tenants_TABLE_NAME, values, selection, selectionArgs);

                 /*
                 *  Notify all listener that the data has changed for the tenant content URI
                 */
                getContext().getContentResolver().notifyChange(uri, null);

                return returnVal;
            }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d(LOG_TAG, " delete >" + uri+"<");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int returnVal;

        switch (match) {
            case Tenants:
                returnVal = db.delete(TenantsContract.TenantEntry.Tenants_TABLE_NAME, selection, selectionArgs);
                break;

            case Tenants_ID:
                selection = TenantsContract.TenantEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                returnVal =  db.delete(TenantsContract.TenantEntry.Tenants_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

         /*
         *  Notify all listener that the data has changed for the pet content URI
         */
        getContext().getContentResolver().notifyChange(uri, null);

        return returnVal;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}