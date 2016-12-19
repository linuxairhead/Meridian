package com.lingoville.meridian;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lingoville.meridian.Data.TenantsContract;
import com.lingoville.meridian.Data.TenantsDbHelper;

// This activity is unused.
public class TenantActivity extends AppCompatActivity {

    /** Log Tag for debug */
    public static final String LOG_TAG = TenantActivity.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int List_TENANT_LOADER = 0;

    /* Tenant Uri to access Tenant Provider */
    private Uri mListTenantUri;

    private TextView mRoomNumber;
    private EditText mFirstName;
    private EditText mLastName;
    private TextView mMoveInDate;
    private TextView mMoveOutDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayTenantInfo( int roomnumber ){

         String[] projection = {
                TenantsContract.TenantEntry._ID,
                TenantsContract.TenantEntry.COLUMN_ROOMNUMBER,
                TenantsContract.TenantEntry.COLUMN_FIRSTNAME,
                TenantsContract.TenantEntry.COLUMN_LASTNAME,
                TenantsContract.TenantEntry.COLUMN_EMAIL,
                TenantsContract.TenantEntry.COLUMN_PHONENUMBER,
                TenantsContract.TenantEntry.COLUMN_MOVEIN,
                TenantsContract.TenantEntry.COLUMN_MOVEOUT
        };

/*
        TextView displayView = (TextView) findViewById(R.id.content_tenant);

        try{
            // Create a header in the Text View
            displayView.setText(" the tenant table contains " + cursor.getCount() + " Tenant\n\n");
            displayView.append(TenantsContract.TenantEntry._ID + " - " +
                    TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " - " +
                    TenantsContract.TenantEntry.COLUMN_FIRSTNAME + " - " +
                    TenantsContract.TenantEntry.COLUMN_LASTNAME + " - " +
                    TenantsContract.TenantEntry.COLUMN_EMAIL + " - " +
                    TenantsContract.TenantEntry.COLUMN_PHONENUMBER + "\n");

            int index = cursor.getColumnIndex(TenantsContract.TenantEntry._ID);

            while (cursor.moveToNext()) {

                if(roomnumber == cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER))
                displayView.append( "\n" +
                        cursor.getInt(cursor.getColumnIndex(TenantsContract.TenantEntry._ID)) + "    " +
                        cursor.getInt(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER)) + "    " +
                        cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_FIRSTNAME)) + "    " +
                        cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_LASTNAME)) + "    " +
                        cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_EMAIL)) + "    " +
                        cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_PHONENUMBER)) + "    " );

            }
        } finally {
            cursor.close();
        }
    }*/

}
    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503
    };
}
