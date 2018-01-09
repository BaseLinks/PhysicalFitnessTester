package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TimeActivity extends AppCompatActivity {
    private static final String TAG = "TimeActivity";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        hideSystemUI(getWindow().getDecorView());

        mDatePicker = findViewById(R.id.datepicker);
        mTimePicker = findViewById(R.id.timepicker);
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_button:
//                finish();
                break;

            case R.id.yes_button:
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, mDatePicker.getYear());
                c.set(Calendar.MONTH, mDatePicker.getMonth());
                c.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
                c.set(Calendar.HOUR, mTimePicker.getCurrentHour());
                c.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                long when = c.getTimeInMillis();
                if (when / 1000 < Integer.MAX_VALUE) {
                    boolean ret = SystemClock.setCurrentTimeMillis(when);
                    String text = "设置时间: " + ret;
                    Log.i(TAG, text);
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                }

                String text = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + "-" + String.valueOf(hour) + "-" + String.valueOf(minute);
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
//                finish();
                break;
        }
    }
}
