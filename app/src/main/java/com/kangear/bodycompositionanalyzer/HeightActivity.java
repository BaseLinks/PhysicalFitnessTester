package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HeightActivity extends Com2Activity {
    private static final String TAG = "HeightActivity";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        setView(true, getWindow().getDecorView(), null);
        hideSystemUI(getWindow().getDecorView());
        mEditText = findViewById(R.id.tizhibi_edittext);
        mEditText.setClickable(false);
        mEditText.setEnabled(false);;
        mEditText.setText("");
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
        mEditText.addTextChangedListener(mHeightTextWatcher);
    }

    private TextWatcher mHeightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int val = Integer.parseInt(s.toString());
                if(WelcomeActivity.checkHeight(val)) {
                    mEditText.setTextColor(Color.parseColor("#F39801"));
                    mNextButton.setEnabled(true);
                } else {
                    mEditText.setTextColor(Color.RED);
                    mNextButton.setEnabled(false);
                }
            } catch (NumberFormatException ex) {
                // Do something
            }
            onContentChanged();
        }
    };

    /**
     * check is can next
     * @return
     */
    @Override
    public boolean canNext(String str) {
        return str.length() > 0;
    }

    @Override
    public void onNextButtonClick() {
        super.onNextButtonClick();
        Log.i(TAG, "onNextButtonClick");
        WelcomeActivity.getRecord().setHeight(Float.valueOf(mEditText.getText().toString()));
        WelcomeActivity.startGender(this);
        finish();
    }

    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
        exitAsFail(this);
    }
}
