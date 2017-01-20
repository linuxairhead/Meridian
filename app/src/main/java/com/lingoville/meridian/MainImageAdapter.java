package com.lingoville.meridian;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lingoville.meridian.Data.TenantsContract;

public class MainImageAdapter extends BaseAdapter {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainImageAdapter.class.getSimpleName();

    private Context mContext;

    private Cursor mCursor;

    public MainImageAdapter(Context context){
        mContext = context;
    }

    public void setCursor(Cursor c) {
        mCursor = c;
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "getView the position is " + position);

        ImageView imageView;

        if (convertView == null) {  // if it's not recycled, initialize some attributes

            imageView = new ImageView(mContext);

            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/5, screenWidth/5));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }
        TextDrawable.IBuilder drawable;
            /*
             *  mCursor which was initialized at MainActivity by query RoomVacant Table.
             *  move along the position, fetch the data, if the Vacant was set as occupied (1), set the image.
             */
            if (position >= 0 && mCursor != null && (position <= getCount())) {

                mCursor.moveToPosition(position);

                String occupied = mCursor.getString(mCursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_Vacancy));

                int roomNumber = mCursor.getInt(mCursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER));

                int floorNumber = roomNumber/100 - 1;

                if (occupied.matches("true")) {
                    // draw rectangular object
                    drawable = TextDrawable.builder().beginConfig().withBorder(20).textLocation(2).endConfig().roundRect(100);
                    /*
                    *  if the room is occupied, set the image resource and background image with rectangular
                    */
                    imageView.setBackgroundResource(R.mipmap.student2);

                    imageView.setImageDrawable(drawable.build("" + roomNumber, mContext.getResources().getColor(mFloorColor[position])));
                } else {
                    // draw rectangular object
                    drawable = TextDrawable.builder().beginConfig().withBorder(20).textLocation(1).endConfig().roundRect(100);
                    /*
                    *  If the room is unoccupied, just set the Background image with rectangular and make image resource as transparent
                    */
                    imageView.setBackgroundResource(android.R.color.transparent);

                    imageView.setImageDrawable(drawable.build("" + roomNumber, mContext.getResources().getColor(mFloorColor[floorNumber])));
                }
            }

        return imageView;
    }

    // references to our images
    private  int[] mFloorColor = {
            R.color.firstFloor, R.color.secondFloor, R.color.thirdFloor, R.color.fourthFloor, R.color.fifFloor
    };
}