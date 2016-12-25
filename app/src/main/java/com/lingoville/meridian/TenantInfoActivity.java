package com.lingoville.meridian;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;

public class TenantInfoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = TenantInfoActivity.class.getSimpleName();

    /*  The Loader for the Cursor Loader  */
    private static final int TENANT_LOADER = 0;

    /* Content URI for the Current Tenant */
    private Uri mCurrentTenentUri;

    private TenantCursorAdapter mCursorAdapter;

    private String mTenantInfoAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_info);

        /* Tenant List will view among three

         /*  Create toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel the activity and go back to main screen.
                Intent goBackToMain = new Intent(TenantInfoActivity.this, MainActivity.class);
                startActivity(goBackToMain);
            }
        });
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);

        final ListView tenantList = (ListView) findViewById(R.id.list_tenant_info);
        tenantList.setClickable(true);
        tenantList.setLongClickable(true);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_tenant_view);
        tenantList.setEmptyView(emptyView);

        tenantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Long Click");
                mCurrentTenentUri = ContentUris.withAppendedId(TenantsContract.TenantEntry.TENANT_CONTENT_URI, id);

                LinearLayout tenantListItem = (LinearLayout)findViewById(R.id.list_tenant_info_items) ;
                //Creating the instance of PopupMenu
                PopupMenu popupMenu = new PopupMenu(TenantInfoActivity.this, tenantListItem );
                //Inflating the Popup using xml file
                popupMenu.getMenuInflater().inflate(R.menu.tenants_popup, popupMenu.getMenu());
                //registering popup with OnMenuItemClickListener

                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(LOG_TAG, "Menu Item Click");

                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                Log.d(LOG_TAG, "Menu Item Click: Edit");
                                Toast.makeText(TenantInfoActivity.this, "Edit Clicked", Toast.LENGTH_SHORT).show();

                                Intent tenantIntent = new Intent(TenantInfoActivity.this, TenantEditActivity.class);
                                tenantIntent.setData(mCurrentTenentUri);
                                startActivity(tenantIntent);
                                return true;

                            case R.id.item_delete:
                                Log.d(LOG_TAG, "Menu Item Click: Delete");
                                deleteTenent();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();//showing popup menu*/
                return true;
            }
        });
        // Setup cursor adapter using cursor from last step
        mCursorAdapter = new TenantCursorAdapter(this, null);
        // Attach cursor adapter to the ListView
        tenantList.setAdapter(mCursorAdapter);
        // Kick off the loader
        getLoaderManager().initLoader(TENANT_LOADER, null, this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Loader");
        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,                                                  // Parent activity context
                    TenantsContract.TenantEntry.TENANT_CONTENT_URI,                   // Provider content URI to query
                    TenantsContract.TenantEntry.TenantTableProjection,                        // Columns to include in the resulting Cursor
                    null,                                                                                                           // No selection clause
                    null,                                                                                                           // No selection arguments
                    null);                                                                                                          // Default sort order
     }


    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
         /*
         * Update TenantCursorAdapter with this new cursor containing updated tenant data
         */
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        /*
         *   Callback called when the data needs to be deleted
         */
        mCursorAdapter.swapCursor(null);
    }

    private void deleteTenent() {
        Log.d(LOG_TAG, "deleteTenent");
        // Only perform the delete if this is an existing pet.
        if (mCurrentTenentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentTenentUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentTenentUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Failed to Deleted", //getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Successfully Deleted", //getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
    /*
     *  Perform the deletion of the tenents in the database
     */
    private int[] listOfRoom = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503
    };
}

