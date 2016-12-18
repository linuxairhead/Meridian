package com.lingoville.meridian;

import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lingoville.meridian.Data.TenantsContract;
import com.lingoville.meridian.Data.TenantsProvider;


public class TransactionInfoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = TransactionInfoActivity.class.getSimpleName();

    /* This variable will tell which tenant's finance table to use
    *  It will be pass from intent from previous activity */
    private int currentRoomNumber = 1;

    /*The Loader for the Cursor Loader */
    private static final int Transaction_LOADER = 0;

    TransactionCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
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
                tenantIntent.putExtra("Room_Number", currentRoomNumber );
                startActivity(tenantIntent);
            }
        });

        /*
         * initilized the TenantsDBHelper to perform
         */
        // get the room number from main activity
        currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);

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
        getLoaderManager().initLoader(Transaction_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
         /*
         * Define a projection that specifies the columns from the table care about.
         */
        String[] projection = {
                TenantsContract.TenantEntry._ID,
                TenantsContract.TenantEntry.COLUMN_ROOMNUMBER,
                TenantsContract.TenantEntry.COLUMN_DATE,
                TenantsContract.TenantEntry.COLUMN_TRANSATION_TYPE,
                TenantsContract.TenantEntry.COLUMN_AMOUNT
        };

        String whereClause = TenantsContract.TenantEntry.COLUMN_ROOMNUMBER+"=?";
        String [] whereArgs = { Integer.toString(currentRoomNumber)};
        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,             // Finance activity context
                TenantsContract.TenantEntry.FINANCE_CONTENT_URI,   // Provider content URI to query
                projection,                                                                 // Columns to include in the resulting Cursor
                null,                                                                          // No selection clause
                null,                                                                          // No selection arguments
                null );                                                                        // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         /*
         * Update TransactionCursorAdapter with this new cursor containing updated tenant data
         */
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         *   Callback called when the data needs to be deleted
         */
        mCursorAdapter.swapCursor(null);
    }
}
