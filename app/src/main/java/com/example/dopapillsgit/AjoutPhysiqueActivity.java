package com.example.dopapillsgit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AjoutPhysiqueActivity extends AppCompatActivity {

    public static final String TAG = "ajout_activite";
    public static final String TAG2 = "ajout_activite";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    public static final String TAG3 = "ajout_activite";
    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    public static final String TAG4 = "ajout_activite";
    private TextView mDisplayTime2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_activite_physique);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int number = cal.get(Calendar.DAY_OF_MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjoutPhysiqueActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet : dd/mm/yyyy " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime = (TextView) findViewById(R.id.heure);
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutPhysiqueActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Log.d(TAG2, "onTimeSet : hh:mm " + hour + ":" + minute);
                String time = hour + ":" + minute;
                mDisplayTime.setText(time);
            }

        };

        mDisplayDate2 = (TextView) findViewById(R.id.tvDate2);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int number = cal.get(Calendar.DAY_OF_MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjoutPhysiqueActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG3, "onDateSet : dd/mm/yyyy " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate2.setText(date);
            }
        };

        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2 = (TextView) findViewById(R.id.heure);
        mDisplayTime2 = (TextView) findViewById(R.id.heure2);
        mDisplayTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                Calendar cal2 = Calendar.getInstance();
                int hour = cal2.get(Calendar.HOUR);
                int minute = cal2.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AjoutPhysiqueActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener2, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Log.d(TAG4, "onTimeSet : hh:mm " + hour + ":" + minute);
                String time = hour + ":" + minute;
                mDisplayTime2.setText(time);
            }

        };
    }
}
