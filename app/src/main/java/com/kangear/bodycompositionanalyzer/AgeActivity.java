package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AgeActivity extends Com2Activity {
    private String TAG = "AgeActivity";
    private EditText mEditText;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);
        hideSystemUI(getWindow().getDecorView());
        setView(true, getWindow().getDecorView(), null);
        mEditText = findViewById(R.id.tizhibi_edittext);
        mNextButton = findViewById(R.id.kb_next_button);
        Button dot = findViewById(R.id.kb_dot_button);
        dot.setEnabled(false);
        mEditText.addTextChangedListener(mAgeTextWatcher);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
        mEditText.setText("");
    }

    private TextWatcher mAgeTextWatcher = new TextWatcher() {
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
                if(WelcomeActivity.checkAge(val)) {
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
        WelcomeActivity.getRecord().setAge(Integer.valueOf(mEditText.getText().toString()));
        WelcomeActivity.startHeight(this);
        finish();
    }

    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
        exitAsFail(this);
    }

    @Override
    public void onKeyBoardButtonClick() {
        super.onKeyBoardButtonClick();
        Log.i(TAG, "onKeyBoardButtonClick");
    }
}
