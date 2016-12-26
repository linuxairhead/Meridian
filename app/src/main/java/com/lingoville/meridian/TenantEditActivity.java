package com.lingoville.meridian;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lingoville.meridian.Data.TenantsContract;



public class TenantEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /** Log Tag for debug */
    public static final String LOG_TAG = TenantEditActivity.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int CURRENT_TENANT_LOADER = 0;

    /* Tenant Uri to access Tenant Provider */
    private Uri mCurrentTenantUri;

    private int mCurrentRoomNumber;

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

        setContentView(R.layout.activity_new_tenant);
        /*
            Create toolbar
          */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /* to improve code readability initialized all the local variable with private method */
        initializeLocalVariable();

        /*
         * if intent contain uri, current tenant has been already initialized
          * so initialize cursor loader for load save tenant info
         */
        if(mCurrentTenantUri != null) {
            setTitle(R.string.title_activity_edit_tenant);
            getLoaderManager().initLoader(CURRENT_TENANT_LOADER, null, this);
        } else {

                setTitle(R.string.title_activity_new_tenant);

            /* set today''s date as Date */
            setDatePickerAsToday();
        }

        /*
         *   Handle Button for Cancel Tenant
         */
        Button cancelButton = (Button) findViewById(R.id.CancelTenant);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel : finish the current activity and go back to previous screen.
                finish();
            }
        });

        /*
         *   Handle Button for Save Tenant
         */
        Button saveButton = (Button) findViewById(R.id.SaveTenant);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // insert the Tenant information.
                    saveTenant();

                    // once inserted the Tenant info call go back to main screen
                    Intent saveIntent = new Intent(TenantEditActivity.this, TransactionInfoActivity.class);
                    saveIntent.putExtra("Room_Number", mCurrentRoomNumber);
                    finish();
                    startActivity(saveIntent);
                } catch ( IllegalArgumentException e ) {
                    Log.d(LOG_TAG, "onCreate : handling illegal argument exception");
                }
            }
        });
    }

    /*
     * Get the user input from editor and save new tenant into database.
     */
    private void saveTenant() {
        Log.d(LOG_TAG, " saveTenant ");

        // Create the content value class by reading from user input editor
        ContentValues values = new ContentValues();

        values.put(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER, mCurrentRoomNumber);
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

                ContentValues trueValues = new ContentValues();
                trueValues.put(TenantsContract.TenantEntry.COLUMN_Vacancy, "true");
                String selection = TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " = ?";
                String[] selectionArgs = new String[]{Integer.toString(mCurrentRoomNumber)};
                getContentResolver().update(TenantsContract.TenantEntry.ROOM_CONTENT_URI,trueValues, selection, selectionArgs);

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

    /*
     * When Tenant Editor shows all the proper attributes which defined projection,
     * call Cursor Loader will execute the ContentProvider's query method on background
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");

        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,              // Parent activity context
            mCurrentTenantUri,                                                  // Provider content URI to query
                TenantsContract.TenantEntry.TenantTableProjection,  // Columns to include in the resulting Cursor
                null,                                                                          // No selection clause
                null,                                                                          // No selection arguments
                null );                                                                        // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
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
        Log.d(LOG_TAG, "onLoaderReset");

        mRoomNumber.setText(""+mCurrentRoomNumber);
        mFirstName.setText("");
        mLastName.setText("");
        mPhoneNumber.setText("");
        mEmail.setText("");
        setDatePickerAsToday();
    }

    /*
    *  initialized all the local variable for the Tenant Edit Activity by setting View
    */
    private void initializeLocalVariable(){
        // get the room number from main activity
        mCurrentRoomNumber = getIntent().getIntExtra("Room_Number", 1);

        mRoomNumber = (TextView) findViewById(R.id.newTenant_RoomNumber);
        mRoomNumber.setText("" + mCurrentRoomNumber);
        mFirstName = (EditText) findViewById(R.id.newTenant_firstName);
        mLastName = (EditText) findViewById(R.id.newTenant_lastName);
        mPhoneNumber = (EditText) findViewById(R.id.newTenant_PhoneNumber);
        mEmail = (EditText) findViewById(R.id.newTenant_Email);
        mMoveInDate = (TextView) findViewById(R.id.newTenant_MoveInDate);
        mMoveOutDate = (TextView) findViewById(R.id.newTenant_MoveOutDate);

        // Examine the intent for the proper toolbar title
        Intent intent = getIntent();
        mCurrentTenantUri = intent.getData();
    }

    /*
     *  When setDatePicker initizlized, set as today's date
     */
    private void setDatePickerAsToday(){

        Log.d(LOG_TAG, "setDatePickerAsToday");

        Date curDate = (Date) java.util.Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy / MM / dd");
        String today = formatter.format(curDate);

        mMoveInDate.setText(today);
        mMoveInDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();//(R.id.newTenant_MoveInDate);
                Bundle aBundle = new Bundle();
                aBundle.putInt("DATE", R.id.newTenant_MoveInDate);
                dialogfragment.setArguments(aBundle);
                dialogfragment.onCreateDialog(aBundle);
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });

        mMoveOutDate.setText(today);
        mMoveOutDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                Bundle aBundle = new Bundle();
                aBundle.putInt("DATE", R.id.newTenant_MoveOutDate);
                dialogfragment.setArguments(aBundle);
                dialogfragment.onCreateDialog(aBundle);
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503, 504
    };
}


