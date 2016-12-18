package com.lingoville.meridian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Paint.EMBEDDED_BITMAP_TEXT_FLAG;

/**
 * Created by m88to on 12/9/2016.
 */

public class TenantsAdapter extends ArrayAdapter<Tenants> {


    //private  ArrayList<Tenants> mTenants;
    private  Context mContext;

    public TenantsAdapter(Context context) {

        super(context, 0);
        mContext = context;
    }

    /*public TenantsAdapter(Context context, ArrayList<Tenants> tenants) {

        super(context, 0, tenants);
    }*/

    public int getCount() {
        return mFloorColor.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }
        TextDrawable.IBuilder drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig().roundRect(10);

        imageView.setImageDrawable(drawable.build("Text", mContext.getResources().getColor(mFloorColor[position])));

        return imageView;
    }

    private  int[] mFloorColor = {
            R.color.firstFloor, R.color.firstFloor, R.color.firstFloor, R.color.firstFloor, R.color.firstFloor,
            R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor,
            R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor, R.color.secondFloor,
            R.color.secondFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor,
            R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor, R.color.thirdFloor,
            R.color.thirdFloor, R.color.thirdFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor,
            R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor,
            R.color.fourthFloor, R.color.fourthFloor, R.color.fourthFloor, R.color.fifFloor, R.color.fifFloor,
            R.color.fifFloor, R.color.fifFloor, R.color.fifFloor
    };


}
