package com.lingoville.meridian;

import android.Manifest;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    /*
     * Internal static identifier
     */
    private static final int PICK_Camera_IMAGE = 0;
    private static final int PICK_Gallery_IMAGE=1;
    private static final int REQUEST_READ_MEDIA = 2;

    /* Main Activity pass Current Room Number */
    private int mCurrentRoomNumber;

    /*
     * View variable to set the Tenant Edit Activity
     */
    private ImageView mImageView;
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
         * Create toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /* initialized all the local variable */
        initializeLocalVariable();

        /*
         * if intent contain uri, current tenant has been already initialized
          * so initialize cursor loader for load save tenant info
         */
        if(mCurrentTenantUri != null) {

            getLoaderManager().initLoader(CURRENT_TENANT_LOADER, null, this);
        } else {
            setTitle(getString(R.string.title_activity_new_tenant) + mCurrentRoomNumber);

            /* set today''s date as Date */
            setDatePickerAsToday();
        }

        /*
        * Floating Action Button to add new transaction (TrasactionEditActivity)
        */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tenantImage_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating the instance of PopupMenu
                PopupMenu popupMenu = new PopupMenu(TenantEditActivity.this, view, Gravity.END );
                //Inflating the Popup using xml file
                popupMenu.getMenuInflater().inflate(R.menu.tenantimage_popup, popupMenu.getMenu());
                //registering popup with OnMenuItemClickListener

                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(LOG_TAG, "Menu Item Click");

                       switch (item.getItemId()) {
                           case R.id.item_CameraImage:
                               Log.d(LOG_TAG, "Menu Item Click: take photo");

                               Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                               startActivityForResult(takePicture, PICK_Camera_IMAGE);

                               return true;

                           case R.id.item_GalleryImage:
                               Log.d(LOG_TAG, "Menu Item Click: select gallery");

                               Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                               startActivityForResult(pickPhoto , PICK_Gallery_IMAGE);

                               return true;
                       }
                       return false;
                   }
               });
               popupMenu.show();//showing popup menu*/
           }
       });
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
            // Update the views on the screen with the values from the database
            mCurrentRoomNumber = cursor.getInt(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER));
            setTitle(getString(R.string.title_activity_edit_tenant) + mCurrentRoomNumber);
            mFirstName.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_FIRSTNAME)));
            mLastName.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_LASTNAME)));
            mPhoneNumber.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_PHONENUMBER)));
            mEmail.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_EMAIL)));
            mMoveInDate.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_MOVEIN)));
            mMoveOutDate.setText(cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_MOVEOUT)));
        }
    }

    /*
     *  If the Tenant loader is invalidated, clear out all the data and reset the value
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d(LOG_TAG, "onLoaderReset");

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

        Log.d(LOG_TAG, "initializeLocalVariable " );

        // get the room number from main activity
        mImageView = (ImageView) findViewById(R.id.newTenant_image);
        mCurrentRoomNumber = getIntent().getIntExtra("Room_Number", 1);
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
                DialogFragment dialogfragment = new DatePickerDialogClass();
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

    /*
     * When it return from Camera or Gallery Activity,
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.d(LOG_TAG, "onActivityResult ");

        /* check whether able to get the image from camera or gallery */
        if (resultCode == RESULT_OK) {

            /* Check to see whether it obtain the read permission from external source
             *  And If it failed to obtain the read permission, request the read permission.
             */
            if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_MEDIA);
            } else {
                // get the image uri from previous activity and set the View
                previewCapturedImage(imageReturnedIntent.getData());
            }
        } else {
            Log.d(LOG_TAG, "onActivityResult : Unable to get the image");
        }
    }

    /*
     * From image uri, get the Bitmap, resize to Bitmap and set to the View
     */
    private void previewCapturedImage(Uri photoUri) {
        Log.d(LOG_TAG, "previewCapturedImage " );

        try {
            // Get the image from uri and resize
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

            // Set Bitmap to View
            mImageView.setImageBitmap(resizedBitmap);

        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "previewCapturedImage : NullPointerException" );
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "previewCapturedImage : FileNotFoundException" );
            e.printStackTrace();
        }
    }

}


