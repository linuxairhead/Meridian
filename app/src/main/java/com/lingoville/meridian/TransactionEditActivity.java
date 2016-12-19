package com.lingoville.meridian;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;

import java.util.Calendar;
import java.util.Date;

public class TransactionEditActivity extends AppCompatActivity {

    public static final String LOG_TAG = TransactionEditActivity.class.getSimpleName();

    private static int currentRoomNumber ;
    private String today;
    private String type;
    Integer amountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate rm is " + currentRoomNumber);
        super.onCreate(savedInstanceState);


        // get the room number from main activity
        currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);
        setTitle("New Transaction for #" + currentRoomNumber);

        setContentView(R.layout.activity_new_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.newTrans_Date);

        Date curDate = (Date) Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyy / MM / dd");
        today = formatter.format(curDate);
        textView.setText(today);

        Button cancelButton = (Button) findViewById(R.id.CancelTenant);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cancel the activity and go back to main screen.
                Intent cancelIntent = new Intent(TransactionEditActivity.this, TransactionInfoActivity.class);
                cancelIntent.putExtra("RoomNumber", currentRoomNumber );
                finish();
                startActivity(cancelIntent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.SaveTenant);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "SaveTenant rm is " + currentRoomNumber);
                // insert the Tenant information.
                insertTenant();

                // once inserted the Tenant info call go back to main screen
                Intent saveIntent = new Intent(TransactionEditActivity.this, TransactionInfoActivity.class);
                saveIntent.putExtra("RoomNumber", currentRoomNumber );
                finish();
                startActivity(saveIntent);
            }
        });
    }

    /*
 * Get the user input from editor and save new tenant into database.
 */
    private void insertTenant() {
        Log.d(LOG_TAG, "insertTenant rm is " + currentRoomNumber);

        // Create the content value class by reading from user input editor
        ContentValues values = new ContentValues();

        try{

            type = ((Spinner) findViewById(R.id.newTrans_Type)).getSelectedItem().toString().trim();
            amountText = Integer.parseInt (((EditText) findViewById(R.id.newTrans_Amount)).getText().toString().trim());
            // get the room number from main activity
            currentRoomNumber = getIntent().getIntExtra("Room_Number", 1);

        } catch ( Exception e) {
            Log.e("NewTransactionActivity", "Can not parse zero string");
            Toast.makeText(this, "Error with saving New Transaction.\n Must Enter Amount", Toast.LENGTH_SHORT).show();
            return; // Or another exception handling.
        };

        values.put(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER, currentRoomNumber);
        values.put(TenantsContract.TenantEntry.COLUMN_DATE, today );
        values.put(TenantsContract.TenantEntry.COLUMN_TRANSATION_TYPE, type );
        values.put(TenantsContract.TenantEntry.COLUMN_AMOUNT,amountText);

        // insert the content value to the database
        Uri newUri = getContentResolver().insert(TenantsContract.TenantEntry.FINANCE_CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving New Transaction", Toast.LENGTH_SHORT).show();

        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "New Transaction saved with row id: ", Toast.LENGTH_SHORT).show();
        }
    }

}
