package com.lingoville.meridian;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lingoville.meridian.Data.TenantsContract;


public class TransactionInfoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = TransactionInfoActivity.class.getSimpleName();

    /* This variable will tell which tenant's finance table to use
    *  It will be pass from intent from previous activity */
    private static int currentRoomNumber;

    /*The Loader for the Cursor Loader */
    private static final int TRANSACTION_INFO_LOADER = 3;

    TransactionCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate rm is " + currentRoomNumber);
        super.onCreate(savedInstanceState);

        // get the room number from main activity
        currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);
        setTitle("Transaction for #" + currentRoomNumber);

        setContentView(R.layout.activity_transaction_history);
        /*
         * Toolbar, revert button will take back to mainActivity
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenantIntent = new Intent(TransactionInfoActivity.this, MainActivity.class);
                tenantIntent.putExtra("Room_Number", currentRoomNumber);
                finish();
                startActivity(tenantIntent);
            }
        });
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);

        /*
        * Floating Action Button to add new transaction (TrasactionEditActivity)
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tenantIntent = new Intent(TransactionInfoActivity.this, TransactionEditActivity.class);
                // get the room number from main activity
                currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);
                tenantIntent.putExtra("Room_Number", currentRoomNumber );
                finish();
                startActivity(tenantIntent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list_transaction_history);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_transaction_view);
        petListView.setEmptyView(emptyView);

        // Setup cursor adapter using cursor from last step
        mCursorAdapter = new TransactionCursorAdapter(this, null);
        // Attach cursor adapter to the ListView
        petListView.setAdapter(mCursorAdapter);
        // Kick off the loader
        getLoaderManager().initLoader(TRANSACTION_INFO_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        currentRoomNumber = this.getCurrentRoomNumber();

        Log.d(LOG_TAG, "onCreateLoader rm is " + currentRoomNumber);


        /*
         * To display Transaction info          *
         *  query(SELECT * FROM financeTable WHERE Room_Number = "currentRoomNumber")
         *  selection will set as "Room_Number = ?"
         *  selectionArgs will set as currentRoomnumber
         */
        String selection = TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " = ?";
        String [] selectionArgs = new String[] { Integer.toString(currentRoomNumber)};
        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
    return new android.content.CursorLoader(this,                                   // Finance activity context
            TenantsContract.TenantEntry.FINANCE_CONTENT_URI,       // Provider content URI to query
            TenantsContract.TenantEntry.TransactionTableProjection,    // Columns to include in the resulting Cursor
            selection,                                                                                        // No selection clause
            selectionArgs,                                                                                 // No selection arguments
            null );                                                                                              // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
         /*
         * Update TransactionCursorAdapter with this new cursor containing updated tenant data
         */
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        /*
         *   Callback called when the data needs to be deleted
         */
        mCursorAdapter.swapCursor(null);
    }

    private int getCurrentRoomNumber(){
        return currentRoomNumber;
    }

}
