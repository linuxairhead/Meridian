package com.lingoville.meridian;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingoville.meridian.Data.TenantsContract;

import static java.security.AccessController.getContext;

/**
 * Created by m88to on 12/14/2016.
 */

public class TransactionCursorAdapter extends CursorAdapter{

    public static final String LOG_TAG = TransactionCursorAdapter.class.getSimpleName();

    private Context mContext;

    /*
     *  Constructor
     */
    public TransactionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    /*
     * CursorAdapter - need two override method.
     * The newView method is used to inflate a new view and return it.
     */
    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        Log.d(LOG_TAG, "newView");
        return LayoutInflater.from(context).inflate(R.layout.content_transaction_history_list, parent, false);
    }

    /*
     * CursorAdapter - need two override method.
     * The bindView method is used to bind all data to a given view
     */
    @Override
    public void bindView(View view, Context context, Cursor c) {

        Log.d(LOG_TAG, "bindView");
        ImageView transType = (ImageView) view.findViewById(R.id.list_TransType);
        TextView transDate = (TextView) view.findViewById(R.id.list_TransDate);
        TextView transAmount = (TextView) view.findViewById(R.id.list_TransAmount);
        //TextView transTotal = (TextView) view.findViewById(R.id.list_TotalAmount);

        String type = c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_TRANSATION_TYPE));
        TextDrawable.IBuilder drawable = TextDrawable.builder().beginConfig().withBorder(8).endConfig().roundRect(20);
        transType.setImageDrawable(drawable.build(""+type, mContext.getResources().getColor(getTypeColor(type))));

        String amount =  c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_AMOUNT));
        transAmount.setText(" $" + amount);
        transDate.setText(c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_DATE)));
    }

    private int getTypeColor(String type) {
        int transColorType=0;

        switch (type) {
            case "Security Deposit":
            case "Deposit":
                transColorType = R.color.colorDeposit;
                break;
            case "Rent":
                transColorType = R.color.colorRent;
                break;
            case "Utility":
                transColorType = R.color.colorUtility;
                break;
        }

        return transColorType;
    }
}
