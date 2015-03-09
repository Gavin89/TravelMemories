package com.hardygtw.travelmemories;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	private Button dateTitle;
	
	public DatePickerFragment(Button dateTitle) {
		this.dateTitle = dateTitle;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH); 

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	// when dialog box is closed, below method will be called.
	public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
		int day = selectedDay;
		int month = selectedMonth;
		int year = selectedYear;


		// set selected date into Text View
		dateTitle.setText(new StringBuilder().append(day)
				.append("-").append(month + 1).append("-").append(year).append(" "));

	}
}