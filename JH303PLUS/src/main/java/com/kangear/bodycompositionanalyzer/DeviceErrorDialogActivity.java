package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DeviceErrorDialogActivity extends AppCompatActivity {
    private static final String TAG = "AdDialogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_error);
        setFinishOnTouchOutside(false);
    }
}
