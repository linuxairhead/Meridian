package com.lingoville.meridian;

import android.content.ContentUris;
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

    /*
    * The Loader for the Cursor Loader
    */
    private static final int TENANT_LOADER = 0;

    TenantCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_info);

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

                Intent intent = new Intent(TenantInfoActivity.this, TenantEditActivity.class);
                Uri currentTenantUri = ContentUris.withAppendedId(TenantsContract.TenantEntry.TENANT_CONTENT_URI, id);
                intent.setData(currentTenantUri);
                startActivity(intent);

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
                        Toast.makeText(TenantInfoActivity.this, "MenuItemClick", Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                Toast.makeText(TenantInfoActivity.this, "edit Clicked", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.item_delete:
                                Toast.makeText(TenantInfoActivity.this, "delete Clicked", Toast.LENGTH_SHORT).show();
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
        return new android.content.CursorLoader(this,                                       // Parent activity context
                TenantsContract.TenantEntry.TENANT_CONTENT_URI,    // Provider content URI to query
                projection,                                                                 // Columns to include in the resulting Cursor
                null,                                                                          // No selection clause
                null,                                                                          // No selection arguments
                null );                                                                        // Default sort order
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
         /*
         * Update TenantCursorAdapter with this new cursor containing updated tenant data
         */
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        /*
         *   Callback called when the data needs to be deleted
         */
        mCursorAdapter.swapCursor(null);
    }
}
