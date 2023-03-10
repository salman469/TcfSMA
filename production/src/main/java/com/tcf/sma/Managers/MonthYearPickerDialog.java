package com.tcf.sma.Managers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.R;

import java.util.Calendar;

public class MonthYearPickerDialog extends DialogFragment {

    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1999;
    private DatePickerDialog.OnDateSetListener listener;
    //    private int year,month;
    private Calendar calendar;


//    public void setListener(DatePickerDialog.OnDateSetListener listener,int year,int month) {
//        this.listener = listener;
//        this.year=year;
//        this.month=month;
//    }

    public void setListener(DatePickerDialog.OnDateSetListener listener, Calendar calendar) {
        this.listener = listener;
        this.calendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);


        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setValue(month);
        monthPicker.setDisplayedValues(AppConstants.monthArray);


//        int year = cal.get(CalendarsModel.YEAR);
//        yearPicker.setMinValue(year);
//        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(currYear);
        yearPicker.setValue(year);

        builder.setView(dialog)
                .setTitle("Month - Year")
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MonthYearPickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
