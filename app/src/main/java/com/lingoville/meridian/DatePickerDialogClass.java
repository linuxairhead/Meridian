package com.lingoville.meridian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by m88to on 12/13/2016.
 */

public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int mID;

    public DatePickerDialogClass(int rID) {
        mID = rID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

        return datepickerdialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){

        TextView textview;

        if(mID == R.id.newTenant_MoveInDate )
            textview = (TextView)getActivity().findViewById(R.id.newTenant_MoveInDate);
        else if(mID == R.id.newTenant_MoveOutDate)
            textview = (TextView)getActivity().findViewById(R.id.newTenant_MoveOutDate);
        else
            textview = (TextView)getActivity().findViewById(R.id.newTrans_Date);

        textview.setText(year + " / " +  (month+1) + " / " + day);
    }
}