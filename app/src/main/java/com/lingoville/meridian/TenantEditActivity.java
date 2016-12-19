package com.lingoville.meridian;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;


import java.util.Date;

public class TenantEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /** Log Tag for debug */
    public static final String LOG_TAG = TenantEditActivity.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int CURRENT_TENANT_LOADER = 0;

    TenantCursorAdapter mCursorAdapter;

    /* Tenant Uri to access Tenant Provider */
    private Uri mCurrentTenantUri;

    private int currentRoomNumber;
    private TextView mRoomNumber;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNumber;
    private EditText mEmail;
    private TextView mMoveInDate;
    private TextView mMoveOutDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        Log.d(LOG_TAG, "onCreate " );

        super.onCreate(savedInstanceState);

        // get the room number from main activity
        currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);

        setContentView(R.layout.activity_new_tenant);
        /*
            Create toolbar
          */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Examine the intent for the proper toolbar title
        Intent intent = getIntent();
        mCurrentTenantUri = intent.getData();
        /*
         * if intent contain uri, current tenant has uri so initialize cursor loader.
         */
        if(mCurrentTenantUri != null) {
            setTitle(R.string.title_activity_edit_tenant);
            getLoaderManager().initLoader(CURRENT_TENANT_LOADER, null, this);
        } else {

            currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);
            /*
             * check for whether the current room has occupied or not.
             * if occupied, call transactionInfoActivity to view transaction history
             * if not occupied, continue edit activity with new tenant title
             */
            if (occupiedRoom()) {
                Intent transactionIntent = new Intent(TenantEditActivity.this, TransactionInfoActivity.class);
                transactionIntent.putExtra("Room_Number", currentRoomNumber);
                finish();
                startActivity(transactionIntent);
            } else
                setTitle(R.string.title_activity_new_tenant);

            /*
             *  initialized all the local variable for the Tenant Edit Activity by setting View
             */
            mRoomNumber = (TextView) findViewById(R.id.newTenant_RoomNumber);
            mRoomNumber.setText("" + currentRoomNumber);
            mFirstName = (EditText) findViewById(R.id.newTenant_firstName);
            mLastName = (EditText) findViewById(R.id.newTenant_lastName);
            mPhoneNumber = (EditText) findViewById(R.id.newTenant_PhoneNumber);
            mEmail = (EditText) findViewById(R.id.newTenant_Email);
            mMoveInDate = (TextView) findViewById(R.id.newTenant_MoveInDate);
            mMoveOutDate = (TextView) findViewById(R.id.newTenant_MoveOutDate);

            /*
             * set today''s date as Date
             */
            setDatePickerAsToday();
        }

        Button cancelButton = (Button) findViewById(R.id.CancelTenant);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel the activity and go back to main screen.
                Intent cancelIntent = new Intent(TenantEditActivity.this, MainActivity.class);
                finish();
                startActivity(cancelIntent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.SaveTenant);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // insert the Tenant information.
                    saveTenant();

                    // once inserted the Tenant info call go back to main screen
                    Intent saveIntent = new Intent(TenantEditActivity.this, TransactionInfoActivity.class);
                    saveIntent.putExtra("Room_Number", currentRoomNumber);
                    finish();
                    startActivity(saveIntent);
                } catch ( IllegalArgumentException e ) {
                    Log.d(LOG_TAG, "onCreate : handling illegal argument exception");

                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }

    /*
     *  When setDatePicker initizlized, set as today's date
     */
    private void setDatePickerAsToday(){
        Date curDate = (Date) java.util.Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyy / MM / dd");
        String today = formatter.format(curDate);

        mMoveInDate.setText(today);
        mMoveInDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass(R.id.newTenant_MoveInDate);
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });

        mMoveOutDate.setText(today);
        mMoveOutDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass(R.id.newTenant_MoveOutDate);
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    /*
     * Get the user input from editor and save new tenant into database.
     */
    private void saveTenant() {
        Log.d(LOG_TAG, " insertTenant ");

        // Create the content value class by reading from user input editor
        ContentValues values = new ContentValues();

        values.put(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER, currentRoomNumber);
        values.put(TenantsContract.TenantEntry.COLUMN_FIRSTNAME, mFirstName.getText().toString().trim());
        values.put(TenantsContract.TenantEntry.COLUMN_LASTNAME, mLastName.getText().toString().trim());
        values.put(TenantsContract.TenantEntry.COLUMN_PHONENUMBER, mPhoneNumber.getText().toString().trim());
        values.put(TenantsContract.TenantEntry.COLUMN_EMAIL, mEmail.getText().toString().trim());
        values.put(TenantsContract.TenantEntry.COLUMN_MOVEIN, mMoveInDate.getText().toString().trim());
        values.put(TenantsContract.TenantEntry.COLUMN_MOVEOUT, mMoveOutDate.getText().toString().trim());

        /*
         * if mCurrentTenantUri is null, it is new tenant
         * otherwise it is exsting tenant so just need to update.
         */
        if(mCurrentTenantUri == null ) {
            // inset new Tenant

            /*
             * This is new Tenant, so insert a new Tenant into the provider,
             * And it will return the content URI for the new Tenant
             */
            Uri newUri = getContentResolver().insert(TenantsContract.TenantEntry.TENANT_CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_tenant_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_tenant_successful), Toast.LENGTH_SHORT).show();
            }
        } else {
            // update the tenant

            // Since mCurrentTenantUri located the correct row for the tenants in the database that we will just to modify.
            int rowsNeedToUpdate = getContentResolver().update(mCurrentTenantUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsNeedToUpdate == 0) {
                 // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_tenant_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_tenant_successful), Toast.LENGTH_SHORT).show();
            }
        }


    }
    private boolean occupiedRoom() {

        Log.d(LOG_TAG, "occupiedRoom");
        boolean returnValue = false;

        String[] projection = {
                TenantsContract.TenantEntry._ID,
                TenantsContract.TenantEntry.COLUMN_ROOMNUMBER,
                TenantsContract.TenantEntry.COLUMN_FIRSTNAME,
                TenantsContract.TenantEntry.COLUMN_LASTNAME,
                TenantsContract.TenantEntry.COLUMN_PHONENUMBER,
                TenantsContract.TenantEntry.COLUMN_EMAIL,
                TenantsContract.TenantEntry.COLUMN_MOVEIN,
                TenantsContract.TenantEntry.COLUMN_MOVEOUT,
        };

        Cursor cursor = getContentResolver().query(TenantsContract.TenantEntry.TENANT_CONTENT_URI, projection, null, null, null );
        while (cursor.moveToNext()) {
            // Get the tenant entry and check whether the room is occupied or not.
            int roomNumber = cursor.getInt(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER));
            if (currentRoomNumber == roomNumber) {
                returnValue = true;
                break;
            }
        }
        Log.d(LOG_TAG, "occupiedRoom " + returnValue);
        return returnValue;
    }

    /*
     * When Tenant Editor shows all the proper attributes which defined projection,
     * call Cursor Loader will execute the ContentProvider's query method on background
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            /*
             * Define a projection that specifies the columns from the table care about.
             */
         String[] projection = {
                TenantsContract.TenantEntry._ID,
                TenantsContract.TenantEntry.COLUMN_ROOMNUMBER,
                TenantsContract.TenantEntry.COLUMN_FIRSTNAME,
                TenantsContract.TenantEntry.COLUMN_LASTNAME,
                TenantsContract.TenantEntry.COLUMN_EMAIL,
                TenantsContract.TenantEntry.COLUMN_PHONENUMBER,
                TenantsContract.TenantEntry.COLUMN_MOVEIN,
                TenantsContract.TenantEntry.COLUMN_MOVEOUT };

        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,              // Parent activity context
            mCurrentTenantUri,                                                  // Provider content URI to query
                projection,                                                                 // Columns to include in the resulting Cursor
                null,                                                                          // No selection clause
                null,                                                                          // No selection arguments
                null );                                                                        // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of tenant attributes that we're interested in
            int roomNumColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER);
            int fNameColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_FIRSTNAME);
            int lNameColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_LASTNAME);
            int phoneNumColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_PHONENUMBER);
            int emailColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_EMAIL);
            int inDateColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_MOVEIN);
            int outDateColumnIndex = cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_MOVEOUT);

            // Extract out the value from the Cursor for the given column index
            String roomNum = cursor.getString(roomNumColumnIndex);
            String fName = cursor.getString(fNameColumnIndex);
            String lName = cursor.getString(lNameColumnIndex);
            String phoneNum = cursor.getString(phoneNumColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String inDate = cursor.getString(inDateColumnIndex);
            String outDate = cursor.getString(outDateColumnIndex);

            // Update the views on the screen with the values from the database
            mRoomNumber.setText(roomNum);
            mFirstName.setText(fName);
            mLastName.setText(lName);
            mPhoneNumber.setText(phoneNum);
            mEmail.setText(email);
            mMoveInDate.setText(inDate);
            mMoveOutDate.setText(outDate);
        }
    }

    /*
     *  If the Tenant loader is invalidated, clear out all the data and reset the value
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //mRoomNumber = (TextView) findViewById(R.id.newTenant_RoomNumber);
        mFirstName.setText("");
        mLastName.setText("");
        mPhoneNumber.setText("");
        mEmail.setText("");
        setDatePickerAsToday();
    }

    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503
    };
}


