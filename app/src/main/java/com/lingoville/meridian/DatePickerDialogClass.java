package com.lingoville.meridian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by m88to on 12/13/2016.
 */

public class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int mID;

    private int mYear;

    private int mMonth;

    private int mDay;

    DatePickerDialog datepickerdialog;

    public DatePickerDialogClass() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar calendar = Calendar.getInstance();

        Bundle aBundle = getArguments();
        mID = aBundle.getInt("DATE");

        mYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (!this.isAdded()) { //this = current fragment
            return null;         }

        datepickerdialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,this,mYear,mMonth,mDay);

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