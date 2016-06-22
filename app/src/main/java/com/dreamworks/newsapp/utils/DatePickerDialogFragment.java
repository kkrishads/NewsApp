package com.dreamworks.newsapp.utils;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment {
    public static final String TIME_IN_MILLIS = "com.android.tamilmarxist.utils.TIME_IN_MILLIS";

    private OnDateSetListener mDateSetCallback;
    private boolean isMinDate = false;
    private boolean isMaxDate = false;

    public DatePickerDialogFragment() {

    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mDateSetCallback = listener;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        Calendar c = Calendar.getInstance();
        long time = b.getLong(TIME_IN_MILLIS);

        if (time < System.currentTimeMillis()) {
            time = System.currentTimeMillis();
        }
        c.setTimeInMillis(time);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            boolean fired = false;

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (fired) {
                    return;
                }
                fired = true;
                if (mDateSetCallback != null) {
                    mDateSetCallback.onDateSet(year, monthOfYear, dayOfMonth);
                }
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
        if (DeviceUtils.hasHoneycomb() && isMinDate) {
            DatePicker dp = dialog.getDatePicker();
            long currentTime = System.currentTimeMillis();
            currentTime -= 1000 * 60 * 60;
            dp.setMinDate(currentTime);
        }

        if (DeviceUtils.hasHoneycomb() && isMaxDate) {
            DatePicker dp = dialog.getDatePicker();
            long currentTime = System.currentTimeMillis();
            currentTime -= 1000 * 60 * 60;
            dp.setMaxDate(currentTime);
        }

        return dialog;
    }

    public void setCurrentDateAsMinDate(boolean minDate) {
        this.isMinDate = minDate;
    }

    public void setCurrentDateAsMaxDate(boolean maxDate) {
        this.isMaxDate = maxDate;
    }

    public interface OnDateSetListener {
        void onDateSet(int year, int monthOfYear, int dayOfMonth);
    }
}