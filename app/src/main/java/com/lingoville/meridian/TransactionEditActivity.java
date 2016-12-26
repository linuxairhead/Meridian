package com.lingoville.meridian;

import android.app.DialogFragment;
import android.app.LoaderManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class TransactionEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = TransactionEditActivity.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int TRANSACTION_EDIT_LOADER = 4;

    /* Tenant Uri to access Tenant Provider */
    private Uri mCurrentFinanceUri;

    private static int mCurrentRoomNumber ;
    private String today;
    private TextView mDate;
    private Spinner mType;
    private EditText mAmount;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate rm is " + mCurrentRoomNumber);
        super.onCreate(savedInstanceState);

        // get the room number from main activity
        mCurrentRoomNumber = getIntent().getIntExtra("Room_Number", 1);

        // initialized the toolbar
        setTitle("New Transaction for #" + mCurrentRoomNumber);
        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Examine the intent for the proper toolbar title
        Intent intent = getIntent();
        mCurrentFinanceUri = intent.getData();

        /*
         * if intent contain uri, current finance has been already initialized
          * so initialize cursor loader for load save finance info
         */
        if(mCurrentFinanceUri != null) {
            setTitle(R.string.title_activity_edit_transaction);
            getLoaderManager().initLoader(TRANSACTION_EDIT_LOADER, null, this);
        } else {
            setTitle(R.string.title_activity_new_transaction);
        }
        /*
         *  initialized all the local variable for the Transaction Edit Activity by setting View
         */
        mDate = (TextView) findViewById(R.id.newTrans_Date);
        mType = (Spinner) findViewById(R.id.newTrans_Type);
        List<String> typeList = new ArrayList<String>();
        typeList.add("Rent");
        typeList.add("Utility");
        typeList.add("Deposit");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(dataAdapter);
        mAmount = (EditText) findViewById(R.id.newTrans_Amount);

        /*
         * Get today's instance and set today's date.
         */
        setDatePickerAsToday();

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
                Log.d(LOG_TAG, "SaveTenant rm is " + mCurrentRoomNumber);
                // save the Tenant information.
                saveTransaction();

                // once inserted the Tenant info call go back to main screen
                Intent saveIntent = new Intent(TransactionEditActivity.this, TransactionInfoActivity.class);
                saveIntent.putExtra("Room_Number", mCurrentRoomNumber );
                finish();
                startActivity(saveIntent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Loader rm is" + mCurrentRoomNumber);

        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,                                   // Finance activity context
                mCurrentFinanceUri,       // Provider content URI to query
                TenantsContract.TenantEntry.TransactionTableProjection,    // Columns to include in the resulting Cursor
                null,                                                                                               // No selection clause
                null,                                                                                               // No selection arguments
                null );                                                                                             // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoaderFinished rm is" + mCurrentRoomNumber);
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            mDate.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_DATE)));
            mDate.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_DATE)));
            mType.setSelection(dataAdapter.getPosition(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_TRANSACTION_TYPE))));
            mAmount.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_AMOUNT)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset rm:" + mCurrentRoomNumber);
        mDate.setText(today);
        mAmount.setText("");
    }

    /*
    *  When setDatePicker initizlized, set as today's date
    */
    private void setDatePickerAsToday(){

        Log.d(LOG_TAG, "setDatePickerAsToday");

        Date curDate = (Date) java.util.Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy / MM / dd");
        String today = formatter.format(curDate);

        mDate.setText(today);
        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                Bundle aBundle = new Bundle();
                aBundle.putInt("DATE", R.id.newTrans_Date);
                dialogfragment.onCreateDialog(aBundle);
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    /*
    * Get the user input from editor and save new tenant into database.
    */
    private void saveTransaction() {
        Log.d(LOG_TAG, "insertTenant rm is " + mCurrentRoomNumber);

        // Create the content value class by reading from user input editor
        ContentValues values = new ContentValues();

        try{
            values.put(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER, mCurrentRoomNumber);
            values.put(TenantsContract.TenantEntry.COLUMN_DATE, mDate.getText().toString().trim( ));
            values.put(TenantsContract.TenantEntry.COLUMN_TRANSACTION_TYPE, mType.getSelectedItem().toString().trim());
            values.put(TenantsContract.TenantEntry.COLUMN_AMOUNT, Integer.parseInt (mAmount.getText().toString().trim()));

        } catch ( Exception e) {
            Log.e("NewTransactionActivity", "Can not parse zero string");
            Toast.makeText(this, "Error with saving New Transaction.\n Must Enter Amount", Toast.LENGTH_SHORT).show();
            return; // Or another exception handling.
        };

        /*
         * if mCurrentFinanceUri is null, it is new finance transaction
         * otherwise it is exsting finance so just need to update.
         */
        if(mCurrentFinanceUri == null ) {
            // inset new Finance

            // insert the content value to the database
            Uri newUri = getContentResolver().insert(TenantsContract.TenantEntry.FINANCE_CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, R.string.editor_insert_transaction_failed, Toast.LENGTH_SHORT).show();

            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, R.string.editor_insert_transaction_successful, Toast.LENGTH_SHORT).show();
            }

        } else {
            // update the Finance

            // Since mCurrentTenantUri located the correct row for the tenants in the database that we will just to modify.
            int rowsNeedToUpdate = getContentResolver().update(mCurrentFinanceUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsNeedToUpdate == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_transaction_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_transaction_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
