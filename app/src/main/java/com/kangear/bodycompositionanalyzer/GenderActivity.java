package com.kangear.bodycompositionanalyzer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.kangear.bodycompositionanalyzer.Person.GENDER_FEMALE;
import static com.kangear.bodycompositionanalyzer.Person.GENDER_MALE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GenderActivity extends Com2Activity {
    private String TAG = "GenderActivity";
    private RadioGroup mSexRadioGroup;
    private EditText mEditText;
    private TextView mGenderEnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        hideSystemUI(getWindow().getDecorView());
        setView(true, getWindow().getDecorView(), null);
        setLongBackButton();
        mEditText = findViewById(R.id.tizhibi_edittext);
        mGenderEnTextView = findViewById(R.id.gender_en_textview);
        mSexRadioGroup = findViewById(R.id.sex_radiogroup);
        dissAllwithoutBackNext();
        mSexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.male_radiobutton:
                        mEditText.setText(GENDER_MALE);
                        if (mGenderEnTextView != null)
                            mGenderEnTextView.setText("Male");
                        break;
                    case R.id.female_radiobutton:
                        mEditText.setText(GENDER_FEMALE);
                        if (mGenderEnTextView != null)
                            mGenderEnTextView.setText("Female");
                        break;
                }
            }
        });
    }

    private void dissAllwithoutBackNext() {
        int[] reses = {
                R.id.kb_0_button,
                R.id.kb_1_button,
                R.id.kb_2_button,
                R.id.kb_3_button,
                R.id.kb_4_button,
                R.id.kb_5_button,
                R.id.kb_6_button,
                R.id.kb_7_button,
                R.id.kb_8_button,
                R.id.kb_9_button,
                R.id.kb_delete_button,
                R.id.kb_dot_button,
        };

        for (int res : reses) {
            findViewById(res).setVisibility(View.INVISIBLE);
        }
    }

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
        WelcomeActivity.getRecord().setGender(mEditText.getText().toString());
        WelcomeActivity.doTest(this);
        finish();
    }

    @Override
    public void onBackButtonClick() {
        super.onBackButtonClick();
        Log.i(TAG, "onBackButtonClick");
        finish();
    }

    @Override
    public void onKeyBoardButtonClick() {
        super.onKeyBoardButtonClick();
        Log.i(TAG, "onKeyBoardButtonClick");
    }
}
