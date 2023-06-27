package com.kangear.bodycompositionanalyzer;

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
public class AdminPasswdActivity extends Com2Activity {
    private String TAG = "AgeActivity";
    private EditText mEditText;
    private EditText mTopEditText;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_passwd);
        hideSystemUI(getWindow().getDecorView());
        setView(true, getWindow().getDecorView(), null);
        mEditText = findViewById(R.id.tizhibi_edittext);
        mEditText.setVisibility(View.INVISIBLE);
        mTopEditText = findViewById(R.id.top_edittext);
        mNextButton = findViewById(R.id.kb_next_button);
        Button dot = findViewById(R.id.kb_dot_button);
        dot.setEnabled(false);
        mEditText.addTextChangedListener(mAgeTextWatcher);
        mEditText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
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
//                int val = Integer.parseInt(s.toString());
                if(s.toString().equals("6666")) {
                    mEditText.setTextColor(Color.parseColor("#F39801"));
                    mNextButton.setEnabled(true);
                } else {
                    mEditText.setTextColor(Color.RED);
                    mNextButton.setEnabled(false);
                }
                String text = "_ _ _ _";
                switch (s.toString().length()) {
                    case 0:
                        break;
                    case 1:
                        text = "* _ _ _";
                        break;
                    case 2:
                        text = "* * _ _";
                        break;
                    case 3:
                        text = "* * * _";
                        break;
                    case 4:
                        text = "* * * *";
                        break;
                }
                mTopEditText.setText(text);
            } catch (NumberFormatException ex) {
                // Do something
                ex.printStackTrace();
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
        // WelcomeActivity.getRecord().setAge(Integer.valueOf(mEditText.getText().toString()));
        WelcomeActivity.doSettings(this);
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
