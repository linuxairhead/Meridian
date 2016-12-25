package com.lingoville.meridian;

import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
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
        return roomNumber.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getRoomNumber(int position) {
        return roomNumber[position];
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "getView the position is " + position);
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes

            imageView = new ImageView(mContext);

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

            int screenWidth = metrics.widthPixels;

            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/5, screenWidth/5));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }

        TextDrawable.IBuilder drawable = TextDrawable.builder().beginConfig().withBorder(5).endConfig().roundRect(100);

        /*
         *  Set the Background image with ractagular
         */
        imageView.setBackground(drawable.build("" + getRoomNumber(position), mContext.getResources().getColor(mFloorColor[position])));

        /*
         *  mCursor which was initialized at MainActivity by query RoomVacant Table.
         *  move along the position, fetch the data, if the Vacant was set as occupied (1), set the image.
         */
        if (position >= 0 && mCursor != null) {

            mCursor.moveToPosition(position);

            String occupied = mCursor.getString(mCursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_Vacancy));

            if (occupied.matches("true")) {
                Log.d(LOG_TAG, "getView : occupied the position is " + position);
                imageView.setAlpha(100);
                imageView.setImageResource(R.mipmap.man);
            }
        }


        return imageView;
    }

    // references to our images
    private  int[] mFloorColor = {
            R.color.firstFloor, R.color.firstFloor, R.color.firstFloor, R.color.firstFloor, R.color.firstFloor,
            R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor,
            R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor,
            R.color.secondFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor,
            R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor,
            R.color.thirdFloor, R.color.thirdFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor,
            R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor,
            R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fifFloor, R.color.fifFloor,
            R.color.fifFloor, R.color.fifFloor
    };

    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503, 504
    };
}