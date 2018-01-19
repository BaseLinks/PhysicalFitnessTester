package com.kangear.bodycompositionanalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PdfActivity extends AppCompatActivity {
    private String TAG = "PdfActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_20180115);
        hideSystemUI(getWindow().getDecorView());
    }
}
