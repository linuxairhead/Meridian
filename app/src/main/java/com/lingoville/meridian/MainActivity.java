package com.lingoville.meridian;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int CURRENT_TENANT_LOADER = 0;

    private MainImageAdapter mImageAdapter;

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Kick off the loader
        getLoaderManager().initLoader(CURRENT_TENANT_LOADER, null, this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        Log.d(LOG_TAG, "onBackPressed");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_unpaidRent) {
            Intent tenantIntent = new Intent(MainActivity.this, TenantInfoActivity.class);
            tenantIntent.putExtra("Tenant_Info", "Tenant_Unpaied_List");
            startActivity(tenantIntent);
            return true;
        } else if (id == R.id.action_vacancy) {
            Intent tenantIntent = new Intent(MainActivity.this, TenantInfoActivity.class);
            tenantIntent.putExtra("Tenant_Info", "Tenant_Vacant_List");
            startActivity(tenantIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onNavigationItemSelected");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tenant) {
            Intent tenantIntent = new Intent(MainActivity.this, TenantInfoActivity.class);
            tenantIntent.putExtra("Tenant_Info", "Tenant_Info_List");
            startActivity(tenantIntent);
            // Handle the camera action
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent tenantIntent = new Intent(MainActivity.this, TransactionEditActivity.class);
            tenantIntent.putExtra("RoomNumber", 103);
            startActivity(tenantIntent);

        } else if (id == R.id.nav_send) {
            Intent tenantIntent = new Intent(MainActivity.this, TenantEditActivity.class);
            tenantIntent.putExtra("RoomNumber", 103);
            startActivity(tenantIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503, 504
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * This loader will execute the ContentProvider's query method on a background thread
         */
        return new android.content.CursorLoader(this,                           // Parent activity context
                TenantsContract.TenantEntry.ROOM_CONTENT_URI,   // Provider content URI to query
                TenantsContract.TenantEntry.RoomTableProjection,       // Columns to include in the resulting Cursor
                null,                                                                                       // No selection clause
                null,                                                                                       // No selection arguments
                null);                                                                                       // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        gridview = (GridView) findViewById(R.id.gridview);
        mImageAdapter = new MainImageAdapter(this);
        mImageAdapter.setCursor(data);
        gridview.setAdapter(mImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + mImageAdapter.getRoomNumber(position), Toast.LENGTH_SHORT).show();

                Intent tenantIntent = new Intent(MainActivity.this, TenantEditActivity.class);
                tenantIntent.putExtra("Room_Number", roomNumber[position]);
                startActivity(tenantIntent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}