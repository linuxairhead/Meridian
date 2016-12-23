package com.lingoville.meridian;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MainImageAdapter extends BaseAdapter {

    private Context mContext;

    public MainImageAdapter(Context context){
        mContext = context;
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
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            //imageView.setLayoutParams(new GridView.LayoutParams(250, 200));
            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/5, screenWidth/5));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        TextDrawable.IBuilder drawable = TextDrawable.builder().beginConfig().withBorder(5).endConfig().roundRect(100);
        imageView.setImageDrawable(drawable.build(""+getRoomNumber(position), mContext.getResources().getColor(mFloorColor[position])));

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
