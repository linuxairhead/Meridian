package com.lingoville.meridian;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingoville.meridian.Data.TenantsContract;


/**
 * Created by m88to on 12/15/2016.
 */

public class TenantCursorAdapter  extends CursorAdapter {

    public static final String LOG_TAG = TenantCursorAdapter.class.getSimpleName();

    private Context mContext;

    /*
     *  Constructor
     */
    public TenantCursorAdapter(Context context, Cursor c) {
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
        return LayoutInflater.from(context).inflate(R.layout.content_tenant_info_list, parent, false);
    }

    /*
     * CursorAdapter - need two override method.
     * The bindView method is used to bind all data to a given view
     */
    @Override
    public void bindView(View view, Context context, Cursor c) {

        Log.d(LOG_TAG, "bindView");
        ImageView tenantRoomNumber = (ImageView) view.findViewById(R.id.list_TenantRoomNumber);
        TextView tenantFirstName = (TextView) view.findViewById(R.id.list_TenantFirstName);
        TextView tenantLastName = (TextView) view.findViewById(R.id.list_TenantLastName);

        String roomNum = c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER));
        TextDrawable.IBuilder drawable = TextDrawable.builder().beginConfig().withBorder(8).endConfig().roundRect(20);
        tenantRoomNumber.setImageDrawable(drawable.build(roomNum, mContext.getResources().getColor(R.color.colorDeposit)));

        try {
            String lastName = c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_LASTNAME));
            String firstName = c.getString(c.getColumnIndexOrThrow(TenantsContract.TenantEntry.COLUMN_FIRSTNAME));

            tenantLastName.setText(lastName);
            tenantFirstName.setText(firstName);
        } catch (Exception e) {

        }
    }
}
